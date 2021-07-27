package worldholidaydates;



import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.Test;

import worldholidaydates.holidayparser.Rule;
import worldholidaydates.holidayparser.HolidayParser;
import worldholidaydates.holidayparser.ParseException;

public class UnitTest {
    @Test
    public void gregorianDateTest() {
        String input = "2021-05-01";
        LocalDateTime expected = LocalDateTime.parse("2021-05-01T00:00");
        testParser(input, expected);
    }

    @Test
    public void startOfMonthTest() {
        String input = "January";
        LocalDateTime expected = LocalDateTime.parse("2021-01-01T00:00");
        testParser(input, expected);
    }

    public void testParser(String input, LocalDateTime expected) {
        HolidayParser parser = new HolidayParser(new ByteArrayInputStream(input.getBytes()));
        try {
            Rule rule = parser.parse();
            LocalDateTime actual = rule.calculate();
            assertEquals(expected, actual);
        } catch (ParseException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }
}
