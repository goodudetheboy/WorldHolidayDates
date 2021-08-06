package com.goodu.worldholidaydates.holidayparser;

import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * A class for calculating the date and time of equinox events, on which
 * a holiday can be based.
 */
public class EquinoxDate extends AstronomicalDate {

    public EquinoxDate() {
        // empty
    }

    public EquinoxDate(int year, int month, ZoneId timezone) {
        setMonth(month);
        setYear(year);
        setTimezone(timezone);
    }

    public EquinoxDate(int year, int month) {
        this(year, month, DEFAULT_ZONE);
    }

    @Override
    public boolean isAcceptedMonth(int month) {
        return month == 3 || month == 9;
    }

    @Override
    public String toNamedString() {
        StringBuilder b = new StringBuilder();
        b.append(namedMonth.getName()).append(" ")
         .append("equinox").append(", ")
         .append(year);
        return b.toString();
    }
    
    @Override
    protected ZonedDateTime calculateAstronomicalDate(int defaultYear) {
        int yearToUse = (year != UNDEFINED_NUM) ? year : defaultYear;
        return calculateEquinoxDate(month, yearToUse);
    }

    /**
     * Calculates the date of either March or September Equinox in input year,
     * with timezone set to {@link #DEFAULT_TIMEZONE}. The output can deviate
     * from actual dates by up to one day.
     * <p>
     * Part of this code is adapted from
     * <a href="https://github.com/sualeh/sunposition">sunposition</a>.
     * 
     * @param month 3 for March, 9 for September
     * @param year 
     * @return either March or September Equinox in input year
     */
    public static ZonedDateTime calculateEquinoxDate(int month, int year) {
        double m = ((double) year - 2000) / 1000;
        final double m2 = m * m;
        final double m3 = m2 * m;
        final double m4 = m3 * m;

        switch (month) {
            case 3:
                // Vernal Equinox
                double vEqui = 2451623.80984 + 365242.37404 * m + 0.05169 * m2
                            - 0.00411 * m3 - 0.00057 * m4;
                return jdeToDate(vEqui);
            case 9:
                // Autumnal Equinox
                double aEqui = 2451810.21715 + 365242.01767 * m - 0.11575 * m2
                            + 0.00337 * m3 + 0.00078 * m4;
                return jdeToDate(aEqui);
            default:
                throw new IllegalArgumentException("Equinox dates must be in March or September, not: " + month);
        }
    }
}
