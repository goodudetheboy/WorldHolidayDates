package worldholidaydates.holidayparser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import worldholidaydates.holidayparser.GregorianDate.GregorianMonth;

public class EquinoxDate extends Date {

    public EquinoxDate() {
        setYear(GregorianDate.DEFAULT_GREGORIAN_YEAR);
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
    public void setMonth(int month) {
        if (month == 3 || month == 9) {
            super.setMonth(month);
        } else {
            throw new IllegalArgumentException("Equinox dates must be in March or September, not: " + month);
        }
    }

    @Override
    public void setNamedMonth(NamedMonth namedMonth) {
        if (namedMonth instanceof GregorianMonth) {
            setMonth(((GregorianMonth) namedMonth).getValue());
            super.setNamedMonth(namedMonth);
        } else {
            throw new IllegalArgumentException("NamedMonth must be a GregorianMonth");
        }
    }

    @Override
    public GregorianMonth getNamedMonth() {
        return (GregorianMonth) namedMonth;
    }

    @Override
    public LocalDateTime calculate() {
        LocalDateTime result = calculateRaw();
        if (offset != 0) {
            result = getOffsetDate(result, (isAfter) ? offset : -offset);
        }
        if (offsetWeekDay != 0) {
            result = getOffsetWeekDayDate(result, offsetWeekDay, offsetWeekDayNth, isAfter);
        }
        return result;
    }

    @Override
    public ZonedDateTime calculateWithTimeZone() {
        return calculate().atZone(timezone);
    }

    @Override
    public LocalDateTime calculateRaw() {
        ZonedDateTime defaultResult = calculateEquinoxDate(month, year);
        ZonedDateTime shiftedResult = defaultResult.withZoneSameInstant(timezone);
        return shiftedResult.toLocalDateTime();
    }

    @Override
    public LocalDate calculateRawDate() {
        ZonedDateTime defaultResult = calculateEquinoxDate(month, year);
        ZonedDateTime shiftedResult = defaultResult.withZoneSameInstant(timezone);
        return shiftedResult.toLocalDate();
    }

    @Override
    public String toNamedString() {
        StringBuilder b = new StringBuilder();
        b.append(namedMonth.getName()).append(" ")
         .append("equinox").append(", ")
         .append(year);
        return b.toString();
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
