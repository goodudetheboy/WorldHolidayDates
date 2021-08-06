package com.goodu.worldholidaydates.holidayparser;

import java.time.LocalDate;
import java.time.chrono.HijrahChronology;
import java.time.chrono.HijrahEra;

/**
 * A class for calculating the date of Hirja dates based on Hirja calendar
 */
public class HijraDate extends Date {
    public enum HijraMonth implements NamedMonth {
        MUHARRAM(1, "Muharram"),
        SAFAR(2, "Safar"),
        RABI_AL_AWWAL(3, "Rabi al-awwal"),
        RABI_AL_THANI(4, "Rabi al-thani"),
        JUMADA_AL_AWWAL(5, "Jumada al-awwal"),
        JUMADA_AL_THANI(6, "Jumada al-thani"),
        RAJAB(7, "Rajab"),
        SHABAN(8, "Shaban"),
        RAMADAN(9, "Ramadan"),
        SHAWWAL(10, "Shawwal"),
        DHU_AL_QIDAH(11, "Dhu al-qidah"),
        DHU_AL_HIJJAH(12, "Dhu al-hijjah");

        private final int value;
        private final String name;

        HijraMonth(int value, String name) {
            this.value = value;
            this.name = name;
        }

        @Override
        public int getValue() {
            return value;
        }

        @Override
        public String getName() {
            return name;
        }

        public static HijraMonth fromValue(int value) {
            for (HijraMonth h : HijraMonth.values()) {
                if (h.getValue() == value) {
                    return h;
                }
            }
            return null;
        }

        public static HijraMonth fromName(String name) {
            for (HijraMonth h : HijraMonth.values()) {
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

    public static final int DEFAULT_HIJRA_YEAR = 1442; // Gregorian Year = 2021-2022

    public HijraDate() {
        // empty
    }

    public HijraDate(int year, int month, int dayOfMonth, int time) {
        super(year, month, dayOfMonth, time);
    }

    public HijraDate(int year, int month, int dayOfMonth) {
        this(year, month, dayOfMonth, 0);
    }

    public HijraDate(int month, int dayOfMonth) {
        super(month, dayOfMonth);
    }

    @Override
    public void setMonth(int month) {
        super.setMonth(month);
        super.setNamedMonth(HijraMonth.fromValue(month));
    }

    @Override
    public void setNamedMonth(NamedMonth namedMonth) {
        if (namedMonth instanceof HijraMonth) {
            super.setMonth(((HijraMonth) namedMonth).getValue());
        } else {
            throw new IllegalArgumentException("NamedMonth must be a HijraMonth");
        }
    }

    @Override
    public HijraMonth getNamedMonth() {
        return (HijraMonth) namedMonth;
    }

    /**
     * Calculates the raw date stored in this {@link HijraDate} with the 
     * Hirja calendar, then convert to the Gregorian calendar.
     * 
     * @return a {@link LocalDate} object representing the raw Hebrew date 
     *      converted to Gregorian calendar
     */
    @Override
    public LocalDate calculateDate(int defaultYear) {
        int yearToUse = (year != UNDEFINED_NUM) ? year : greogrianYearToHijraYear(defaultYear);
        java.time.chrono.HijrahDate date =  HijrahChronology.INSTANCE.date(HijrahEra.AH, yearToUse, month, dayOfMonth);
        return LocalDate.from(date);
    }

    /**
     * Convert a Gregorian year to Hijra year. This calculation is very rough
     * and not precise, and is to be used within this class.
     * <p>
     * The number 579 is taken from 2021 - 1442, since in the year 2021, the
     * Hijra year is 1442.
     * 
     * TODO: fix this calculation
     * 
     * @param gregorianYear a Greogrian year
     * @return a rough estimation of Bengali year
     */
    public static int greogrianYearToHijraYear(int gregorianYear) {
        return gregorianYear - 579;
    }
}
