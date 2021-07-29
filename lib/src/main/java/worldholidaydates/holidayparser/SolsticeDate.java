package worldholidaydates.holidayparser;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import worldholidaydates.holidayparser.GregorianDate.GregorianMonth;

public class SolsticeDate extends Date {

    public SolsticeDate() {
        setYear(GregorianDate.DEFAULT_GREGORIAN_YEAR);
    }

    public SolsticeDate(int year, int month, ZoneId timezone) {
        setMonth(month);
        setYear(year);
        setTimezone(timezone);
    }

    public SolsticeDate(int year, int month) {
        this(year, month, DEFAULT_ZONE);
    }

    @Override
    public void setMonth(int month) {
        if (month == 6 || month == 12) {
            super.setMonth(month);
        } else {
            throw new IllegalArgumentException("Solstice dates must be in June or December, not " + month);
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
    public LocalDate calculateRawDate() {
        ZonedDateTime defaultResult = calculateEquinoxDate(month, year);
        ZonedDateTime shiftedResult = defaultResult.withZoneSameInstant(timezone);
        return shiftedResult.toLocalDate();
    }

    @Override
    public String toNamedString() {
        StringBuilder b = new StringBuilder();
        b.append(namedMonth.getName()).append(" ")
         .append("solstice").append(", ")
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
            case 6:
                // Summer Solstice
                double sSols = 2451716.56767 + 365241.62603 * m + 0.00325 * m2 
                            + 0.00888 * m3 - 0.00030 * m4;
                return jdeToDate(sSols);
            case 12:
                // Winter Solstice
                double wSols = 2451900.05952 + 365242.74049 * m - 0.06223 * m2 
                        - 0.00823 * m3 + 0.00032 * m4;
                return jdeToDate(wSols);
            default:
                throw new IllegalArgumentException("Solstice dates must be in June or December, not: " + month);
        }
    }
}
