package worldholidaydates.holidayparser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Rule {
    public static final int UNDEFINED_NUM = Integer.MIN_VALUE;
    Date    rawDate    = null;
     
    // the range of this {@link Date} in minutes, the default range is from the
    // startTime to the end of the stored day
    int         range       = UNDEFINED_NUM; 

    // Gregorian days deviation from raw stored date
    int         offset      = 0;

    // weekday offset deviation from raw stored date
    int         offsetWeekDay = 0; // 1-7 corresponds to Monday-Sunday
    int         offsetWeekDayNth = 0; // 1-100th
    
    // Offset before or after raw stored date
    boolean     isAfter     = true;

    public Rule() {
        // empty
    }

    public Date getRawDate() {
        return rawDate;
    }

    /**
     * @return the range of this {@link Date} (in minutes)
     */
    public int getRange() {
        return range;
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

    public void setRawDate(Date mainDate) {
        this.rawDate = mainDate;
    }

    /**
     * Sets the range of this {@link Date} (in minutes).
     * 
     * @param range the range (in minutes)
     */
    public void setRange(int range) {
        this.range = range;
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

    /**
     * Shifts the input date by the {@link #offset} and {@link #offsetWeekDay},
     * if any.
     * 
     * @param date the date to shift
     * @return the shifted date by offset and weekday offset, if any
     */
    private LocalDateTime offsetShift(LocalDateTime date) {
        LocalDateTime result = date;
        if (offset != 0) {
            result = Date.getOffsetDate(result, (isAfter) ? offset : -offset);
        }
        if (offsetWeekDay != 0) {
            result = Date.getOffsetWeekDayDate(result, offsetWeekDay, offsetWeekDayNth, isAfter);
        }
        return result;
    }

    /**
     * Calculates the start Gregorian date and time created from the {@link #rawDate},
     * along with any offset (deviation from raw dates).
     * <p>
     * The returned date will be of {@link LocalDate}. Support for more options,
     * such as Threeten, will be implemented later.
     * 
     * @return the Gregorian date created only from the year, month, day, and
     *      start time without taking any offset into calculation.
     */
    public LocalDateTime calculate() {
        LocalDateTime raw = calculateRaw();
        return offsetShift(raw);
    }

    /**
     * Calculates the Gregorian date and time created only from the {@link #rawDate},
     * along with any offset (deviation from raw dates).
     * <p>
     * The returned date will be of {@link LocalDate}. Support for more options,
     * such as Threeten, will be implemented later.
     * 
     * @return the Gregorian date created only from the year, month, day,
     *      with offset, if any
     */
    public LocalDateTime calculateEnd() {
        return calculate().plusMinutes(range);
    }
    
    /**
     * Calculates the start Gregorian date created from the {@link #rawDate},
     * and along with any offset (deviation from raw dates), if any.
     * <p>
     * The returned date will be of {@link LocalDate}. Support for more options,
     * such as Threeten, will be implemented later.
     * 
     * @return the Gregorian date created from the year, month, day stored in
     *      this date, with offset, if any
     */
    public LocalDate calculateDate() {
        LocalDateTime raw = calculateRaw();
        return offsetShift(raw).toLocalDate();
    }

    /**
     * Calculates the end Gregorian date created from the {@link #rawDate},
     * shifted by {@link range}, and along with any offset (deviation
     * from raw dates), if any.
     * <p>
     * The returned date will be of {@link LocalDate}. Support for more options,
     * such as Threeten, will be implemented later.
     * 
     * @return the Gregorian date created from the year, month, day stored in
     *      this date, with offset, if any
     */
    public LocalDate calculateDateEnd() {
        LocalDateTime raw = calculateRawEnd();
        return offsetShift(raw).toLocalDate();
    }

    /**
     * Calculates the start Gregorian date and time of the {@link #rawDate}
     * stored in this {@link Rule}. To get the date with offset, use the
     * {@link #calculate} method instead. To get the end date, use 
     * <p>
     * The returned date will be of {@link LocalDate}. Support for more
     * options, such as Threeten, will be implemented later.
     * 
     * @return the Gregorian date of the {@link #rawDate}
     */
    public LocalDateTime calculateRaw() {
        return rawDate.calculate();
    }

    /**
     * Calculates the Gregorian date and time created only from from the
     * {@link #rawDate}, shifted by {@link range} in this {@link Date}, without 
     * taking any offset into calculation. To get the date with offset, use the
     * {@link #calculateEnd} method instead.
     * <p>
     * By default, the end time is defined as the This is useful for when a
     * {@link Date} is defined to have range longer than one day/24 hours.
     * <p>
     * The returned date will be of {@link LocalDate}. Support for more options,
     * such as Threeten, will be implemented later.
     * 
     * @return the Gregorian date created only from the year, month, day, and
     *      end time without taking any offset into calculation.
     */
    public LocalDateTime calculateRawEnd() {
        LocalDateTime raw = calculateRaw();
        if (range == UNDEFINED_NUM) {
            LocalDate date = raw.toLocalDate();
            LocalTime time = Date.minutesToLocalTime(Date.MAX_TIME);
            return date.atTime(time);
        }
        return raw.plusMinutes(range);
    }

    /**
     * Calculates the start Gregorian date created only from the {@link #rawDate},
     * without taking any offset into calculation. To get the date with offset,
     * use the {@link #calculateDate} method instead.
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

    public LocalDate calculateRawDate() {
        return rawDate.calculateDate();
    }


    /**
     * Calculates the end Gregorian date created only from the {@link #rawDate},
     * shifted by {@link range}, without taking any offset into calculation.
     * To get the end date with offset, use the {@link #calculateDateEnd} method
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
    public LocalDate calculateRawDateEnd() {
        return calculateRawEnd().toLocalDate();
    }
}
