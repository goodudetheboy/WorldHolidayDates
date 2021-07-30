package worldholidaydates.holidayparser;

import java.time.LocalDate;

/**
 * A class for calculating the date of Bengali dates based on Bengali revised
 * calendar 
 */
public class BengaliDate extends Date {
    public static final int DEFAULT_BENGALI_YEAR = 1428;

    public BengaliDate() {
        setYear(DEFAULT_BENGALI_YEAR);
    }

    public BengaliDate(int year, int month, int dayOfMonth, int time) {
        super(year, month, dayOfMonth, time);
    }

    public BengaliDate(int year, int month, int dayOfMonth) {
        super(year, month, dayOfMonth, 0);
    }

    public BengaliDate(int month, int dayOfMonth) {
        super(DEFAULT_BENGALI_YEAR, month, dayOfMonth);
    }
    
    @Override
    public NamedMonth getNamedMonth() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Calculates the raw date stored in this {@link BengaliDate} with the 
     * Bengali calendar, then converted to the Gregorian calendar.
     * 
     * @return a {@link LocalDate} object representing the raw Bengali date 
     *      converted to Gregorian calendar
     */
    @Override
    public LocalDate calculateRawDate() {
        return toGregorianDate(year, month, dayOfMonth);
    }
    
    /**
     * Converts input bengali-revised year, month, and day to Gregorian date
     * based 
     * 
     * @param year Bengali year
     * @param month Bengali month
     * @param day Bengali day
     * @return a rough estimation of corresponding Gregorian date, may deviate
     *      one day from actual date
     */
    public static LocalDate toGregorianDate(int year, int month, int day) {
        // not supported yet
        if (year < 1425) {
            return null;
        }
        int revYear = 1425;
        LocalDate gDateMark = LocalDate.parse("2018-04-15");
        int dayToMove = (year - revYear)*365 + (year - revYear + 2)/4;

        if (month > 6) {
            dayToMove += 6*31;
            int remMonth = month - 6 - 1; // offset by one month
            if (remMonth > 4) {
                dayToMove -= 1; // in case of Falgun, the 29/30 day month
            }
            dayToMove += remMonth*30;                
        } else {
            dayToMove += (month - 1)*31;
        }

        dayToMove += day-1;
        return gDateMark.plusDays(dayToMove);
    }
}
