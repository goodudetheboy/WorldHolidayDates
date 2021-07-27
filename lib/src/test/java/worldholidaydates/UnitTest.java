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

    public void testParser(String input, LocalDateTime expected) {
        HolidayParser parser = new HolidayParser(new ByteArrayInputStream(input.getBytes()));
        try {
            Rule rule = parser.parse();
            LocalDateTime actual = rule.calculate();
            assertEquals(expected, actual);
        } catch (ParseException e) {
            e.printStackTrace();
            fail("Parse fail at \"" + input + "\" - " + e.getMessage());
        }
    }

    @Test
    public void gregorianDateTest() {
        testParser("2021-05-01", LocalDateTime.parse("2021-05-01T00:00"));
    }

    @Test
    public void startOfMonthTest() {
        testParser("january", LocalDateTime.parse("2021-01-01T00:00"));
        testParser("march", LocalDateTime.parse("2021-03-01T00:00"));
    }


    @Test
    public void easterOrthodoxTest() {
        testParser("easter", LocalDateTime.parse("2021-04-04T00:00"));
        testParser("orthodox", LocalDateTime.parse("2021-05-02T00:00"));
        testParser("easter -2", LocalDateTime.parse("2021-04-02T00:00"));
        testParser("orthodox 3", LocalDateTime.parse("2021-05-05T00:00"));
        testParser("easter +49", LocalDateTime.parse("2021-05-23T00:00"));
        testParser("orthodox -6", LocalDateTime.parse("2021-04-26T00:00"));
    }
}
