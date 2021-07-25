package worldholidaydates.holidaydata;

import java.lang.reflect.Type;
import java.util.Map;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.internal.LinkedTreeMap;

public class HolidayDeserializer implements JsonDeserializer<Holiday> {

    @Override
    public Holiday deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        Holiday h = new Holiday();

        setName(h, jsonObject.get("name"), context);
        h.setReferenceName(context.deserialize(jsonObject.get("_name"), String.class));
        h.setType(context.deserialize(jsonObject.get("type"), String.class));
        h.setNote(context.deserialize(jsonObject.get("note"), String.class));
        h.setSubstitute(context.deserialize(jsonObject.get("substitute"), Boolean.class));
        h.setDisable(context.deserialize(jsonObject.get("disable"), String[].class));
        h.setEnable(context.deserialize(jsonObject.get("enable"), String[].class));

        return h;
    }

    private void setName(Holiday h, JsonElement name, JsonDeserializationContext context) {
        Map<String, String> result = new LinkedTreeMap<>();
        if (name == null || name.isJsonNull()) {
            result = null;
        } else if (name.isJsonPrimitive()) {
            // TODO: handle places where name is not present
            result.put("xx", context.deserialize(name, String.class));
        } else {
            result = context.deserialize(name, Map.class);
        }
        h.setName(result);
    }
}
