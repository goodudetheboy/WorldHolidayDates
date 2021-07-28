package worldholidaydates.holidayparser;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * A class for calculating the date of Bengali holidays based on 
 * Bengali revised calendar 
 */
public class BengaliDate implements Date {
    public static final int DEFAULT_BENGALI_YEAR = 1428;

    private int     year    = DEFAULT_BENGALI_YEAR;
    private int     month   = UNDEFINED_NUM;
    private int     day     = UNDEFINED_NUM;

    public BengaliDate() {
        // empty
    }

    public BengaliDate(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setDay(int day) {
        this.day = day;
    }

    @Override
    public LocalDateTime calculate() {
        return calculateDate().atStartOfDay();
    }

    @Override
    public LocalDate calculateDate() {
        return toGregorianDate(year, month, day);
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

    public static void main(String[] args) {
        System.out.println(toGregorianDate(1428, 1, 1));
    }
}
