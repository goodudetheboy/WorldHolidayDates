package worldholidaydates.holidayparser;

import java.time.LocalDate;
import java.time.LocalDateTime;

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

    private     int         cycle       = DEFAULT_EAST_ASIAN_CYCLE;
    private     int         yearOfCycle = DEFAULT_EAST_ASIAN_YEAR_OF_CYCLE;
    private     int         month       = Date.UNDEFINED_NUM; // chinese
    private     boolean     isLeapMonth = false;
    private     int         day         = Date.UNDEFINED_NUM; // chinese, day of lunar month
    private     CalendarType calType    = null;
    
    private     int         solarTermTh = 0;

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

    public EastAsianDate(int month, int day, CalendarType calType) {
        this(month, false, day, calType);
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
    
    public int getGregorianYear() {
        return toGregorianYear(cycle, yearOfCycle);
    }

    public boolean isLeapMonth() {
        return isLeapMonth;
    }

    public CalendarType getCalendarType() {
        return calType;
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

    public void setCalendarType(CalendarType calType) {
        this.calType = calType;
    }

    @Override
    public LocalDateTime calculate() {
        return calculateDate().atStartOfDay();
    }

    @Override @Nullable
    public LocalDate calculateDate() {
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
}
