package worldholidaydates.holidayparser;

import java.time.LocalDate;

import javax.annotation.Nullable;

import org.threeten.extra.chrono.JulianDate;

public class JulianCalDate extends Date {
    public enum JulianMonth implements NamedMonth {
        IANURARIUS(1, "Ianuarius"),
        FEBRUARIUS(2, "Februarius"),
        MARTURIUS(3, "Martius"),
        APRILIS(4, "Aprilis"),
        MAIUS(5, "Maius"),
        IUNIUS(6, "Iunius"),
        IULIUS(7, "Iulius"),
        AUGUSTUS(8, "Augustus"),
        SEPTEMBER(9, "September"),
        OCTOBER(10, "October"),
        NOVEMBER(11, "November"),
        DECEMBER(12, "December");
        
        private int value;
        private String name;
        
        private JulianMonth(int value, String name) {
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

        /**
         * Convert a Julian month name to a Julian month number.
         * 
         * @param name a month name
         * @return a corresponding month number from 1 to 12
         */
        public static JulianMonth fromName(String name) {
            for (JulianMonth month : JulianMonth.values()) {
                if (month.getName().equals(name)) {
                    return month;
                }
            }
            return null;
        }

        /**
         * Convert a Julian month number to a Julian month name.
         * 
         * @param value a Julian month number from 1 to 12
         * @return a corresponding Julian month name
         */
        @Nullable
        public static JulianMonth fromValue(int value) {
            for (JulianMonth month : JulianMonth.values()) {
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
    public static int DEFAULT_JULIAN_YEAR = 2021;

    public JulianCalDate() {
        super();
        setYear(DEFAULT_JULIAN_YEAR);
    }

    public JulianCalDate(int year, int month, int dayOfMonth, int time) {
        super(year, month, dayOfMonth, time);
    }

    public JulianCalDate(int year, int month, int dayOfMonth) {
        this(year, month, dayOfMonth, 0);
    }

    public JulianCalDate(int month, int dayOfMonth) {
        this(DEFAULT_JULIAN_YEAR, month, dayOfMonth, 0);
    }

    @Override
    public JulianMonth getNamedMonth() {
        return (JulianMonth) namedMonth;
    }

    @Override
    public LocalDate calculateDate() {
        JulianDate result = JulianDate.of(year, month, dayOfMonth);
        return julianToLocalDate(result);
    }
    
    /**
     * Converts Threeten's julian date to {@link LocalDate}.
     * 
     * @param julianDate a julian date
     * @return a {@link LocalDate}
     */
    public static LocalDate julianToLocalDate(org.threeten.extra.chrono.JulianDate julianDate) {
        return LocalDate.ofEpochDay(julianDate.toEpochDay());
    }

    public static void main(String[] args) {
        System.out.println(JulianDate.now());
        System.out.println(julianToLocalDate(JulianDate.now()));
    }
}
