package io.github.goodudetheboy.worldholidaydates.holidayparser;

import static org.junit.Assert.assertEquals;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import io.github.goodudetheboy.worldholidaydates.Utils;
import io.github.goodudetheboy.worldholidaydates.holidaydata.Country;
import io.github.goodudetheboy.worldholidaydates.holidaydata.HolidayData;

public class DataCollector {
    public static void collectDates() throws IOException {
        String path = "./test-data/date.txt";
        FileWriter fWriter = new FileWriter(path);

        HolidayData data = HolidayData.initalizeRawData();
        int i = 0;
        for (Map.Entry<String, Country> entry : data.getHolidays().entrySet()) {
            writeHolidaysInCountry(entry.getValue(), fWriter);
            i++;
        }
        fWriter.close();
        assertEquals(168, i);
    }

    public static void writeHolidaysInCountry(Country country, FileWriter fWriter) throws IOException {
        Map<Rule, Object> holidays = country.getRawDays();
        if (holidays != null) {
            for (Map.Entry<Rule, Object> entry2 : holidays.entrySet()) {
                fWriter.write(entry2.getKey().getOriginalRule() + Utils.LINE_SEPARATOR);
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

    public static void main(String[] args) throws IOException {
        collectDates();
    }
}
