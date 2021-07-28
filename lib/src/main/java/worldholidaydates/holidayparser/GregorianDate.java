package worldholidaydates.holidayparser;

import java.time.LocalDate;

import javax.annotation.Nullable;

/**
 * A class for calculating the date of Gregorian dates
 */
public class GregorianDate extends Date {
    public enum GregorianMonth implements NamedMonth {
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
        
        private int value;
        private String name;
        
        private GregorianMonth(int month, String name){
            this.value = month;
            this.name = name;
        }
        
        @Override
        public int getValue(){
            return value;
        }
        
        @Override
        public String getName(){
            return name;
        }

        /**
         * Convert a Gregorian month name to a Gregorian month number.
         * 
         * @param name a month name
         * @return a corresponding month number from 1 to 12
         */
        public static GregorianMonth fromName(String name) {
            for (GregorianMonth month : GregorianMonth.values()) {
                if (month.getName().equals(name)) {
                    return month;
                }
            }
            return null;
        }

        /**
         * Convert a Gregorian month number to a Gregorian month name.
         * 
         * @param value a Gregorian month number from 1 to 12
         * @return a corresponding Gregorian month name
         */
        @Nullable
        public static GregorianMonth fromValue(int value) {
            for (GregorianMonth month : GregorianMonth.values()) {
                if (month.getValue() == value) {
                    return month;
                }
            }
            return null;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    public static final int DEFAULT_GREGORIAN_YEAR = 2021;

    public GregorianDate() {
        super();
        setYear(DEFAULT_GREGORIAN_YEAR);
    }

    public GregorianDate(int year, int month, int dayOfMonth, int hour, int minute) {
        super(year, month, dayOfMonth, hour, minute);
    }

    public GregorianDate(int year, int month, int dayOfMonth) {
        super(year, month, dayOfMonth, 0, 0);
    }

    public GregorianDate(int month, int dayOfMonth, int hour, int minute) {
        super(month, dayOfMonth, hour, minute);
        setYear(DEFAULT_GREGORIAN_YEAR);
    }

    public GregorianDate(int month, int dayOfMonth) {
        super(month, dayOfMonth);
        setYear(DEFAULT_GREGORIAN_YEAR);
    }

    @Override
    public GregorianMonth getNamedMonth() {
        return (GregorianMonth) namedMonth;
    }

    @Override
    public void setMonth(int month) {
        super.setMonth(month);
        super.setNamedMonth(GregorianMonth.fromValue(month));
    }

    @Override
    public void setNamedMonth(NamedMonth namedMonth) {
        if (namedMonth instanceof GregorianMonth) {
            super.setMonth(((GregorianMonth) namedMonth).getValue());
        } else {
            throw new IllegalArgumentException("NamedMonth must be a GregorianMonth");
        }
    }

    /**
     * Calculates the raw date stored in this {@link GregorianDate}, in the
     * Gregorian calendar.
     * 
     * @return a {@link LocalDate} object representing the raw date in
     *      Gregorian calendar
     */
    @Override
    public LocalDate calculateRawDate() {
        return LocalDate.of(year, month, dayOfMonth);
    }
}
