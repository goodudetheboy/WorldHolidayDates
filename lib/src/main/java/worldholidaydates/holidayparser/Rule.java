package worldholidaydates.holidayparser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

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

    // Start-time of holiday changes per weekday
    // list of if weekday, from 1-7
    List<List<Integer>>   ifWeekdays   = null; 
     // list of alternative time, from {@link Date#MIN_TIME} to {@link Date#MAX_TIME}
    List<Integer>   altTime     = null;

    public Rule() {
        // empty
    }

    /**
     * @return the raw {@link Date} of this {@link Rule}.
     */
    public Date getRawDate() {
        return rawDate;
    }

    /**
     * @return the range of the raw {@link Date} (in minutes)
     */
    public int getRange() {
        return range;
    }

    /**
     * @return the offset of the raw {@link Date} (in minutes)
     */
    public int getOffset() {
        return offset;
    }

    /**
     * @return the weekday offset of the raw {@link Date}.
     */
    public int getOffsetWeekDay() {
        return offsetWeekDay;
    }

    /**
     * @return the nth of the weekday offset of the raw {@link Date}.
     */
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

    /**
     * @return the list of if weekday, from 1-7
     */
    public List<List<Integer>> getIfWeekdays() {
        return ifWeekdays;
    }

    /**
     * @return the list of alternative time, from {@link Date#MIN_TIME} to {@link Date#MAX_TIME}
     */
    public List<Integer> getAlternateTime() {
        return altTime;
    }

    /**
     * Sets the {@link Date} to be used in the {@link Rule}
     * 
     * @param rawDate the {@link Date} to be used in the {@link Rule}
     * 
     */
    public void setRawDate(Date rawDate) {
        this.rawDate = rawDate;
    }

    /**
     * Sets the range of this {@link Date} (in minutes).
     * 
     * @param range the range (in minutes)
     */
    public void setRange(int range) {
        this.range = range;
    }

    /**
     * Sets the offset of this {@link Rule} (in minutes).
     * 
     * @param offset the offset (in minutes)
     */
    public void setOffset(int offset) {
        this.offset = offset;
    }

    /**
     * Sets the offset and the offset direction of this {@link Rule}
     * (in minutes).
     * 
     * @param offset the offset (in minutes)
     * @param isAfter the offset direction (true if the offset is after, false
     */
    public void setOffset(int offset, boolean isAfter) {
        this.offset = offset;
        this.isAfter = isAfter;
    }

    /**
     * Sets the weekday offset of this {@link Rule} (from 1-7).
     * 
     * @param offsetWeekDay the weekday offset (from 1-7)
     */
    public void setOffsetWeekDay(int offsetWeekDay) {
        if (offsetWeekDay < 1 || offsetWeekDay > 7) {
            throw new IllegalArgumentException("offsetWeekDay must be between 1 and 7");
        }
        this.offsetWeekDay = offsetWeekDay;
    }
    
    /**
     * Sets the weekday offset (from 1-7) and the nth of that weekday (from 1-100)
     * of this {@link Rule}.
     * 
     * @param offsetWeekDay the weekday offset (from 1-7)
     * @param offsetWeekDayNth the nth of that weekday (from 1-100)
     */
    public void setOffsetWeekDay(int offsetWeekDay, int offsetWeekDayNth) {
        setOffsetWeekDay(offsetWeekDay);
        setOffsetWeekDayNth(offsetWeekDayNth);
    }

    /**
     * Sets the weekday offset (from 1-7) and the nth of that weekday (from 1-100)
     * and the offset direction.
     * 
     * @param offsetWeekDay the weekday offset (from 1-7)
     * @param offsetWeekDayNth the nth of that weekday (from 1-100)
     * @param isAfter the offset direction (true if the offset is after, false
     *      if it is before)
     */
    public void setOffsetWeekDay(int offsetWeekDay, int offsetWeekDayNth, boolean isAfter) {
        setOffsetWeekDay(offsetWeekDay, offsetWeekDayNth);
        setOffsetDirection(isAfter);
    }

    /**
     * Sets the nth of the weekday offset of the raw {@link Date} (from 1-100).
     * 
     * @param offsetWeekDayNth the nth of the weekday offset (from 1-100)
     */
    public void setOffsetWeekDayNth(int offsetWeekDayNth) {
        if (offsetWeekDayNth < 0 || offsetWeekDayNth > 100) {
            throw new IllegalArgumentException("offsetWeekDayNth must be between 0 and 100");
        }
        this.offsetWeekDayNth = offsetWeekDayNth;
    }

    /**
     * Set the offset direction of this {@link Date}.
     * 
     * @param isAfter true if the offset direction is after, false if it
     *      is before
     */
    public void setOffsetDirection(boolean isAfter) {
        this.isAfter = isAfter;
    }

    /**
     * Sets the list of if weekday.
     * 
     * @param ifWeekdays the list of if weekday
     */
    public void setIfWeekdays(List<List<Integer>> ifWeekdays) {
        this.ifWeekdays = ifWeekdays;
    }
    
    /**
     * Sets the list of alternative time.
     * 
     * @param altTime the list of alternative time
     */
    public void setAlternateTime(List<Integer> altTime) {
        this.altTime = altTime;
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
     * Check if the input date's weekday is in one of the {@link #ifWeekdays}.
     * If yes, the input date's new time will be the corresponding indexed
     * {@link #altTime}'s time.
     * 
     * @param date the date to check
     * @return the input date's new time, if it is in one of the {@link #ifWeekdays}
     */
    private LocalDateTime checkIfWeekday(LocalDateTime date) {
        LocalDateTime result = date;
        for (int i=0; i<ifWeekdays.size(); i++) {
            List<Integer> ifWeekday = ifWeekdays.get(i);
            for (int weekday : ifWeekday) {
                if (result.getDayOfWeek().getValue() == weekday) {
                    LocalTime newTime = Date.minutesToLocalTime(altTime.get(i));
                    return result.toLocalDate().atTime(newTime);
                }
            }
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
        LocalDateTime offsetShifted = offsetShift(raw);
        return checkIfWeekday(offsetShifted);
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
        // throw new IllegalArgumentException(range + "" );
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
