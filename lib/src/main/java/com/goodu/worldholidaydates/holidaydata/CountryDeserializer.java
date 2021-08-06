package com.goodu.worldholidaydates.holidaydata;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.goodu.worldholidaydates.holidayparser.HolidayParser;
import com.goodu.worldholidaydates.holidayparser.ParseException;
import com.goodu.worldholidaydates.holidayparser.Rule;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.internal.LinkedTreeMap;

public class CountryDeserializer implements JsonDeserializer<Country> {

    public CountryDeserializer() {
        // empty
    }

    @Override
    public Country deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        
        Country c = new Country();

        c.setNames(context.deserialize(jsonObject.get("names"), Map.class));
        c.setName(context.deserialize(jsonObject.get("name"), String.class));
        c.setDayoff(context.deserialize(jsonObject.get("dayoff"), String.class));
        c.setLangs(context.deserialize(jsonObject.get("langs"), String[].class));
        c.setZones(context.deserialize(jsonObject.get("zones"), String[].class));
        setReferenceDays(c, jsonObject.get("_days"), context);
        setDays(c, jsonObject.get("days"), context);
        setStates(c, jsonObject.get("states"), context);
        setRegions(c, jsonObject.get("regions"), context);

        return c;
    }

    private void setReferenceDays(Country c, JsonElement refDays, JsonDeserializationContext context) {
        String[] result;
        if (refDays == null || !refDays.isJsonNull()) {
            result = null;
        } else if (refDays.isJsonPrimitive()) {
            result = new String[]{ refDays.getAsString() };
        } else {
            result = context.deserialize(refDays, String[].class);
        }
        c.setReferenceDays(result);
    }

    void setDays(Country c, JsonElement days, JsonDeserializationContext context) {
        List<Holiday> result = new ArrayList<>();
        if (days == null || days.isJsonNull()) {
            result = null;
        } else {
            JsonObject daysArr = days.getAsJsonObject();
            for (Map.Entry<String, JsonElement> entry : daysArr.entrySet()) {
                JsonElement value = entry.getValue();
                if (!value.isJsonPrimitive()) {
                    Rule rule = null;
                    try {
                        // parse and get rule
                        String originalRule = entry.getKey();
                        HolidayParser parser = new HolidayParser(new ByteArrayInputStream(originalRule.getBytes()));
                        rule = parser.parse();
                        rule.setOriginalRule(originalRule);
                    } catch (ParseException e) {
                        throw new HolidayInitializationException(e.getMessage(), e);
                    }
                    Holiday h = context.deserialize(value, Holiday.class);
                    h.setRule(rule);
                    result.add(h);
                }
            }
        }
        c.setDays(result);
    }

    private void setStates(Country c, JsonElement states, JsonDeserializationContext context) {
        Map <String, Country> result = new LinkedTreeMap<>();
        if (states == null || states.isJsonNull()) {
            result = null;
        } else {
            JsonObject statesArr = states.getAsJsonObject();
            for (Map.Entry<String, JsonElement> entry : statesArr.entrySet()) {
                JsonElement value = entry.getValue();
                Country state = context.deserialize(value, Country.class);
                result.put(entry.getKey(), state);
            }
        }
        c.setStates(result);
    }

    private void setRegions(Country c, JsonElement regions, JsonDeserializationContext context) {
        Map <String, Country> result = new LinkedTreeMap<>();
        if (regions == null || regions.isJsonNull()) {
            result = null;
        } else {
            JsonObject regionsArr = regions.getAsJsonObject();
            for (Map.Entry<String, JsonElement> entry : regionsArr.entrySet()) {
                JsonElement value = entry.getValue();
                Country region = context.deserialize(value, Country.class);
                result.put(entry.getKey(), region);
            }
        }
        c.setRegions(result);
    }
}
