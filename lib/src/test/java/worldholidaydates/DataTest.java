package worldholidaydates;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import worldholidaydates.holidaydata.HolidayData;

public class DataTest {
    @Test
    public void dataJsonParseTest() {
        HolidayData data = HolidayData.initializeData();
        assertEquals(168, data.getHolidays().size());
    }
}
