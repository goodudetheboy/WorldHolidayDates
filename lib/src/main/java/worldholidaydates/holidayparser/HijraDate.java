package worldholidaydates.holidayparser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.HijrahChronology;
import java.time.chrono.HijrahEra;

public class HijraDate implements Date {
    public static final int DEFAULT_HIJRA_YEAR = 1442; // Gregorian Year = 2021-2022

    // (Muharram|Safar|Rabi al-awwal|Rabi al-thani|Jumada al-awwal|Jumada al-thani|Rajab|Shaban|Ramadan|Shawwal|Dhu al-Qidah|Dhu al-Hijjah)
    public enum HijraMonth {
        MUHARRAM(1, "Muharram"),
        SAFAR(2, "Safar"),
        RABI_AL_AWWAL(3, "Rabi al-awwal"),
        RABI_AL_THANI(4, "Rabi al-thani"),
        JUMADA_AL_AWWAL(5, "Jumada al-awwal"),
        JUMADA_AL_THANI(6, "Jumada al-thani"),
        RAJAB(7, "Rajab"),
        SHABAN(8, "Shaban"),
        RAMADAN(9, "Ramadan"),
        SHAWWAL(10, "Shawwal"),
        DHU_AL_QIDAH(11, "Dhu al-Qidah"),
        DHU_AL_HIJJAH(12, "Dhu al-Hijjah");

        private final int month;
        private final String name;

        HijraMonth(int month, String name) {
            this.month = month;
            this.name = name;
        }

        public int getMonth() {
            return month;
        }

        public String getName() {
            return name;
        }

        public static HijraMonth fromMonth(int month) {
            for (HijraMonth h : HijraMonth.values()) {
                if (h.getMonth() == month) {
                    return h;
                }
            }
            return null;
        }

        public static HijraMonth fromName(String name) {
            for (HijraMonth h : HijraMonth.values()) {
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

    private int year = DEFAULT_HIJRA_YEAR;
    private HijraMonth month = null;
    private int day = Date.UNDEFINED_NUM;

    public HijraDate() {
        // empty
    }

    public HijraDate(int year, HijraMonth month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public HijraDate(HijraMonth month, int day) {
        this.month = month;
        this.day = day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setMonth(HijraMonth month) {
        this.month = month;
    }

    @Override
    public LocalDateTime calculate() {
        return calculateDate().atStartOfDay();
    }

    @Override
    public LocalDate calculateDate() {
        java.time.chrono.HijrahDate date =  HijrahChronology.INSTANCE.date(HijrahEra.AH, year, month.getMonth(), day);
        return LocalDate.from(date);
    }
}
