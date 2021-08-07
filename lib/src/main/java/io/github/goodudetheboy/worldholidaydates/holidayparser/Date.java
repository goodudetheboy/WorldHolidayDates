package io.github.goodudetheboy.worldholidaydates.holidayparser;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;

import javax.annotation.Nullable;

/**
 * An abstract class containing the date information for a holiday. Each
 * subclass of this class represents special implementations of a calendar
 * around the world
 */
public abstract class Date {
    public static final int     UNDEFINED_NUM   = Integer.MIN_VALUE;
    public static final ZoneId  DEFAULT_ZONE    = ZoneId.of("GMT");
    public static final int     MIN_TIME        = 0;
    public static final int     MAX_TIME        = 1440 - 1; // can't have 24:00 time

    int         year        = UNDEFINED_NUM;
    int         month       = UNDEFINED_NUM;
    NamedMonth  namedMonth  = null;
    int         dayOfMonth  = UNDEFINED_NUM;

    int         startTime   = UNDEFINED_NUM; // in minutes, MAX = 1440 - 1, MIN = 0

    ZoneId      timezone    = DEFAULT_ZONE;

    /**
     * Default constructor
     */
    protected Date() {
        // empty
    }

    /**
     * Constructs a {@link Date} with a given year, month, dayOfMonth and time.
     * There's no IllegalArgumentException thrown if the parameters are invalid,
     * so please use with caution.
     * 
     * @param year the year
     * @param month the month, 1-12
     * @param dayOfMonth the day of month, 1-31
     * @param time the time, in minutes, {@link #MIN_TIME} to {@link #MAX_TIME}
     */
    protected Date(int year, int month, int dayOfMonth, int time) {
        setYear(year);
        setMonth(month);
        setDayOfMonth(dayOfMonth);
        setStartTime(time);
    }

    /**
     * Constructs a {@link Date} with a given year, month, dayOfMonth, with time
     * set to {@link #MIN_TIME}. There's no IllegalArgumentException thrown if
     * the parameters are invalid, so please use with caution.
     * 
     * @param year the year
     * @param month the month, 1-12
     * @param dayOfMonth the day of month, 1-31
     */
    protected Date(int year, int month, int dayOfMonth) {
        this(year, month, dayOfMonth, 0);
    }

    /**
     * Constructs a {@link Date} with a given month, dayOfMonth, with year set
     * to default year set in each date and time set to {@link #MIN_TIME}. There's
     * no IllegalArgumentException thrown if the parameters are invalid, so
     * please use with caution.
     * 
     * @param month the month, 1-12
     * @param dayOfMonth the day of month, 1-31
     */
    protected Date(int month, int dayOfMonth) {
        setMonth(month);
        setDayOfMonth(dayOfMonth);
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public abstract NamedMonth getNamedMonth();

    public int getDayOfMonth() {
        return dayOfMonth;
    }

    /**
     * @return the start time of this {@link Date} (in minutes)
     */
    public int getStartTime() {
        return startTime;
    }

    public ZoneId getTimezone() {
        return timezone;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setNamedMonth(NamedMonth namedMonth) {
        this.namedMonth = namedMonth;
    }

    public void setDayOfMonth(int dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }
    
    /**
     * Sets the start time of this {@link Date} (in minutes).
     * 
     * @param startTime the start time (in minutes)
     */
    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public void setTimezone(ZoneId timezone) {
        this.timezone = timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = ZoneId.of(timezone);
    }

    /**
     * Calculates the {@link LocalDateTime} of this {@link Date}.
     * <p>
     * If the {@link Date} already have a year, it will be used. Otherwise,the
     * default year will be used.
     * 
     * @param defaultYear default Gregorian year
     * @return the {@link LocalDateTime} of this {@link Date}
     */
    public LocalDateTime calculate(int defaultYear) {
        LocalDate rawDate = calculateDate(defaultYear);
        LocalTime rawTime = (startTime != UNDEFINED_NUM)
                            ? minutesToLocalTime(startTime) 
                            : minutesToLocalTime(0);
        return rawDate.atTime(rawTime);
    }

    /**
     * Similar to {@link #calculate}, but converted to {@link ZonedDateTime}
     * with the stored {@link #timezone}.
     * <p>
     * If the {@link Date} already have a year, it will be used. Otherwise,the
     * default year will be used.
     * 
     * @param defaultYear optional Gregorian year
     * @return the Gregorian date created only from the year, month, day, and
     *      start time with offset, if any, at the stored {@link #timezone}
     */
    public ZonedDateTime calculateWithTimeZone(int defaultYear) {
        return calculate(defaultYear).atZone(timezone);
    }

    /**
     * Calculates the {@link LocalDate} of this {@link Date}. This must be
     * implemented depending on the system of calendar.
     * <p>
     * If the {@link Date} already have a year, it will be used. Otherwise,the
     * default year will be used.
     * 
     * @param defaultYear default Gregorian year
     * @return the {@link LocalDate} of this {@link Date}
     */
    public abstract LocalDate calculateDate(int defaultYear);

    /**
     * @return a string representation of the date with named month, which is
     *      special to each {@link Date} implementation.}
     */
    @Nullable
    public String toNamedString() {
        if (namedMonth == null) {
            return null;
        }
        StringBuilder b = new StringBuilder();
        b.append(namedMonth).append(" ")
         .append(dayOfMonth).append(", ")
         .append(year);
        
        return b.toString();
    }

    /**
     * @return a string representation of the date, which is special to each
     * {@link Date} implementation
     */
    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append(year).append("-")
         .append(month).append("-")
         .append(dayOfMonth).append("-");
        
         return String.format("%d-%s-%d", year, month, dayOfMonth);        
    }

    
    /**
     * Return the {@link LocalDateTime} that is offset by some days from the input
     * date, according to the following rule:
     * <ol>
     * <li> offset < 0: return [offset] days before input date
     * <li> offset > 0: return [offset] days after input date
     * <li> offset = 0: return original date
     * </ol>
     * <p>
     * 
     * @param date an input date
     * @param offset offset, in days
     * @return the date that is offset by some [offset] days from the input date
     */
    public static LocalDateTime getOffsetDate(LocalDateTime date, int offset) {
        if (offset > 0) {
            return date.plusDays(offset);
        } 
        if (offset < 0) {
            return date.minusDays(-offset);
        }
        return date;
    }

    /**
     * Return the {@link ZonedDateTime} that is offset by some days from the
     * input date, according to the following rule:
     * <ol>
     * <li> offset < 0: return [offset] days before input date
     * <li> offset > 0: return [offset] days after input date
     * <li> offset = 0: return original date
     * </ol>
     * <p>
     * 
     * @param date an input date
     * @param offset offset, in days
     * @return the date that is offset by some [offset] days from the input date
     */
    public static ZonedDateTime getOffsetZonedDate(ZonedDateTime zDate, int offset) {
        if (offset > 0) {
            return zDate.plusDays(offset);
        } 
        if (offset < 0) {
            return zDate.minusDays(-offset);
        }
        return zDate;
    }

    /**
     * Returns a {@link LocalDateTime} that is offset by an nth weekday before
     * or after the input date.
     * 
     * @param date an input {@link LocalDateTime}
     * @param weekday a weekday to be offset before or after
     * @param nth count of the weekday before or after
     * @param isAfter true to offset after, false for before
     * @return a {@link LocalDateTime} that is offset by an nth weekday before
     *      or after the input date
     */
    public static LocalDateTime getOffsetWeekDayDate(LocalDateTime date, int weekday, int nth, boolean isAfter) {
        if (nth == 0) {
            return date;
        }
        DayOfWeek dayOfWeek = DayOfWeek.of(weekday);
        TemporalAdjuster dir = (isAfter)
                            ? TemporalAdjusters.next(dayOfWeek)
                            : TemporalAdjusters.previous(dayOfWeek);
        LocalDateTime result = date.with(dir);
        nth -= 1;
        if (nth == 0) {
            return result;
        } else {
            return getOffsetWeekDayDate(result, weekday, nth, isAfter);
        }
    }

    /**
     * Returns a {@link ZonedDateTime} that is offset by an nth weekday before
     * or after the input date.
     * 
     * @param zDate an input {@link ZonedDateTime}
     * @param weekday a weekday to be offset before or after
     * @param nth count of the weekday before or after
     * @param isAfter true to offset after, false for before
     * @return a {@link ZonedDateTime} that is offset by an nth weekday before
     *      or after the input date
     */
    public static ZonedDateTime getOffsetWeekDayZonedDate(ZonedDateTime zDate, int weekday, int nth, boolean isAfter) {
        if (nth == 0) {
            return zDate;
        }
        DayOfWeek dayOfWeek = DayOfWeek.of(weekday);
        TemporalAdjuster dir = (isAfter)
                            ? TemporalAdjusters.next(dayOfWeek)
                            : TemporalAdjusters.previous(dayOfWeek);
        ZonedDateTime result = zDate.with(dir);
        nth -= 1;
        if (nth == 0) {
            return result;
        } else {
            return getOffsetWeekDayZonedDate(result, weekday, nth, isAfter);
        }
    }

    /**
     * Converts Julian Ephemeris Day (JDE) value to LocalDateTime, set at GMT
     * timezone, although there is nothing guaranteed about this :)
     * <p>
     * Part of this code is adapted from
     * <a href="https://github.com/sualeh/sunposition">sunposition</a>.
     * 
     * @param jde input JDE value
     * @return corresponding LocalDateTime
     * @see <a href="https://en.wikipedia.org/wiki/Julian_day">Julian Day</a>
     * @see <a href="https://en.wikipedia.org/wiki/Ephemeris_day">Ephemeris Day</a>
     */
    public static ZonedDateTime jdeToDate(double jde) {
        final double p = Math.floor(jde + 0.5);
        final double s1 = p + 68569;
        final double n = Math.floor(4 * s1 / 146097);
        final double s2 = s1 - Math.floor((146097 * n + 3) / 4);
        final double i = Math.floor(4000 * (s2 + 1) / 1461001);
        final double s3 = s2 - Math.floor(1461 * i / 4) + 31;
        final double q = Math.floor(80 * s3 / 2447);
        final double e = s3 - Math.floor(2447 * q / 80);
        final double s4 = Math.floor(q / 11);
    
        final double month = q + 2 - 12 * s4;
        final double year = 100 * (n - 49) + i + s4;
        final double dayOfMonth = e + jde - p + 0.5;
    
        double hour;
        double minute;
        double tm;
    
        tm = 24 * (dayOfMonth - Math.floor(dayOfMonth));
        hour = Math.floor(tm);
        tm = 60 * (tm - hour);
        minute = Math.floor(tm);
        
        return ZonedDateTime.of(LocalDate.of((int) year, (int) month, (int) dayOfMonth),
                                LocalTime.of((int) hour, (int) minute),
                                DEFAULT_ZONE);
    }

    /**
     * Converts a weekday name to the following mapping:
     * <p>
     * <ol>
     * <li> "Monday" -> 1
     * <li> "Tuesday" -> 2
     * <li> "Wednesday" -> 3
     * <li> "Thursday" -> 4
     * <li> "Friday" -> 5
     * <li> "Saturday" -> 6
     * <li> "Sunday" -> 7
     * </ol>
     * 
     * @param weekday a weekday name
     * @return a number that represents the weekday
     */
    public static int weekdayToValue(String weekday) {
        switch (weekday) {
            case "Monday":
                return 1;
            case "Tuesday":
                return 2;
            case "Wednesday":
                return 3;
            case "Thursday":
                return 4;
            case "Friday":
                return 5;
            case "Saturday":
                return 6;
            case "Sunday":
                return 7;
            default:
                throw new IllegalArgumentException("Invalid weekday: " + weekday);
        }
    }

    /**
     * Converts a minute value from {@link MIN_TIME} to {@link MAX_TIME} to an
     * instance of {@link LocalTime}.
     * 
     * @param timeInMinutes a minute value from {@link MIN_TIME} to {@link MAX_TIME}
     * @return an instance of {@link LocalTime}
     */
    public static LocalTime minutesToLocalTime(int timeInMinutes) {
        int hour = timeInMinutes / 60;
        int minute = timeInMinutes % 60;
        return LocalTime.of(hour, minute);
    }

    /**
     * Gets the next or previous weekday starting from the input date.
     * 
     * @param date a date
     * @param weekday a weekday
     * @param isAfter true to get the next weekday, false for the previous
     * @return a date that is the next or previous weekday from the input date
     */
    public static LocalDate getNextOrPreviousWeekday(LocalDate date, int weekday, boolean isAfter) {
        return (isAfter) ? date.with(TemporalAdjusters.next(DayOfWeek.of(weekday)))
                         : date.with(TemporalAdjusters.previous(DayOfWeek.of(weekday)));
    }
}
