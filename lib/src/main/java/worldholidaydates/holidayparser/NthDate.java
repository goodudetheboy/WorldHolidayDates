package worldholidaydates.holidayparser;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

import worldholidaydates.holidayparser.GregorianDate.GregorianMonth;

/**
 * A class for calculating an nth weekday of a month, in Gregorian calendar.
 */
public class NthDate extends Date {

    int         weekday     = UNDEFINED_NUM;
    int         nth         = UNDEFINED_NUM;
    // true to find in month, false to find before
    boolean     isInMonth   = true;

    public NthDate() {
        // empty
    }

    public int getWeekday() {
        return weekday;
    }

    public int getNth() {
        return nth;
    }

    public boolean isInMonth() {
        return isInMonth;
    }

    public void setWeekday(int weekday) {
        this.weekday = weekday;
    }

    public void setNth(int nth) {
        this.nth = nth;
    }

    public void setInMonth(boolean isInMonth) {
        this.isInMonth = isInMonth;
    }

    @Override
    public GregorianMonth getNamedMonth() {
        return (GregorianMonth) namedMonth;
    }

    @Override
    public LocalDate calculateDate() {
        int monthToSearch = (isInMonth) ? month : month - 1;
        return getNthDateOfMonth(year, monthToSearch, weekday, nth);
    }
    
    /**
     * Returns the nth weekday of the month of the year.
     * 
     * @param year year
     * @param monthToSearch month
     * @param weekday day of the week (from 1-7)
     * @param nth nth of day of week
     * @return the nth weekday of the month of the year
     */
    public static LocalDate getNthDateOfMonth(int year, int monthToSearch, int weekday, int nth) {
        LocalDate d = LocalDate.of(year, monthToSearch, 1);
        return d.with(TemporalAdjusters.dayOfWeekInMonth(nth, DayOfWeek.of(weekday)));
    }
}
