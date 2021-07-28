package worldholidaydates;



import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.HijrahDate;

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

    public void testFailParser(String input) throws ParseException {
        HolidayParser parser = new HolidayParser(new ByteArrayInputStream(input.getBytes()));
        Rule rule = parser.parse();
    }

    public void testParserDate(String input, LocalDate expected) {
        HolidayParser parser = new HolidayParser(new ByteArrayInputStream(input.getBytes()));
        try {
            Rule rule = parser.parse();
            LocalDate actual = rule.calculateDate();
            assertEquals(expected, actual);
        } catch (ParseException e) {
            e.printStackTrace();
            fail("Parse fail at \"" + input + "\" - " + e.getMessage());
        }
    }

    @Test
    public void gregorianDateTest() {
        testParserDate("2021-05-01", LocalDate.parse("2021-05-01"));
        testParserDate("05-01", LocalDate.parse("2021-05-01"));
    }

    @Test
    public void startOfMonthTest() {
        testParserDate("january", LocalDate.parse("2021-01-01"));
        testParserDate("March", LocalDate.parse("2021-03-01"));
    }


    @Test
    public void easterOrthodoxTest() {
        testParserDate("easter", LocalDate.parse("2021-04-04"));
        testParserDate("orthodox", LocalDate.parse("2021-05-02"));
        testParserDate("easter -2", LocalDate.parse("2021-04-02"));
        testParserDate("orthodox 3", LocalDate.parse("2021-05-05"));
        testParserDate("easter +49", LocalDate.parse("2021-05-23"));
        testParserDate("orthodox -6", LocalDate.parse("2021-04-26"));
    }

    @Test
    public void hijraTest() {
        testParserDate("29 Muharram", LocalDate.parse("2020-09-17"));
        testParserDate("30 Safar", LocalDate.parse("2020-10-17"));
        testParserDate("28 Rabi al-awwal", LocalDate.parse("2020-11-14"));
        testParserDate("27 Rabi al-thani", LocalDate.parse("2020-12-12"));
        testParserDate("26 Jumada al-awwal", LocalDate.parse("2021-01-10"));
        testParserDate("22 Jumada al-thani", LocalDate.parse("2021-02-04"));
        testParserDate("21 Rajab", LocalDate.parse("2021-03-05"));
        testParserDate("20 Shaban", LocalDate.parse("2021-04-02"));
        testParserDate("9 Ramadan", LocalDate.parse("2021-04-21"));
        testParserDate("10 Shawwal", LocalDate.parse("2021-05-22"));
        testParserDate("11 Dhu al-Qidah", LocalDate.parse("2021-06-21"));
        testParserDate("17 Dhu al-Hijjah", LocalDate.parse("2021-07-27"));
    }

    @Test
    public void hebrewTest() {
        testParserDate("1 Nisan", LocalDate.parse("2021-03-14"));
        testParserDate("2 Iyyar", LocalDate.parse("2021-04-14"));
        testParserDate("3 Sivan", LocalDate.parse("2021-05-14"));
        testParserDate("10 Tamuz", LocalDate.parse("2021-06-20"));
        testParserDate("11 Av", LocalDate.parse("2021-07-20"));
        testParserDate("12 Elul", LocalDate.parse("2021-08-20"));
        testParserDate("24 Tishrei", LocalDate.parse("2020-10-12"));
        testParserDate("25 Kislev", LocalDate.parse("2020-12-11"));
        testParserDate("26 Tevet", LocalDate.parse("2021-01-10"));
        testParserDate("27 Shvat", LocalDate.parse("2021-02-09"));
        testParserDate("28 Adar", LocalDate.parse("2021-03-12"));
    }

    @Test
    public void eastAsianTest() {
        testParserDate("chinese 6-0-18", LocalDate.parse("2021-07-27"));
        testParserDate("chinese 78-38-6-0-18", LocalDate.parse("2021-07-27"));
        testParserDate("korean 6-0-18", LocalDate.parse("2021-07-27"));
        testParserDate("korean 78-38-6-0-18", LocalDate.parse("2021-07-27"));
        testParserDate("vietnamese 6-0-18", LocalDate.parse("2021-07-27"));
        testParserDate("vietnamese 78-38-6-0-18", LocalDate.parse("2021-07-27"));
    }

    @Test
    public void eastAsianSolarTermTest() {
        testParserDate("chinese 5-01 solarterm", LocalDate.parse("2021-04-05"));
        testParserDate("chinese 78-38-5-01 solarterm", LocalDate.parse("2021-04-05"));
    }

    @Test
    public void eastAsianSolarTermFailTest() {
        try {
            testFailParser("chinese 25-01 solarterm");
            fail("Should have failed");
        } catch (ParseException e) {
            // expected
            System.out.println(e.getMessage());
            assertEquals("Encountered \" <NUMBER> \"25 \"\" at line 1, column 9.", e.getMessage().substring(0, 50));
        }
    }

    @Test
    public void bengaliRevisedTest() {
        testParserDate("bengali-revised 11-9", LocalDate.parse("2022-02-23"));
        testParserDate("bengali-revised 1428-11-9", LocalDate.parse("2022-02-23"));
        testParserDate("bengali-revised 4-31", LocalDate.parse("2021-08-16"));
    }
}
