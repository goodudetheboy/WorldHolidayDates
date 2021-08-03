package worldholidaydates.holidaydata;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class DataTest {
    @Test
    public void dataJsonParseTest() {
        HolidayData data = HolidayData.initializeData();
        assertEquals(168, data.getHolidays().size());
    }
}
