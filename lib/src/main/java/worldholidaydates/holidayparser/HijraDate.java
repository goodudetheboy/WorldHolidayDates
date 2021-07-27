package worldholidaydates.holidayparser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.HijrahChronology;
import java.time.chrono.HijrahDate;
import java.time.chrono.HijrahEra;

import org.omg.CORBA.PUBLIC_MEMBER;

public class HijraDate implements Date {
    public static final int DEFAULT_HIJRA_YEAR = 1442; // Gregorian Year = 2021-2022

    public enum Month {
        MUHARRAM,
        SAFAR,
        RABI_AL_AWWAL,
        RABI_AL_THANI,
        JUMADA_AL_AWWAL,
        JUMADA_AL_THANI,
        RAJAB,
        SHABAN,
        RAMADAN,
        SHAWWAL,
        DHU_AL_QIDAH,
        DHU_AL_HIJJAH;

        public static Month hijraMonthConvert(String monthString) {
            switch (monthString) {
                case "Muharram":
                    return Month.MUHARRAM;
                case "Safar":
                    return Month.SAFAR;
                case "Rabi al-awwal":
                    return Month.RABI_AL_AWWAL;
                case "Rabi al-thani":
                    return Month.RABI_AL_THANI;
                case "Jumada al-awwal":
                    return Month.JUMADA_AL_AWWAL;
                case "Jumada al-thani":
                    return Month.JUMADA_AL_THANI;
                case "Rajab":
                    return Month.RAJAB;
                case "Shaban":
                    return Month.SHABAN;
                case "Ramadan":
                    return Month.RAMADAN;
                case "Shawwal":
                    return Month.SHAWWAL;
                case "Dhu al-Qidah":
                    return Month.DHU_AL_QIDAH;
                case "Dhu al-Hijjah":
                    return Month.DHU_AL_HIJJAH;
                default:
                    return null;
            }
        }

        public int toInt() {
            return this.ordinal() + 1;
        }
    }

    private int year = DEFAULT_HIJRA_YEAR;
    private Month month = null;
    private int dayOfMonth = Date.UNDEFINED_NUM;

    public HijraDate() {
        // empty
    }

    public HijraDate(int year, Month month, int dayOfMonth) {
        this.year = year;
        this.month = month;
        this.dayOfMonth = dayOfMonth;
    }

    public HijraDate(Month month, int dayOfMonth) {
        this.month = month;
        this.dayOfMonth = dayOfMonth;
    }

    public void setDayOfMonth(int dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public void setMonth(Month month) {
        this.month = month;
    }

    @Override
    public LocalDateTime calculate() {
        return calculateDate().atStartOfDay();
    }

    @Override
    public LocalDate calculateDate() {
        java.time.chrono.HijrahDate date =  HijrahChronology.INSTANCE.date(HijrahEra.AH, year, month.toInt(), dayOfMonth);
        return LocalDate.from(date);
    }
 

}
