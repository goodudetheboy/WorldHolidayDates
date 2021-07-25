package worldholidaydates.holidaydata;

import java.lang.reflect.Type;
import java.util.Map;

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
        set_days(c, jsonObject.get("_days"), context);
        setDays(c, jsonObject.get("days"), context);
        c.setStates(context.deserialize(jsonObject.get("states"), Map.class));
        c.setRegions(context.deserialize(jsonObject.get("regions"), Map.class));

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
        Map<String, Object> result = new LinkedTreeMap<>();
        if (days == null || days.isJsonNull()) {
            result = null;
        } else {
            JsonObject daysArr = days.getAsJsonObject();
            for (Map.Entry<String, JsonElement> entry : daysArr.entrySet()) {
                JsonElement value = entry.getValue();
                if (value.isJsonPrimitive()) {
                    // TODO: add error handling here
                    result.put(entry.getKey(), value.getAsBoolean());
                } else {
                    Holiday h = context.deserialize(value, Holiday.class);
                    result.put(entry.getKey(), h);
                }
            }
        }
        c.setDays(result);
    }
}
