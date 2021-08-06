package worldholidaydates.holidaydata;

import java.io.ByteArrayInputStream;
import java.util.Map;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.internal.LinkedTreeMap;

import worldholidaydates.holidayparser.HolidayParser;
import worldholidaydates.holidayparser.ParseException;
import worldholidaydates.holidayparser.Rule;

public class CountryRawDeserializer extends CountryDeserializer {

    public CountryRawDeserializer() {
        // empty
    }

    @Override
    void setDays(Country c, JsonElement days, JsonDeserializationContext context) {
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
                    throw new HolidayInitializationException(e.getMessage(), e);
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
        c.setRawDays(result);
    }
}
