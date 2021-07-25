package worldholidaydates;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

public class DataTest {
    @Test
    public void dataJsonParseTest() throws IOException {
        HolidayData data = HolidayData.initializeData();
        assertEquals(168, data.getHolidays().size());
    }
}
