package worldholidaydates.holidayparser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class Rule {
    public static final int UNDEFINED_NUM = Integer.MIN_VALUE;
    // the raw date, without any offset or time applied
    Date        rawDate         = null;

    // set substitute check during if weekday check
    boolean     substituteCheck = false;

    // the range of this {@link Date} in minutes, the default range is from the
    // startTime to the end of the stored day
    int         range           = UNDEFINED_NUM; 

    // Gregorian days deviation from raw stored date
    int         offset              = 0;

    // weekday offset deviation from raw stored date
    int         offsetWeekDay       = 0; // 1-7 corresponds to Monday-Sunday
    int         offsetWeekDayNth    = 0; // 1-100th
    
    // Offset before or after raw stored date
    boolean     isAfter     = true;

    // Date enabled only in certain year only (if any of below is true)
    boolean     inEvenYear      = false;
    boolean     inOddYear       = false;
    boolean     inLeapYear      = false;
    boolean     inNonLeapYear   = false;

    // Start-time of holiday changes per weekday
    // list of if weekday, from 1-7
    List<List<Integer>>     ifWeekdays      = null; 
    // list of alternative time, from {@link Date#MIN_TIME} to {@link Date#MAX_TIME}
    //(null if no alternative)
    List<Integer>           altTime         = null;
    // list of alternative weekday, from 1-7 (null if no alternative)
    // 0 for previous and 1 for next
    List<List<Integer>>     altWeekdays     = null;

    List<List<Integer>>     ifWeekdaysExtra = null;
    List<List<Integer>>     extraWeekdays   = null;

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
     * @return if this {@link Rule}'s substitute check mode is on.
     */
    public boolean getSubstituteCheck() {
        return substituteCheck;
    }

    /**
     * Check if this {@link Date} is a sustitute. It will be a subtitute if, the 
     * {@link #substituteCheck} mode is true, and:
     * <ol>
     * <li> the raw {@link Date}'s weekday falls into one of {@link ifWeekdays} list
     * </ol>
     * 
     * @return if this {@link Rule} is a substitute.
     */
    public boolean isSubstitute() {
        if (!substituteCheck) return false;
        // check in ifWeekdays
        int rawWeekday = calculateRaw().getDayOfWeek().getValue();
        for (List<Integer> ifWeekday : ifWeekdays) {
            if (ifWeekday.contains(rawWeekday)) {
                return true;
            }
        }
        return false;
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
     * @return true if this {@link Rule} is enabled in even years, false otherwise
     */
    public boolean isInEvenYearOnly() {
        return inEvenYear;
    }

    /**
     * @return true if this {@link Rule} is enabled in odd years, false otherwise
     */
    public boolean isInOddYearOnly() {
        return inOddYear;
    }

    /**
     * @return true if this {@link Rule} is enabled in leap years, false otherwise
     */
    public boolean isInLeapYearOnly() {
        return inLeapYear;
    }

    /**
     * @return true if this {@link Rule} is enabled in non-leap years, false otherwise
     */
    public boolean isInNonLeapYearOnly() {
        return inNonLeapYear;
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
     * @return the list of alternative weekday, from 1-7 (null if no alternative)
     *      as the first index, and 0 for previous and 1 for next as the second
     *      index
     */
    public List<List<Integer>> getAlternateWeekdays() {
        return altWeekdays;
    }

    /**
     * @return the list of if weekday extra, from 1-7
     */
    public List<List<Integer>> getIfWeekdaysExtra() {
        return ifWeekdaysExtra;
    }
    
    /**
     * @return the list of weekday extra, from 1-7 (null if no alternative) as
     *      the first index, and 0 for previous and 1 for next as the second
     *      index
     */
    public List<List<Integer>> getExtraWeekdays() {
        return extraWeekdays;
    }

    /**
     * Sets the {@link Date} to be used in the {@link Rule}.
     * 
     * @param rawDate the {@link Date} to be used in the {@link Rule}
     * 
     */
    public void setRawDate(Date rawDate) {
        this.rawDate = rawDate;
    }

    /**
     * Sets the substitute check mode of this {@link Rule}.
     * 
     * @param isSubstitute the substitute check mode of this {@link Rule}
     */
    public void setSubstituteCheck(boolean substituteCheck) {
        this.substituteCheck = substituteCheck;
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
     * Sets this {@link Rule} to be enabled only in even year.
     * 
     * @param inEvenYear true if this {@link Rule} is enabled only in even year
     */
    public void setInEvenYearOnly(boolean inEvenYear) {
        this.inEvenYear = inEvenYear;
    }

    /**
     * Sets this {@link Rule} to be enabled only in odd year.
     * 
     * @param inOddYear true if this {@link Rule} is enabled only in odd year
     */
    public void setInOddYearOnly(boolean inOddYear) {
        this.inOddYear = inOddYear;
    }

    /**
     * Sets this {@link Rule} to be enabled only in leap year.
     * 
     * @param inLeapYear true if this {@link Rule} is enabled only in leap year
     */
    public void setInLeapYearOnly(boolean inLeapYear) {
        this.inLeapYear = inLeapYear;
    }

    /**
     * Sets this {@link Rule} to be enabled only in non-leap year.
     * 
     * @param inNonLeapYear true if this {@link Rule} is enabled only in non-leap year
     */
    public void setInNonLeapYearOnly(boolean inNonLeapYear) {
        this.inNonLeapYear = inNonLeapYear;
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
     * Sets the list of alternative weekday. Each of the list is a list of
     * two integers. The first integer is the index of the weekday, the second
     * is either 0 for previous and 1 for next.
     * 
     * @param altWeekdays the list of alternative weekday
     */
    public void setAlternateWeekdays(List<List<Integer>> altWeekdays) {
        this.altWeekdays = altWeekdays;
    }

    /**
     * Sets the list of if weekday extra
     * 
     * @param ifWeekdays the list of if weekday extra
     */
    public void setIfWeekdaysExtra(List<List<Integer>> ifWeekdaysExtra) {
        this.ifWeekdaysExtra = ifWeekdaysExtra;
    }

    /**
     * Sets the list of extra weekday. Each of the list is a list oftwo integers.
     * The first integer is the index of the weekday, the second is either 0 for
     * previous and 1 for next.
     * 
     * @param extraWeekdays the list of alternative weekday
     */
    public void setExtraWeekdays(List<List<Integer>> extraWeekdays) {
        this.extraWeekdays = extraWeekdays;
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
     * {@link #altTime}'s time, or if there's no alternative, then will look
     * for alternate weekday.
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
                    Integer aTime = altTime.get(i);
                    List<Integer> altWeekday = altWeekdays.get(i);
                    if (aTime != null) {
                        LocalTime newTime = Date.minutesToLocalTime(aTime);
                        return result.toLocalDate().atTime(newTime);
                    } else if (altWeekday != null) {
                        int altWeekdayVal = altWeekday.get(0);
                        boolean isNext = (altWeekday.get(1) == 1);
                        LocalDate newDate = Date.getNextOrPreviousWeekday(result.toLocalDate(), altWeekdayVal, isNext);
                        LocalTime time = result.toLocalTime();
                        return newDate.atTime(time);
                    }
                }
            }
        }
        return result;
    }

    private boolean hasYearRequirement() {
        return inEvenYear || inOddYear || inLeapYear || inNonLeapYear;
    }

    private LocalDateTime checkYearRequirement(LocalDateTime dateTime) {
        LocalDate date = dateTime.toLocalDate();
        int year = date.getYear();
        if (!hasYearRequirement()
        ||  inEvenYear && (year % 2 == 0)
        ||  inOddYear && (year % 2 == 1)
        ||  inLeapYear && date.isLeapYear()
        ||  inNonLeapYear && !date.isLeapYear()) {
            return dateTime;
        }
        return null;
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
    @Nullable
    public LocalDateTime calculate() {
        LocalDateTime raw = calculateRaw(); // get raw date
        LocalDateTime offsetShifted = offsetShift(raw); // apply offset
        LocalDateTime weekdayCheck = checkIfWeekday(offsetShifted); // check if weekday
        return checkYearRequirement(weekdayCheck); // check year requirement (even/odd/leap/non-leap year only)
    }

    /**
     * Calculates extra holidays if the processed {@link #rawDate} (offset,
     * ifWeekdays... applied) satisfies that:
     * <ol>
     * <li> its weekday is in one of the {@link #ifWeekdaysExtra}
     * </ol>
     * 
     * @return the extra holidays if the processed {@link #rawDate} satisfies
     *      the above conditions
     */
    public List<LocalDateTime> calculateExtra() {
        List<LocalDateTime> result = new ArrayList<>();

        if (ifWeekdaysExtra != null) {
            LocalDate mainDate = calculateDate();
            int weekdayValue = mainDate.getDayOfWeek().getValue();
            for (int i=0; i<ifWeekdaysExtra.size(); i++) {
                List<Integer> ifWeekdayExtra = ifWeekdaysExtra.get(i);
                if (ifWeekdayExtra.contains(weekdayValue)) {
                    List<Integer> extraWeekday = extraWeekdays.get(i);
                    int eWeekdayVal = extraWeekday.get(0);
                    boolean isNext = (extraWeekday.get(1) == 1);
                    LocalDate extraDate = Date.getNextOrPreviousWeekday(mainDate, eWeekdayVal, isNext);
                    result.add(extraDate.atStartOfDay());
                }
            }
        }
        
        return result;
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
        LocalDateTime result = calculate();
        return (result != null) ? calculate().toLocalDate() : null;
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
