package io.github.goodudetheboy.worldholidaydates.holidaydata;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.junit.Test;

public class UnitTest {
    public static HolidayData data = HolidayData.initializeData();

    @Test
    public void getStateTest() {
        Country us = data.getCountry("US");
        Country newYork = us.getStateByName("NY");
        assertNotNull(newYork);
        Country none = us.getStateByName("RU");
        assertNull(none);
    }

    @Test
    public void getRegionTest() {
        Country pe = data.getCountry("PE");
        Country cus = pe.getRegionByName("CUS");
        assertNotNull(cus);
        Country none = pe.getRegionByName("RU");
        assertNull(none);
    }

    @Test
    public void getSubRegionTest() {
        Country us = data.getCountry("US");
        Country newYork = us.getSubRegion("NY");
        assertNotNull(newYork);
        Country none = us.getRegionByName("RU");
        assertNull(none);
    }
}
