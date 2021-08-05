package worldholidaydates.holidaydata;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Type;
import java.util.Map;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.internal.LinkedTreeMap;

import worldholidaydates.holidayparser.HolidayParser;
import worldholidaydates.holidayparser.ParseException;
import worldholidaydates.holidayparser.Rule;

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
        set_days(c, jsonObject.get("_days"), context);
        setDays(c, jsonObject.get("days"), context);
        setStates(c, jsonObject.get("states"), context);
        setRegions(c, jsonObject.get("regions"), context);

        return c;
    }

    private void set_days(Country c, JsonElement _days, JsonDeserializationContext context) {
        String[] result;
        if (_days == null || !_days.isJsonNull()) {
            result = null;
        } else if (_days.isJsonPrimitive()) {
            result = new String[]{ _days.getAsString() };
        } else {
            result = context.deserialize(_days, String[].class);
        }
        c.setReferenceDays(result);
    }

    private void setDays(Country c, JsonElement days, JsonDeserializationContext context) {
        Map<Rule, Object> result = new LinkedTreeMap<>();
        if (days == null || days.isJsonNull()) {
            result = null;
        } else {
            JsonObject daysArr = days.getAsJsonObject();
            for (Map.Entry<String, JsonElement> entry : daysArr.entrySet()) {
                JsonElement value = entry.getValue();
                Rule rule = null;
                try {
                    // parse and get rule
                    String originalRule = entry.getKey();
                    HolidayParser parser = new HolidayParser(new ByteArrayInputStream(originalRule.getBytes()));
                    rule = parser.parse();
                    rule.setOriginalRule(originalRule);
                } catch (ParseException e) {
                    throw new RuntimeException(e.getMessage(), e);
                }

                // construct key and value
                if (value.isJsonPrimitive()) {
                    // TODO: add error handling here
                    result.put(rule, value.getAsBoolean());
                } else {
                    Holiday h = context.deserialize(value, Holiday.class);
                    result.put(rule, h);
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
