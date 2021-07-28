package worldholidaydates.holidayparser;

import java.time.LocalDate;
import java.time.LocalDateTime;

import net.time4j.PlainDate;
import net.time4j.calendar.HebrewCalendar;

public class HebrewDate implements Date {
    public enum HebrewMonth {
        NISAN(1, "Nisan"),
        IYYAR(2, "Iyyar"),
        SIVAN(3, "Sivan"),
        TAMUZ(4, "Tamuz"),
        AV(5, "Av"),
        ELUL(6, "Elul"),
        TISHREI(7, "Tishrei"),
        CHESHVAN(8, "Cheshvan"),
        KISLEV(9, "Kislev"),
        TEVET(10, "Tevet"),
        SHVAT(11, "Shvat"),
        ADAR(12, "Adar");

        private final int month;
        private final String name;

        HebrewMonth(int month, String name) {
            this.month = month;
            this.name = name;
        }

        public int getMonth() {
            return month;
        }

        public String getName() {
            return name;
        }

        public net.time4j.calendar.HebrewMonth toTime4jHebrewMonth() {
            switch(this) {
                case NISAN:
                    return net.time4j.calendar.HebrewMonth.NISAN;
                case IYYAR:
                    return net.time4j.calendar.HebrewMonth.IYAR;
                case SIVAN:
                    return net.time4j.calendar.HebrewMonth.SIVAN;
                case TAMUZ:
                    return net.time4j.calendar.HebrewMonth.TAMUZ;
                case AV:
                    return net.time4j.calendar.HebrewMonth.AV;
                case ELUL:
                    return net.time4j.calendar.HebrewMonth.ELUL;
                case TISHREI:
                    return net.time4j.calendar.HebrewMonth.TISHRI;
                case CHESHVAN:
                    return net.time4j.calendar.HebrewMonth.HESHVAN;
                case KISLEV:
                    return net.time4j.calendar.HebrewMonth.KISLEV;
                case TEVET:
                    return net.time4j.calendar.HebrewMonth.TEVET;
                case SHVAT:
                    return net.time4j.calendar.HebrewMonth.SHEVAT;
                case ADAR:
                    return net.time4j.calendar.HebrewMonth.ADAR_I;
                default:
                    // It is not possible to reach this point
                    throw new IllegalStateException("Unknown HebrewMonth: " + this);
            }
        }

        public static HebrewMonth fromMonth(int month) {
            for (HebrewMonth h : HebrewMonth.values()) {
                if (h.getMonth() == month) {
                    return h;
                }
            }
            return null;
        }

        public static HebrewMonth fromName(String name) {
            for (HebrewMonth h : HebrewMonth.values()) {
                if (h.getName().equals(name)) {
                    return h;
                }
            }
            return null;
        }

        @Override
        public String toString() {
            return name;
        }
    }
    public static final int DEFAULT_HEBREW_YEAR = 5781; // Gregorian Year = 2021-2022

    private int         year    = DEFAULT_HEBREW_YEAR;
    private HebrewMonth month   = null;
    private int         day     = Date.UNDEFINED_NUM;

    public HebrewDate() {
        // empty
    }

    public HebrewDate(int year, HebrewMonth month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public HebrewDate(HebrewMonth month, int day) {
        this.month = month;
        this.day = day;
    }

    public int getYear() {
        return year;
    }

    public HebrewMonth getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setMonth(HebrewMonth month) {
        this.month = month;
    }

    @Override
    public LocalDateTime calculate() {
        return calculateDate().atStartOfDay();
    }

    @Override
    public LocalDate calculateDate() {
        HebrewCalendar date = HebrewCalendar.of(year, month.toTime4jHebrewMonth(), day);
        PlainDate pdate = date.transform(PlainDate.class);
        return pdate.toTemporalAccessor();
    }

    @Override
    public String toString() {
        return String.format("%d-%s-%d", year, month.getName(), day);        
    }
}
