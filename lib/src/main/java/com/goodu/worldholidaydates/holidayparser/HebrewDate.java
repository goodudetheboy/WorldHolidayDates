package com.goodu.worldholidaydates.holidayparser;

import java.time.LocalDate;

import net.time4j.PlainDate;
import net.time4j.calendar.HebrewCalendar;

/**
 * A class for calculating the date of Hebrew dates
 */
public class HebrewDate extends Date {
    public enum HebrewMonth implements NamedMonth {
        NISAN(1, "Nisan"),
        IYYAR(2, "Iyyar"),
        SIVAN(3, "Sivan"),
        TAMUZ(4, "Tamuz"),
        AV(5, "Av"),
        ELUL(6, "Elul"),
        TISHREI(7, "Tishrei"),
        CHESHVAN(8, "Cheshvan"),
        KISLEV(9, "Kislev"),
        TEVET(10, "Tevet"),
        SHVAT(11, "Shvat"),
        ADAR(12, "Adar");

        private final int value;
        private final String name;

        HebrewMonth(int value, String name) {
            this.value = value;
            this.name = name;
        }

        public int getValue() {
            return value;
        }

        public String getName() {
            return name;
        }

        public net.time4j.calendar.HebrewMonth toTime4jHebrewMonth() {
            switch(this) {
                case NISAN:
                    return net.time4j.calendar.HebrewMonth.NISAN;
                case IYYAR:
                    return net.time4j.calendar.HebrewMonth.IYAR;
                case SIVAN:
                    return net.time4j.calendar.HebrewMonth.SIVAN;
                case TAMUZ:
                    return net.time4j.calendar.HebrewMonth.TAMUZ;
                case AV:
                    return net.time4j.calendar.HebrewMonth.AV;
                case ELUL:
                    return net.time4j.calendar.HebrewMonth.ELUL;
                case TISHREI:
                    return net.time4j.calendar.HebrewMonth.TISHRI;
                case CHESHVAN:
                    return net.time4j.calendar.HebrewMonth.HESHVAN;
                case KISLEV:
                    return net.time4j.calendar.HebrewMonth.KISLEV;
                case TEVET:
                    return net.time4j.calendar.HebrewMonth.TEVET;
                case SHVAT:
                    return net.time4j.calendar.HebrewMonth.SHEVAT;
                case ADAR:
                    return net.time4j.calendar.HebrewMonth.ADAR_I;
                default:
                    // It is not possible to reach this point
                    throw new IllegalStateException("Unknown HebrewMonth: " + this);
            }
        }

        public static HebrewMonth fromValue(int month) {
            for (HebrewMonth h : HebrewMonth.values()) {
                if (h.getValue() == month) {
                    return h;
                }
            }
            return null;
        }

        public static HebrewMonth fromName(String name) {
            for (HebrewMonth h : HebrewMonth.values()) {
                if (h.getName().equals(name)) {
                    return h;
                }
            }
            return null;
        }

        @Override
        public String toString() {
            return name;
        }
    }
    public static final int DEFAULT_HEBREW_YEAR = 5781; // Gregorian Year = 2021-2022

    public HebrewDate() {
        // empty
    }

    public HebrewDate(int year, int month, int dayOfMonth, int time) {
        super(year, month, dayOfMonth, time);
    }

    public HebrewDate(int year, int month, int dayOfMonth) {
        this(year, month, dayOfMonth, 0);
    }

    public HebrewDate(int month, int dayOfMonth) {
        super(month, dayOfMonth);
    }

    @Override
    public HebrewMonth getNamedMonth() {
        return (HebrewMonth) namedMonth;
    }

    @Override
    public void setMonth(int month) {
        super.setMonth(month);
        super.setNamedMonth(HebrewMonth.fromValue(month));
    }

    @Override
    public void setNamedMonth(NamedMonth namedMonth) {
        if (namedMonth instanceof HebrewMonth) {
            super.setMonth(((HebrewMonth) namedMonth).getValue());
        } else {
            throw new IllegalArgumentException("NamedMonth must be a HebrewMonth");
        }
    }

    /**
     * Calculates the raw date stored in this {@link HebrewDate} with the 
     * Hebrew calendar, then converted to the Gregorian calendar.
     * 
     * <p>
     * If the {@link Date} already have a year, it will be used. Otherwise,the
     * default year will be used.
     * 
     * @param defaultYear default Gregorian year
     * @return a {@link LocalDate} object representing the raw Hebrew date 
     *      converted to Gregorian calendar
     */
    @Override
    public LocalDate calculateDate(int defaultYear) {
        int yearToUse = (year != UNDEFINED_NUM) ? year : gregorianYearToHebrewYear(defaultYear);
        HebrewCalendar date = HebrewCalendar.of(yearToUse, ((HebrewMonth) namedMonth).toTime4jHebrewMonth(), dayOfMonth);
        PlainDate pdate = date.transform(PlainDate.class);
        return pdate.toTemporalAccessor();
    }

    /**
     * Convert a Gregorian year to Hebrew year. This calculation is very rough
     * and not precise, and is to be used within this class.
     * <p>
     * The number 3760 is taken from 2021 - 5781, since in the year 2021, the
     * Hebrew year is 5781.
     * 
     * TODO: fix this calculation
     * 
     * @param gregorianYear a Greogrian year
     * @return a rough estimation of Bengali year
     */
    public static int gregorianYearToHebrewYear(int gregorianYear) {
        return 3760 + gregorianYear;
    }
}
