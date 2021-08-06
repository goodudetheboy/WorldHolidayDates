package com.goodu.worldholidaydates.holidayparser;

import java.time.LocalDate;

/**
 * A class for calculating the date of Easter and Orthodox easter dates based
 * on the Gregorian calendar
 */
public class EasterDate extends Date {
    boolean isOrthodox      = false;
    int     offset          = 0;

    public EasterDate() {
        // empty
    }

    public EasterDate(int year) {
        this(year, false, 0);
    }
    
    public EasterDate(int year, boolean isOrthodox) {
        this(year, isOrthodox, 0);
    }

    public EasterDate(int year, boolean isOrthodox, int offset) {
        this.year = year;
        this.isOrthodox = isOrthodox;
        this.offset = offset;
    }

    public EasterDate(boolean isOrthodox, int offset) {
        this.isOrthodox = isOrthodox;
        this.offset = offset;
    }

    public boolean isOrthodox() {
        return isOrthodox;
    }

    public int getOffset() {
        return offset;
    }

    public void setOrthodox(boolean isOrthodox) {
        this.isOrthodox = isOrthodox;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    @Override
    public NamedMonth getNamedMonth() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Calculates the date of Easter of the year stored in this
     * {@link EasterDate} in the Gregorian calendar.
     * 
     * @return the date of Easter of the year stored in this {@link EasterDate}
     */
    @Override
    public LocalDate calculateDate(int defaultYear) {
        int yearToUse = (year != UNDEFINED_NUM) ? year : defaultYear;
        LocalDate result = (isOrthodox) ? getOrthodoxEasterDate(yearToUse) : getEasterDate(yearToUse);
        return getOffsetDate(result.atStartOfDay(), offset).toLocalDate();
    }

    /**
     * Returns the easter day of an input year.
     * <p>
     * This function uses the  "Meeus/Jones/Butcher" algorithm. For more
     * information check the link provided below.
     * 
     * @param year valid Gregorian year
     * @return the date of Easter of input year
     * @author Bernhard Seebass, from https://stackoverflow.com/a/55278990/10154717
     * @see "Meeus/Jones/Butcher" algorithm
     * https://en.wikipedia.org/wiki/Date_of_Easter#Anonymous_Gregorian_algorithm
     */
    public static LocalDate getEasterDate(int year) {
        int a = year % 19;
        int b = year / 100;
        int c = year % 100;
        int d = b / 4;
        int e = b % 4;
        int f = (b + 8) / 25;
        int g = (b - f + 1) / 3;
        int h = (19 * a + b - d - g + 15) % 30;
        int i = c / 4;
        int k = c % 4;
        int l = (32 + 2 * e + 2 * i - h - k) % 7;
        int m = (a + 11 * h + 22 * l) / 451;
        int easterMonth = (h + l - 7 * m + 114) / 31;
        int p = (h + l - 7 * m + 114) % 31;
        int easterDay = p + 1;
        return LocalDate.of(year, easterMonth, easterDay);
    }

    /**
     * Returns the Orthodox Easter day of an input year.
     * <p>
     * This code is adapted from https://manios.org/2012/08/14/java-calculate-orthodox-easter-date
     * 
     * @param year an input year
     * @author Christos Manios, from https://manios.org/
     * @return the date of Orthodox Easter of input year
     */
    public static LocalDate getOrthodoxEasterDate(int year) {
        LocalDate result;

        int r1 = year % 4;
        int r2 = year % 7;
        int r3 = year % 19;
        int r4 = (19 * r3 + 15) % 30;
        int r5 = (2 * r1 + 4 * r2 + 6 * r4 + 6) % 7;
        int days = r5 + r4 + 13;
    
        if (days > 39) {
            days = days - 39;
            result = LocalDate.of(year, 5, days);
        } else if (days > 9) {
            days = days - 9;
            result = LocalDate.of(year, 4, days);
        } else {
            days = days + 22;
            result = LocalDate.of(year, 3, days);
        }
        return result;
    }
}
