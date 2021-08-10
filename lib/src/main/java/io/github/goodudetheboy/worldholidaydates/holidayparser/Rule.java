package io.github.goodudetheboy.worldholidaydates.holidayparser;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.LocalTime;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class Rule implements Comparable<Rule> {
    public static final int UNDEFINED_NUM = Integer.MIN_VALUE;

    // original rule from "days" of holidays.json
    String      originalRule    = null;

    // the raw date, without any offset or time applied
    Date        rawDate         = null;

    // set substitute check during if weekday check
    boolean     substituteCheck = false;

    /**
     * Position of the {@link Rule} in the list of "days"
     */
    int         ruleNum         = UNDEFINED_NUM;
    int         relatedRuleNum  = UNDEFINED_NUM;

    /**
     * The range of this {@link Date} in minutes, the default range is from the
     * startTime to the end of the stored day 
     */
    int         range           = UNDEFINED_NUM; 

    // Gregorian days deviation from raw stored date
    int         offset              = 0;

    // weekday offset deviation from raw stored date
    int         offsetWeekDay       = 0; // 1-7 corresponds to Monday-Sunday
    int         offsetWeekDayNth    = 0; // 1-100th
    
    // Offset before or after raw stored date
    boolean     isAfter     = true;
    
    // another weekday offset (seriously? why?)
    int         offsetWeekDay2 = 0; // 1-7 corresponds to Monday-Sunday
    int         offsetWeekDay2Nth = 0; // 1-100th

    // seondary offset before or after raw stored date
    boolean     isAfter2     = true;

    // Date enabled only in certain year only (if any of below is true)
    boolean     inEvenYear      = false;
    boolean     inOddYear       = false;
    boolean     inLeapYear      = false;
    boolean     inNonLeapYear   = false;

    // Start-time of holiday changes per weekday
    // list of if weekday, from 1-7
    List<List<Integer>>     ifWeekdays      = null; 
    /**
     * List of alternative time, from {@link Date#MIN_TIME} to {@link Date#MAX_TIME}
     * (null if no alternative)
     */
    List<Integer>           altTime         = null;
    /**
     * List of alternative weekday, from 1-7 (null if no alternative)
     * 0 for previous and 1 for next
     */
    List<List<Integer>>     altWeekdays     = null;

    /**
     * If raw Date's weekday is in {@link #ifWeekdaysExtra}, then there will be
     * another holiday of the weekdays of the corresponding index in
     * {@link #extraWeekdays}.
     */
    List<List<Integer>>     ifWeekdaysExtra = null;
    List<List<Integer>>     extraWeekdays   = null;

    /**
     * Weekday enabled/disabled in certain weekday
     */
    List<Integer>           enabledWeekdays  = new ArrayList<>();
    List<Integer>           disabledWeekdays = new ArrayList<>();              

    /**
     * Rule is enabled only for a certain year interval since a certain year.
     * <p>
     * {@link #yearInterval} contains the count of the year interval.
     * <p>
     * {@link #yearStart} contains the start year of the interval.
     */
    List<Integer>           yearIntervals   = new ArrayList<>();
    List<Integer>           yearStarts      = new ArrayList<>();

    /**
     * This {@link Rule} is enabled only if the {@link Date} is on the same day
     * is between {@link #enabledUntil} and {@link #enabledSince}, if defined.
     */
    LocalDate               enabledSince    = null;
    LocalDate               enabledUntil    = null;

    /**
     * This rule is enabled only if the date in this list is a public holiday.
     * The int[] contains the {month, dayOfMonth} of the day that needs to be a 
     * public holiday.
     * <p>
     * For now, this just serves as a placeholder until a good way to factor
     * this into the {@link calculate} is found.
     */
    List<int[]>         enabledIfIsPublicDate  = null;

    /**
     * Empty default constructor
     */
    public Rule() {
        // empty
    }

    @Override
    public int compareTo(Rule r) {
        return originalRule.compareTo(r.getOriginalRule());
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Rule) {
            return originalRule.equals(((Rule) o).getOriginalRule());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return originalRule.hashCode();
    }

    /**
     * @return the orignal rule in "days" of holidays.json
     */
    public String getOriginalRule() {
        return originalRule;
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
     * <p>
     * If the {@link Date} already have a year, it will be used. Otherwise,the
     * default year will be used.
     * 
     * @param defaultYear default Gregorian year
     * @return if this {@link Rule} is a substitute.
     */
    public boolean isSubstitute(int defaultYear) {
        if (!substituteCheck) return false;
        // check in ifWeekdays
        int rawWeekday = calculateRaw(defaultYear).getDayOfWeek().getValue();
        for (List<Integer> ifWeekday : ifWeekdays) {
            if (ifWeekday.contains(rawWeekday)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return the position of this {@link Rule} in the list of "days", from 1-9
     */
    public int getRuleNumber() {
        return ruleNum;
    }

    /**
     * @return the related {@link Rule}'s number (number after #).
     */
    public int getRelatedRuleNumber() {
        return relatedRuleNum;
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
        return inLeapYear;    }

    /**
     * @return true if this {@link Rule} is enabled in non-leap years, false otherwise
     */
    public boolean isInNonLeapYearOnly() {
        return inNonLeapYear;
    }
    
    /**
     * @return the secondary weekday offset of the raw {@link Date} (1-7).
     */
    public int getOffsetWeekDay2() {
        return offsetWeekDay2;
    }

    /**
     * @return the secondary nth of the weekday offset of the raw {@link Date}.
     */
    public int getOffsetWeekDay2Nth() {
        return offsetWeekDay2Nth;
    }

    /**
     * Returns the direction of secondary offset calcuation.
     * 
     * @return true if the offset direction is after, false if it is before
     */
    public boolean getOffsetDirection2() {
        return isAfter2;
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
     * @return the list of enabled weekday, from 1-7
     */
    public List<Integer> getEnabledWeekdays() {
        return enabledWeekdays;
    }

    /**
     * @return the list of disabled weekday, from 1-7
     */
    public List<Integer> getDisabledWeekdays() {
        return disabledWeekdays;
    }

    /**
     * @return the list of year interval
     */
    public List<Integer> getYearIntervals() {
        return yearIntervals;
    }

    /**
     * @return the list of the year which are starts of the intervals
     */
    public List<Integer> getYearIntervalStarts() {
        return yearStarts;
    }

    /**
     * @return the date after which this Rule is enabled
     */
    public LocalDate getEnabledSince() {
        return enabledSince;
    }

    /**
     * @return the date before which this Rule is enabled
     */
    public LocalDate getEnabledUntil() {
        return enabledUntil;
    }

    /**
     * @return the list of {month, dayOfMonth} where this Rule is enabled
     *      only if the date in this list is a public holiday.
     */
    public List<int[]> getEnabledIfIsPublicDateList() {
        return enabledIfIsPublicDate;
    }

    /**
     * Sets the original rule string defining this {@link Rule}.
     * 
     * @param originalRule the original rule string
     */
    public void setOriginalRule(String originalRule) {
        this.originalRule = originalRule;
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
     * @param substituteCheck the substitute check mode of this {@link Rule}
     */
    public void setSubstituteCheck(boolean substituteCheck) {
        this.substituteCheck = substituteCheck;
    }

    /**
     * Sets the position of this {@link Rule} in the list of "days". (from 1-9)
     * 
     * @param ruleNum the position of this {@link Rule} in the list of "days"
     */
    public void setRuleNum(int ruleNum) {
        if (ruleNum < 1 || ruleNum > 9) {
            throw new IllegalArgumentException("Rule number must be between 1 and 9");
        }
        this.ruleNum = ruleNum;
    }

    /**
     * Sets the related rule {@link Rule}'s number (number after #).
     * 
     * @param relatedRuleNum the related rule {@link Rule}'s number
     */
    public void setRelatedRuleNumber(int relatedRuleNum) {
        this.relatedRuleNum = relatedRuleNum;
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
     * Sets the secondary weekday offset (from 1-7) 
     * 
     * @param offsetWeekDay2 the weekday offset (from 1-7)
     */
    public void setOffsetWeekDay2(int offsetWeekDay2) {
        this.offsetWeekDay2 = offsetWeekDay2;
    }

    /**
     * Sets the secondary nth of the weekday offset of the raw {@link Date} (from 1-100).
     * 
     * @param offsetWeekDay2Nth the nth of the weekday offset (from 1-100)
     */
    public void setOffsetWeekDay2Nth(int offsetWeekDay2Nth) {
        this.offsetWeekDay2Nth = offsetWeekDay2Nth;
    }

    /**
     * Set the secondary offset direction of this {@link Date}.
     * 
     * @param isAfter2 true if the offset direction is after, false if it
     *      is before
     */
    public void setOffsetDirection2(boolean isAfter2) {
        this.isAfter2 = isAfter2;
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
     * @param ifWeekdaysExtra the list of if weekday extra
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
     * Sets the enabled weekdays list for this {@link Rule}.
     * 
     * @param enabledWeekdays the enabled weekdays list for this {@link Rule}
     */
    public void setEnabledWeekdays(List<Integer> enabledWeekdays) {
        this.enabledWeekdays = enabledWeekdays;
    }

    /**
     * Sets the disabled weekdays list for this {@link Rule}.
     * 
     * @param disabledWeekdays the disabled weekdays list for this {@link Rule}
     */
    public void setDisabledWeekdays(List<Integer> disabledWeekdays) {
        this.disabledWeekdays = disabledWeekdays;
    }

    /**
     * Sets the year intervals of this {@link Rule}.
     * 
     * @param yearIntervals the year intervals of this {@link Rule}, must be
     *      all positive integers
     */
    public void setYearIntervals(List<Integer> yearIntervals) {
        for (Integer yearInterval : yearIntervals) {
            if (yearInterval <= 0) {
                throw new IllegalArgumentException("Year interval must be a positive integer");
            }
        }
        this.yearIntervals = yearIntervals;
    }

    /**
     * Sets the year starts of the interval.
     * 
     * @param yearStarts the year starts of the interval
     */
    public void setYearIntervalStarts(List<Integer> yearStarts) {
        this.yearStarts = yearStarts;
    }

    /**
     * Sets the date after which this {@link Rule} is enabled.
     * 
     * @param enabledSince since time
     */
    public void setEnabledSince(LocalDate enabledSince) {
        this.enabledSince = enabledSince;
    }

    /**
     * Sets the date before which this {@link Rule} is enabled.
     * 
     * @param enabledUntil prior to time
     */
    public void setEnabledUntil(LocalDate enabledUntil) {
        this.enabledUntil = enabledUntil;
    }

    /**
     * Sets the the list of {month, dayOfMonth} where this Rule is enabled
     * only if the date in this list is a public holiday.
     * 
     * @param enabledIfIsPublicDate the list of {month, dayOfMonth} where this
     *      Rule is enabled only if the date in this list is a public holiday
     */
    public void setEnabledIfIsPublicDateList(List<int[]> enabledIfIsPublicDate) {
        this.enabledIfIsPublicDate = enabledIfIsPublicDate;
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
        if (offsetWeekDay2 != 0) {
            result = Date.getOffsetWeekDayDate(result, offsetWeekDay2, offsetWeekDay2Nth, isAfter2);
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

    /**
     * Checks if there are any requirements in specific type of years in this
     * {@link Rule}.
     */
    private boolean hasYearRequirement() {
        return inEvenYear || inOddYear || inLeapYear || inNonLeapYear;
    }

    /**
     * Checks the input date against the year requirements of this {@link Rule}.
     * The input date will be returned untouched if:
     * <ol>
     * <li>there's no year requirement, or</li>
     * <li>the input date's year is in the required year list</li>
     * </ol>
     * 
     * @param dateTime the date to check
     * @return true if the input date is in the required year, false otherwise
     */
    private LocalDateTime checkYearRequirement(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }

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
     * Checks the input dateTime with the weekday enabled/disabled list.
     * The input dateTime will be returned untouched if:
     * <ol>
     * <li>there's no weekday enabled/disabled list, or</li>
     * <li>the input dateTime's weekday is in the enabled list</li>
     * </ol>
     * 
     * It will return null if:
     * <ol>
     * <li>the input dateTime is null, or</li>
     * <li>the input dateTime's weekday is in the disabled list</li>
     * </ol>
     * 
     * Note that the enabled list takes precedence over the disabled list,
     * which means that if the input dateTime's weekday is in the enabled list,
     * it will return the input dateTime, even if the weekday is in the disabled
     * list.
     * 
     * @param dateTime the date to check
     * @return the input dateTime, if it is in the enabled list, null otherwise
     */
    private LocalDateTime checkWeekdayRequirement(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        
        int weekdayValue = dateTime.getDayOfWeek().getValue();
        if (!enabledWeekdays.isEmpty()) {
            return (enabledWeekdays.contains(weekdayValue)) ? dateTime : null;
        }
        if (!disabledWeekdays.isEmpty()) {
            return (disabledWeekdays.contains(weekdayValue)) ? null : dateTime;
        }
        return dateTime;
    }            
      
    /**
     * Checks the input dateTime if its year falls into the interval year list.
     * <p>
     * The input will be returned untouched if:
     * <ol>
     * <li>there's no interval year list, or</li>
     * <li>the input dateTime's year is in the interval year list</li>
     * </ol>
     * <p>
     * The output will be null if:
     * <ol>
     * <li>the input dateTime is null, or</li>
     * <li>the input dateTime's year is not in the interval year list</li>
     * </ol>
     * 
     * @param dateTime the date to check
     * @return the input dateTime, if its year is in the interval year list,
     *      null otherwise
     */
    private LocalDateTime checkYearInterval(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }

        int year = dateTime.toLocalDate().getYear();
        if (!yearIntervals.isEmpty()) {
            for (int i=0; i<yearIntervals.size(); i++) {
                int yearInterval = yearIntervals.get(i);
                int yearStart = yearStarts.get(i);
                if (year >= yearStart && ((year - yearStart) % yearInterval == 0)) {
                    return dateTime;
                }
            }
            return null;
        }
        return dateTime;
    }

    /**
     * Check if the input dateTime is between the {@link #enabledSince} and
     * {@link #enabledUntil}.
     * <p>
     * The input will be returned untouched if:
     * <ol>
     * <li>there's no enabled range, or</li>
     * <li>the input dateTime is between the enabled range</li>
     * </ol>
     * 
     * @param dateTime the date to check
     * @return the input dateTime, if it is between the enabled range, null
     *      otherwise
     */
    private LocalDateTime checkEnabled(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }

        LocalDate date = dateTime.toLocalDate();
        if (enabledSince == null && enabledUntil == null) {
            return dateTime;
        }
        if (enabledSince != null && enabledUntil == null) {
            return (date.isAfter(enabledSince)) ? dateTime : null;
        }
        if (enabledSince == null /*&& enabledPriorTo != null (always true)*/) {
            return (date.isBefore(enabledUntil)) ? dateTime : null;
        }
        return (date.isAfter(enabledSince) && date.isBefore(enabledUntil)) ? dateTime : null;
    }

    /**
     * Calculates the start Gregorian date and time created from the {@link #rawDate},
     * along with any offset (deviation from raw dates).
     * <p>
     * The returned date will be of {@link LocalDate}. Support for more options,
     * such as Threeten, will be implemented later.
     * <p>
     * If the {@link Date} already have a year, it will be used. Otherwise,the
     * default year will be used.
     * 
     * @param defaultYear default Gregorian year
     * @return the Gregorian date created only from the year, month, day, and
     *      start time without taking any offset into calculation.
     */
    @Nullable
    public LocalDateTime calculate(int defaultYear) {
        LocalDateTime raw = calculateRaw(defaultYear); // get raw date
        LocalDateTime offsetShifted = offsetShift(raw); // apply offset
        LocalDateTime ifCheck = checkIfWeekday(offsetShifted); // check if weekday
        LocalDateTime yearCheck = checkYearRequirement(ifCheck); // check year requirement (even/odd/leap/non-leap year only)
        LocalDateTime weekdayCheck = checkWeekdayRequirement(yearCheck); // check weekday requirement
        LocalDateTime yearIntervalCheck = checkYearInterval(weekdayCheck);
        return checkEnabled(yearIntervalCheck); // check year interval
    }

    /**
     * Calculates extra holidays if the processed {@link #rawDate} (offset,
     * ifWeekdays... applied) satisfies that:
     * <ol>
     * <li> its weekday is in one of the {@link #ifWeekdaysExtra}
     * </ol>
     * <p>
     * If the {@link Date} already have a year, it will be used. Otherwise,the
     * default year will be used.
     * 
     * @param defaultYear default Gregorian year
     * @return the extra holidays if the processed {@link #rawDate} satisfies
     *      the above conditions
     */
    public List<LocalDateTime> calculateExtra(int defaultYear) {
        List<LocalDateTime> result = new ArrayList<>();

        if (ifWeekdaysExtra != null) {
            LocalDate mainDate = calculateDate(defaultYear);
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
     * <p>
     * If the {@link Date} already have a year, it will be used. Otherwise,the
     * default year will be used.
     * 
     * @param defaultYear default Gregorian year
     * @return the Gregorian date created only from the year, month, day,
     *      with offset, if any
     */
    public LocalDateTime calculateEnd(int defaultYear) {
        return calculate(defaultYear).plusMinutes(range);
    }
    
    /**
     * Calculates the start Gregorian date created from the {@link #rawDate},
     * and along with any offset (deviation from raw dates), if any.
     * <p>
     * The returned date will be of {@link LocalDate}. Support for more options,
     * such as Threeten, will be implemented later.
     * <p>
     * If the {@link Date} already have a year, it will be used. Otherwise,the
     * default year will be used.
     * 
     * @param defaultYear default Gregorian year
     * @return the Gregorian date created from the year, month, day stored in
     *      this date, with offset, if any
     */
    public LocalDate calculateDate(int defaultYear) {
        LocalDateTime result = calculate(defaultYear);
        return (result != null) ? calculate(defaultYear).toLocalDate() : null;
    }

    /**
     * Calculates the end Gregorian date created from the {@link #rawDate},
     * shifted by {@link range}, and along with any offset (deviation
     * from raw dates), if any.
     * <p>
     * The returned date will be of {@link LocalDate}. Support for more options,
     * such as Threeten, will be implemented later.
     * <p>
     * If the {@link Date} already have a year, it will be used. Otherwise,the
     * default year will be used.
     * 
     * @param defaultYear default Gregorian year
     * @return the Gregorian date created from the year, month, day stored in
     *      this date, with offset, if any
     */
    public LocalDate calculateDateEnd(int defaultYear) {
        LocalDateTime raw = calculateRawEnd(defaultYear);
        return offsetShift(raw).toLocalDate();
    }

    /**
     * Calculates the start Gregorian date and time of the {@link #rawDate}
     * stored in this {@link Rule}. To get the date with offset, use the
     * {@link #calculate} method instead. To get the end date, use 
     * <p>
     * The returned date will be of {@link LocalDate}. Support for more
     * options, such as Threeten, will be implemented later.
     * <p>
     * If the {@link Date} already have a year, it will be used. Otherwise,the
     * default year will be used.
     * 
     * @param defaultYear default Gregorian year
     * @return the Gregorian date of the {@link #rawDate}
     */
    public LocalDateTime calculateRaw(int defaultYear) {
        return rawDate.calculate(defaultYear);
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
     * <p>
     * If the {@link Date} already have a year, it will be used. Otherwise,the
     * default year will be used.
     * 
     * @param defaultYear default Gregorian year
     * @return the Gregorian date created only from the year, month, day, and
     *      end time without taking any offset into calculation.
     */
    public LocalDateTime calculateRawEnd(int defaultYear) {
        LocalDateTime raw = calculateRaw(defaultYear);
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
     * <p>
     * If the {@link Date} already have a year, it will be used. Otherwise,the
     * default year will be used.
     * 
     * @param defaultYear default Gregorian year
     * @return the Gregorian date created only from the year, month, day,
     *      without taking any offset into calculation.
     */

    public LocalDate calculateRawDate(int defaultYear) {
        return rawDate.calculateDate(defaultYear);
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
     * <p>
     * If the {@link Date} already have a year, it will be used. Otherwise,the
     * default year will be used.
     * 
     * @param defaultYear default Gregorian year
     * @return the Gregorian date created only from the year, month, day,
     *      without taking any offset into calculation.
     */
    public LocalDate calculateRawDateEnd(int defaultYear) {
        return calculateRawEnd(defaultYear).toLocalDate();
    }
}
