package worldholidaydates.holidayparser;

import java.time.DayOfWeek;
import java.time.Instant;
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
    public static final int UNDEFINED_NUM = Integer.MIN_VALUE;
    public static final ZoneId DEFAULT_ZONE = ZoneId.of("GMT");

    int         year        = UNDEFINED_NUM;
    int         month       = UNDEFINED_NUM;
    NamedMonth  namedMonth  = null;
    int         dayOfMonth  = UNDEFINED_NUM;

    int         hour        = 0;
    int         minute      = 0;

    // Gregorian days deviation from raw stored date
    int         offset      = 0;

    // weekday offset deviation from raw stored date
    int         offsetWeekDay = 0; // 1-7 corresponds to Monday-Sunday
    int         offsetWeekDayNth = 0; // 1-100th
    
    // Offset before or after raw stored date
    boolean     isAfter     = true;

    ZoneId      timezone    = DEFAULT_ZONE;

    /**
     * Default constructor
     */
    protected Date() {
        // empty
    }

    /**
     * 
     * 
     * @param year
     * @param month
     * @param dayOfMonth
     * @param hour
     * @param minute
     */
    protected Date(int year, int month, int dayOfMonth, int hour, int minute) {
        setYear(year);
        setMonth(month);
        setDayOfMonth(dayOfMonth);
        setHour(hour);
        setMinute(minute);
    }

    protected Date(int year, int month, int dayOfMonth) {
        this(year, month, dayOfMonth, 0, 0);
    }

    protected Date(int month, int dayOfMonth, int hour, int minute) {
        this(GregorianDate.DEFAULT_GREGORIAN_YEAR, month, dayOfMonth, hour, minute);
    }

    protected Date(int month, int dayOfMonth) {
        this(GregorianDate.DEFAULT_GREGORIAN_YEAR, month, dayOfMonth, 0, 0);
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

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public int getOffset() {
        return offset;
    }

    public int getOffsetWeekDay() {
        return offsetWeekDay;
    }

    public int getOffsetWeekDayNth() {
        return offsetWeekDayNth;
    }

    /**
     * Returns the direction of offset calcuation.
     * 
     * @return true if the offset direction is after, false if it is before
     */
    public boolean getOffsetDirection() {
        return isAfter;
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
    
    public void setHour(int hour) {
        this.hour = hour;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public void setOffset(int offset, boolean isAfter) {
        this.offset = offset;
        this.isAfter = isAfter;
    }

    public void setOffsetWeekDay(int offsetWeekDay) {
        if (offsetWeekDay < 1 || offsetWeekDay > 7) {
            throw new IllegalArgumentException("offsetWeekDay must be between 1 and 7");
        }
        this.offsetWeekDay = offsetWeekDay;
    }
    
    public void setOffsetWeekDay(int offsetWeekDay, int offsetWeekDayNth) {
        setOffsetWeekDay(offsetWeekDay);
        setOffsetWeekDayNth(offsetWeekDayNth);
    }

    public void setOffsetWeekDay(int offsetWeekDay, int offsetWeekDayNth, boolean isAfter) {
        setOffsetWeekDay(offsetWeekDay, offsetWeekDayNth);
        setOffsetDirection(isAfter);
    }

    public void setOffsetWeekDayNth(int offsetWeekDayNth) {
        if (offsetWeekDayNth < 0 || offsetWeekDayNth > 100) {
            throw new IllegalArgumentException("offsetWeekDayNth must be between 0 and 100");
        }
        this.offsetWeekDayNth = offsetWeekDayNth;
    }

    /**
     * Set the offset direction of this {@link Date}.
     * 
     * @param isAfter true if the offset direction is after, false if it is before
     */
    public void setOffsetDirection(boolean isAfter) {
        this.isAfter = isAfter;
    }

    public void setTimezone(ZoneId timezone) {
        this.timezone = timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = ZoneId.of(timezone);
    }

    /**
     * Calculates the Gregorian date and time created only from the year,
     * month, day, hour, and minutes stored in this {@link Date}, along
     * with any offset (deviation from raw dates). To get the date with offset,
     * use the {@link #calculate} method instead.
     * <p>
     * The returned date will be of {@link LocalDate}. Support for more options,
     * such as Threeten, will be implemented later.
     * 
     * @return the Gregorian date created only from the year, month, day,
     *      with offset, if any
     */
    public LocalDateTime calculate() {
        return calculateDate().atTime(hour, minute);
    }

    /**
     * Similar to {@link #calculate}, but converted to {@link ZonedDateTime}
     * with the stored {@link #timezone}.
     * 
     * @return the Gregorian date created only from the year, month, day,
     *      with offset, if any, at the stored {@link #timezone}
     */
    public ZonedDateTime calculateWithTimeZone() {
        return calculate().atZone(timezone);
    }

    /**
     * Calculates the Gregorian date created from the year, month, day, along
     * with any offset (deviation from raw dates), if any.
     * <p>
     * The returned date will be of {@link LocalDate}. Support for more options,
     * such as Threeten, will be implemented later.
     * 
     * @return the Gregorian date created from the year, month, day stored in
     *      this date, with offset, if any
     */
    public LocalDate calculateDate() {
        LocalDate result = calculateRawDate();
        if (offset != 0) {
            result = getOffsetDate(result.atStartOfDay(), (isAfter) ? offset : -offset).toLocalDate();
        }
        if (offsetWeekDay != 0) {
            result = getOffsetWeekDayDate(result.atStartOfDay(), offsetWeekDay, offsetWeekDayNth, isAfter).toLocalDate();
        }
        return result;
    }

    /**
     * Calculates the Gregorian date and time created only from the year,
     * month, day, hour, and minutes stored in this {@link Date}, without 
     * taking any offset into calculation. To get the date with offset,
     * use the {@link #calculate} method instead.
     * <p>
     * The returned date will be of {@link LocalDate}. Support for more options,
     * such as Threeten, will be implemented later.
     * 
     * @return the Gregorian date created only from the year, month, day,
     *      without taking any offset into calculation.
     */
    public LocalDateTime calculateRaw() {
        return calculateRawDate().atTime(hour, minute);
    }

    /**
     * Calculates the Gregorian date created only from the year, month, day,
     * stored in this {@link Date} without taking any offset into calculation.
     * To get the date with offset, use the {@link #calculateDate} method
     * instead.
     * <p>
     * The returned date will be of {@link LocalDate}. Support for more options,
     * such as Threeten, will be implemented later.
     * <p>
     * Note that the method is abstract here is because this is intended for
     * use with different calendar systems in the world.
     * 
     * @return the Gregorian date created only from the year, month, day,
     *      without taking any offset into calculation.
     */
    public abstract LocalDate calculateRawDate();

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
        double second;
        double tm;
    
        tm = 24 * (dayOfMonth - Math.floor(dayOfMonth));
        hour = Math.floor(tm);
        tm = 60 * (tm - hour);
        minute = Math.floor(tm);
        tm = 60 * (tm - minute);
        second = Math.round(tm);
        
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
}
