package worldholidaydates.holidayparser;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.annotation.Nullable;

/**
 * An abstract class containing the date information for a holiday. Each
 * subclass of this class represents special implementations of a calendar
 * around the world
 */
public abstract class Date {
    public static final int UNDEFINED_NUM = Integer.MIN_VALUE;

    int         year        = UNDEFINED_NUM;
    int         month       = UNDEFINED_NUM;
    NamedMonth  namedMonth  = null;
    int         dayOfMonth  = UNDEFINED_NUM;

    int         hour        = 0;
    int         minute      = 0;

    // Gregorian days deviate from raw stored date
    int         offset      = 0;

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
        return getOffsetDate(calculateRawDate(), offset);
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
     * Return the date that is offset by some days from the input date,
     * according to the following rule:
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
    public static LocalDate getOffsetDate(LocalDate date, int offset) {
        if (offset > 0) {
            return date.plusDays(offset);
        } 
        if (offset < 0) {
            return date.minusDays(-offset);
        }
        return date;
    }

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
}
