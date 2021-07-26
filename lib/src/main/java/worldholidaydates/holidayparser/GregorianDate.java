package worldholidaydates.holidayparser;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class GregorianDate implements Date{
    public static final int UNDEFINED_NUM = Integer.MIN_VALUE;

    private int year = LocalDate.now().getYear();
    private int month = UNDEFINED_NUM;
    private int day = UNDEFINED_NUM;
    private int hour = 0;
    private int minute = 0;

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
        return LocalDateTime.of(year, month, day, hour, minute);
    }

    @Override
    public LocalDate calculateDate() {
        return LocalDate.of(year, month, day);
    }
}
