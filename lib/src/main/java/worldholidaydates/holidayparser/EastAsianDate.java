package worldholidaydates.holidayparser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;

import javax.annotation.Nullable;

import net.time4j.PlainDate;
import net.time4j.calendar.ChineseCalendar;
import net.time4j.calendar.EastAsianCalendar;
import net.time4j.calendar.EastAsianMonth;
import net.time4j.calendar.EastAsianYear;
import net.time4j.calendar.KoreanCalendar;
import net.time4j.calendar.VietnameseCalendar;

public class EastAsianDate implements Date {
    public enum CalendarType {
        CHINESE("Chinese"),
        KOREAN("Korean"),
        VIETNAMESE("Vietnamese");

        private final String name;

        CalendarType(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public static CalendarType fromName(String name) {
            for (CalendarType h : CalendarType.values()) {
                if (h.getName().equals(name)) {
                    return h;
                }
            }
            return null;
        }
    }

    public static final int DEFAULT_EAST_ASIAN_YEAR_OF_CYCLE = 38;
    public static final int DEFAULT_EAST_ASIAN_CYCLE = 78;
    // to gregorian year: 2021

    private     CalendarType calType        = null;

    // for normal year, month, day
    private     int         cycle           = DEFAULT_EAST_ASIAN_CYCLE;
    private     int         yearOfCycle     = DEFAULT_EAST_ASIAN_YEAR_OF_CYCLE;
    private     int         month           = Date.UNDEFINED_NUM; // chinese
    private     boolean     isLeapMonth     = false;
    private     int         day             = Date.UNDEFINED_NUM; // chinese, day of lunar month
    
    // for solar term
    private     int         solarTermTh     = Date.UNDEFINED_NUM; // from 1 to 24
    private     int         solarTermDay    = Date.UNDEFINED_NUM; // from 1 to 15

    public EastAsianDate() {
        // empty
    }

    public EastAsianDate(int cycle, int cycleYear, int month, int day, CalendarType calType) {
        this(cycle, cycleYear, month, false, day, calType);
    }

    public EastAsianDate(int cycle, int yearOfCycle, int month, boolean isLeapMonth, int day, CalendarType calType) {
        this.cycle = cycle;
        this.yearOfCycle = yearOfCycle;
        this.month = month;
        this.day = day;
        this.isLeapMonth = isLeapMonth;
        this.calType = calType;
    }


    public EastAsianDate(int month, boolean isLeapMonth, int day, CalendarType calType) {
        this.month = month;
        this.isLeapMonth = isLeapMonth;
        this.day = day;
        this.calType = calType;
    }

    public EastAsianDate(int solarTermTh, int solarTermDay, CalendarType calType) {
        this.solarTermTh = solarTermTh;
        this.solarTermDay = solarTermDay;
        this.calType = calType;
    }

    public CalendarType getCalendarType() {
        return calType;
    }

    public int getCycle() {
        return cycle;
    }

    public int getYearOfCycle() {
        return yearOfCycle;
    }

    public int getMonth() {
        return month;
    }
    
    public int getSolarTermTh() {
        return solarTermTh;
    }

    public int getSolarTermDay() {
        return solarTermDay;
    }

    public int getGregorianYear() {
        return toGregorianYear(cycle, yearOfCycle);
    }

    public boolean isLeapMonth() {
        return isLeapMonth;
    }

    public void setCalendarType(CalendarType calType) {
        this.calType = calType;
    }

    public void setCycle(int cycle) {
        this.cycle = cycle;
    }

    public void setYearOfCycle(int yearOfCycle) {
        this.yearOfCycle = yearOfCycle;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setLeapMonth(boolean isLeapMonth) {
        this.isLeapMonth = isLeapMonth;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setSolarTermTh(int solarTermTh) {
        this.solarTermTh = solarTermTh;
    }

    public void setSolarTermDay(int solarTermDay) {
        this.solarTermDay = solarTermDay;
    }

    @Override
    public LocalDateTime calculate() {
        return calculateDate().atStartOfDay();
    }

    @Override @Nullable
    public LocalDate calculateDate() {
        if (month != Date.UNDEFINED_NUM) {
            // normal year, month, day
            // conversion
            EastAsianMonth conMonth = (!isLeapMonth())
                                    ? EastAsianMonth.valueOf(month)
                                    : EastAsianMonth.valueOf(month).withLeap();
            EastAsianYear conYear = EastAsianYear.forGregorian(toGregorianYear(cycle, yearOfCycle));

            // calculation based on different calendar type
            EastAsianCalendar date = null;
            switch(calType) {
                case CHINESE:
                    date = ChineseCalendar.of(conYear, conMonth, day);
                    break;
                case KOREAN:
                    date = KoreanCalendar.of(conYear, conMonth, day);
                    break;
                case VIETNAMESE:
                    date = VietnameseCalendar.of(conYear, conMonth, day);
                    break;
            }
            if (date != null) {
                PlainDate pdate = date.transform(PlainDate.class);
                return pdate.toTemporalAccessor();
            } else {
                return null;
            }
        } else {
            // solar term calculation
            return getDateFromSolarTerm(solarTermTh, solarTermDay, toGregorianYear(cycle, yearOfCycle));
        }
    }

    /**
     * A very rough estimation of Gregorian Year based on the cycle and year cycle.
     * Currently the cycle is 78, and the cycle starts in 1984, so the calculation
     * is as follows:
     * <p>
     * {@code Gregorian Year = (1984 - 78*60 - 1) + cycle*60 + yearOfCycle}
     * <p>
     * with {@code (1984 - 78*60 - 1) = -2697}
     * 
     * @param cycle the cycle
     * @param yearOfCycle year of the cycle, from 1 to 60
     * @return a rough estimation of the Gregorian year
     */
    public static int toGregorianYear(int cycle, int yearOfCycle) {
        if (yearOfCycle < 1 || yearOfCycle > 60) {
            throw new IllegalArgumentException("Year of cycle out of range: " + yearOfCycle);
        }
        return -2697 + (cycle * 60) + yearOfCycle;
    }

    /**
     * Get a rough estimation of a date in year based on the count of solar term
     * and day in that solar term. Based on the information from
     * <a href="https://en.wikipedia.org/wiki/Solar_term#List_of_solar_terms">List of solar terms</a>.
     * <p>
     * As per Wikipedia, the resulting date "can vary within a ±1 day range."
     * 
     * @param solarTermTh the count of solar term from 1 to 24
     * @param solarTermDay the day in the solar term from 1 to 15
     * @param year the year
     * @return a rough estimation of a date in year based on the count of solar term
     * @see https://en.wikipedia.org/wiki/Solar_term
     */
    public static LocalDate getDateFromSolarTerm(int solarTermTh, int solarTermDay, int year) {
        if (solarTermTh < 1 || solarTermTh > 24) {
            throw new IllegalArgumentException("Solar term out of range: " + solarTermTh);
        }
        if (solarTermDay < 1 || solarTermDay > 15) {
            throw new IllegalArgumentException("Solar term day out of range: " + solarTermDay);
        }

        // Start date of solar term taken from https://en.wikipedia.org/wiki/Solar_term#List_of_solar_terms
        int[][] solarTermStartDate =  { { 2,  4}, { 2, 19}, { 3,  6}, { 3, 21}, { 4,  5}, { 4, 20},
                                        { 5,  6}, { 5, 21}, { 6,  6}, { 6, 21}, { 7,  7}, { 7, 23},
                                        { 8,  8}, { 8, 23}, { 9,  8}, { 9, 23}, {10,  8}, {10, 23},
                                        {11,  7}, {11, 22}, {12,  7}, {12, 22}, { 1,  6}, { 1, 20} };
        
        // process the solar term and its day
        int[] solarTermDate = solarTermStartDate[solarTermTh - 1];
        int month = solarTermDate[0];
        int day = solarTermDate[1] + solarTermDay - 1;
        int maxDay = YearMonth.of(year, month).lengthOfMonth();

        // process case spills over into next month or year
        if (day > maxDay) {
            day = day % maxDay;
            if (month == 12) {
                month = 1;
                year += 1;
            } else {
                month += 1;
            }
        }

        return LocalDate.of(year, month, day);
    }
}
