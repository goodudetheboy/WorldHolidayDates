package com.goodu.worldholidaydates.holidaydata;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Map;

import org.junit.Test;

public class DataTest {
    @Test
    public void dataJsonParseTest() {
        HolidayData data = HolidayData.initializeData();
        assertEquals(168, data.getHolidays().size());
    }

    @Test
    public void printHolidayTest() {
        HolidayData data = HolidayData.initializeData();
        Map<String, Country> holidays = data.getHolidays();
        BufferedWriter outputExpected = null;
        try {
            outputExpected = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("holiday-data/" + "all-holidays.txt"), StandardCharsets.UTF_8));
            for (Map.Entry<String, Country> entry : holidays.entrySet()) {
                Country c = entry.getValue();
                outputExpected.write(entry.getKey() + "\n");
                if (c.getDays() != null) {
                    for (LocalDate holiday : c.getHolidayDatesList(2021)) {
                        if (holiday != null) {
                            outputExpected.write(holiday.toString() + "\n");
                        }
                    }
                }
            }
            outputExpected.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            fail(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.getMessage());
        } finally {
            try {
                outputExpected.close();
            } catch (IOException e) {
                e.printStackTrace();
                fail(e.getMessage());
            }
        }
    }
}
