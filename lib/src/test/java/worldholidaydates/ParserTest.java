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

public class ParserTest {
    @Test
    public void gregorianDateTest() {
        String input = "2021-12-20";
        HolidayParser parser = new HolidayParser(new ByteArrayInputStream(input.getBytes()));
        Rule rule;
        try {
            rule = parser.parse();
            LocalDateTime actual = rule.calculate();
            System.out.println(actual);
            assertEquals(LocalDateTime.parse("2021-12-20T00:00"), actual);
        } catch (ParseException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }
}
