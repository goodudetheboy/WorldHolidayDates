package worldholidaydates.holidayparser;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class GregorianDate implements Date{
    public enum GregorianMonth {
        JANUARY(1, "January"),
        FEBRUARY(2, "February"),
        MARCH(3, "March"),
        APRIL(4, "April"),
        MAY(5, "May"),
        JUNE(6, "June"),
        JULY(7, "July"),
        AUGUST(8, "August"),
        SEPTEMBER(9, "September"),
        OCTOBER(10, "October"),
        NOVEMBER(11, "November"),
        DECEMBER(12, "December");
        
        private int month;
        private String name;
        
        private GregorianMonth(int month, String name){
            this.month = month;
            this.name = name;
        }
        
        public int getMonth(){
            return month;
        }
        
        public String getName(){
            return name;
        }

        /**
         * Convert a month name to a month number.
         * 
         * @param name a month name
         * @return a corresponding month number from 1 to 12
         */
        public static int toMonth(String name) {
            for (GregorianMonth month : GregorianMonth.values()) {
                if (month.getName().equals(name)) {
                    return month.getMonth();
                }
            }
            return Integer.MIN_VALUE;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    private int     year    = LocalDate.now().getYear();
    private int     month   = UNDEFINED_NUM;
    private int     day     = UNDEFINED_NUM;
    private int     hour    = 0;
    private int     minute  = 0;

    public GregorianDate(int year, int month, int day, int hour, int minute) {
        setYear(year);
        setMonth(month);
        setDay(day);
        setHour(hour);
        setMinute(minute);
    }

    public GregorianDate(int year, int month, int day) {
        setYear(year);
        setMonth(month);
        setDay(day);
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

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
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

    public void setHour(int hour) {
        this.hour = hour;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    @Override
    public LocalDateTime calculate() {
        return calculateDate().atTime(hour, minute);
    }

    @Override
    public LocalDate calculateDate() {
        return LocalDate.of(year, month, day);
    }
}
