package worldholidaydates.holidayparser;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import worldholidaydates.Utils;
import worldholidaydates.holidaydata.Country;
import worldholidaydates.holidaydata.HolidayData;

public class DataCollector {
    public static void main(String[] args) throws IOException {
        String path = System.getProperty("user.dir") + "/lib/test-data/date.txt";
        FileWriter fileWriter = new FileWriter(path);

        HolidayData data = HolidayData.initializeData();
        for (Map.Entry<String, Country> entry : data.getHolidays().entrySet()) {
            Map<String, Object> holidays = entry.getValue().getDays();
            if (holidays != null) {
                for (Map.Entry<String, Object> entry2 : holidays.entrySet()) {
                    fileWriter.write(entry2.getKey() + Utils.LINE_SEPARATOR);
                }
            }
        }
        fileWriter.close();
    }
}
