package worldholidaydates;



import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.Test;

import worldholidaydates.holidayparser.Rule;
import worldholidaydates.holidayparser.Date;
import worldholidaydates.holidayparser.HolidayParser;
import worldholidaydates.holidayparser.ParseException;

public class UnitTest {

    public static void testParser(String input, LocalDateTime expected) {
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

    public static void testParserEnd(String input, LocalDateTime expected) {
        HolidayParser parser = new HolidayParser(new ByteArrayInputStream(input.getBytes()));
        try {
            Rule rule = parser.parse();
            LocalDateTime actual = rule.calculateEnd();
            assertEquals(expected, actual);
        } catch (ParseException e) {
            e.printStackTrace();
            fail("Parse fail at \"" + input + "\" - " + e.getMessage());
        }
    }


    public static void testFailParser(String input, String expectedMessage) throws ParseException {
        try {
            HolidayParser parser = new HolidayParser(new ByteArrayInputStream(input.getBytes()));
            parser.parse();
            fail("Should have failed");
        } catch (ParseException e) {
            // expected
            System.out.println(e.getMessage());
            assertEquals(expectedMessage, e.getMessage().substring(0, 50));
        }
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
        testParserDate("2015-07-24", LocalDate.parse("2015-07-24"));
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
    public void eastAsianSolarTermFailTest() throws ParseException {
        testFailParser("chinese 25-01 solarterm", "Encountered \" <NUMBER> \"25 \"\" at line 1, column 9.");
    }

    @Test
    public void bengaliRevisedTest() {
        testParserDate("bengali-revised 11-9", LocalDate.parse("2022-02-23"));
        testParserDate("bengali-revised 1428-11-9", LocalDate.parse("2022-02-23"));
        testParserDate("bengali-revised 4-31", LocalDate.parse("2021-08-16"));
    }

    @Test
    public void equinoxTest() {
        testParser("5 days before september equinox", LocalDateTime.parse("2021-09-17T19:11"));
        testParser("march equinox", LocalDateTime.parse("2021-03-20T09:35"));
        testParser("march equinox in -12:00", LocalDateTime.parse("2021-03-19T21:35"));
        testParser("march equinox in Asia/Tokyo", LocalDateTime.parse("2021-03-20T18:35"));
    }

    @Test
    public void solsticeTest() {
        testParser("december solstice", LocalDateTime.parse("2021-12-21T15:46"));
        testParser("3rd Sunday after june solstice", LocalDateTime.parse("2021-07-11T03:24"));
        testParser("3rd Sunday after june solstice in -05:00", LocalDateTime.parse("2021-07-11T22:24"));
    }

    @Test
    public void differentStartTimeTest() {
        testParser("12-31 14:00", LocalDateTime.parse("2021-12-31T14:00"));
        testParser("5 days before september equinox 15:00", LocalDateTime.parse("2021-09-17T15:00"));
        testParser("december solstice 16:00", LocalDateTime.parse("2021-12-21T16:00"));
        testParser("bengali-revised 1428-11-9 17:00", LocalDateTime.parse("2022-02-23T17:00"));
        testParser("chinese 78-38-5-01 solarterm 18:00", LocalDateTime.parse("2021-04-05T18:00"));
        testParser("27 Shvat 19:00", LocalDateTime.parse("2021-02-09T19:00"));
        testParser("17 Dhu al-Hijjah 20:00", LocalDateTime.parse("2021-07-27T20:00"));
        testParser("orthodox 21:00", LocalDateTime.parse("2021-05-02T21:00"));
        testParser("january 22:00", LocalDateTime.parse("2021-01-01T22:00"));
        testParser("2015-07-24 23:00", LocalDateTime.parse("2015-07-24T23:00"));
        testParser("07-24 00:00", LocalDateTime.parse("2021-07-24T00:00"));
    }

    @Test
    public void differentRangeTest() {
        testParser("12-31 14:00 P0DT0H0M", LocalDateTime.parse("2021-12-31T14:00"));
        testParserEnd("12-31 14:00 P0DT0H0M", LocalDateTime.parse("2021-12-31T14:00"));
        testParser("12-31 14:00 PT5H", LocalDateTime.parse("2021-12-31T14:00"));
        testParserEnd("12-31 14:00 PT5H", LocalDateTime.parse("2021-12-31T19:00"));
        testParser("17 Dhu al-Hijjah PT5M", LocalDateTime.parse("2021-07-27T00:00"));
        testParserEnd("17 Dhu al-Hijjah PT5M", LocalDateTime.parse("2021-07-27T00:05"));
        testParser("easter P1DT12H", LocalDateTime.parse("2021-04-04T00:00"));
        testParserEnd("easter P1DT12H", LocalDateTime.parse("2021-04-05T12:00"));
        testParser("1 Shawwal P3D", LocalDateTime.parse("2021-05-13T00:00"));testParserEnd("1 Shawwal P3D", LocalDateTime.parse("2021-05-16T00:00"));
    }

    @Test
    public void startTimeChangesPerWeekDayTest() {
        testParser("2021-08-01 14:00 if Sunday then 00:00", LocalDateTime.parse("2021-08-01T00:00"));
        testParser("2021-08-02 14:00 if Sunday then 00:00", LocalDateTime.parse("2021-08-02T14:00"));
        testParser("2021-07-31 14:00 if Saturday,sunday then 00:00", LocalDateTime.parse("2021-07-31T00:00"));
        testParser("2021-08-02 14:00 if Saturday,sunday then 00:00 if monday,Tuesday then 05:00", LocalDateTime.parse("2021-08-02T05:00"));
        testParser("2021-08-01 14:00 if Saturday,sunday then 00:00 if monday,Tuesday then 05:00", LocalDateTime.parse("2021-08-01T00:00"));
        testParser("2021-08-03 14:00 if Saturday,sunday then 00:00 if monday,Tuesday,wednesday then 05:00", LocalDateTime.parse("2021-08-03T05:00"));
    }

    @Test
    public void differentWeekdayTest() {
        testParser("1st Monday after 08-01", LocalDateTime.parse("2021-08-02T00:00"));
        testParser("Monday after 08-01", LocalDateTime.parse("2021-08-02T00:00"));
        testParser("Saturday before 08-01", LocalDateTime.parse("2021-07-31T00:00"));
        testParser("2nd saturday before 08-01", LocalDateTime.parse("2021-07-24T00:00"));
    }

    @Test
    public void nthWeekdayInMonthTest() {
        testParser("1st Monday in July", LocalDateTime.parse("2021-07-05T00:00"));
        testParser("2nd Sunday in August", LocalDateTime.parse("2021-08-08T00:00"));
        testParser("2nd Sunday before August", LocalDateTime.parse("2021-07-18T00:00"));      
        testParser("3rd Monday before August", LocalDateTime.parse("2021-07-12T00:00"));
    }
}
