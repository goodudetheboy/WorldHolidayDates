package worldholidaydates.holidayparser;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import worldholidaydates.Utils;
import worldholidaydates.holidaydata.Country;
import worldholidaydates.holidaydata.HolidayData;

public class DataCollector {
    public static void main(String[] args) throws IOException {
        String path = System.getProperty("user.dir") + "/test-data/date.txt";
        FileWriter fWriter = new FileWriter(path);

        HolidayData data = HolidayData.initializeData();
        for (Map.Entry<String, Country> entry : data.getHolidays().entrySet()) {
            writeHolidaysInCountry(entry.getValue(), fWriter);
        }
        fWriter.close();
    }

    public static void writeHolidaysInCountry(Country country, FileWriter fWriter) throws IOException {
        Map<String, Object> holidays = country.getDays();
        if (holidays != null) {
            for (Map.Entry<String, Object> entry2 : holidays.entrySet()) {
                fWriter.write(entry2.getKey() + Utils.LINE_SEPARATOR);
            }
        }
        Map<String, Country> states = country.getStates();
        if (states != null) {
            for (Map.Entry<String, Country> entry : states.entrySet()) {
                writeHolidaysInCountry(entry.getValue(), fWriter);
            }
        }
        Map<String, Country> regions = country.getRegions();
        if (regions != null) {
            for (Map.Entry<String, Country> entry : regions.entrySet()) {
                writeHolidaysInCountry(entry.getValue(), fWriter);
            }
        }
    }
}
