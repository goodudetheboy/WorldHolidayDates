package io.github.goodudetheboy.worldholidaydates.holidayparser;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.LocalTime;
import org.threeten.bp.ZonedDateTime;

import io.github.goodudetheboy.worldholidaydates.holidayparser.GregorianDate.GregorianMonth;

/**
 * A class for calculating the date and time of astronomical events, on which
 * a holiday can be based.
 */
public abstract class AstronomicalDate extends Date {

    /**
     * Set the restricted month that is accepted by the subclass.
     * 
     * @param month input month value (from 1-12)
     * @return true if input month is accepted for calculation for astronomical
     *      event
     */
    public abstract boolean isAcceptedMonth(int month);

    @Override
    public void setMonth(int month) {
        if (isAcceptedMonth(month)) {
            super.setMonth(month);
            super.setNamedMonth(GregorianMonth.fromValue(month));
        } else {
            throw new IllegalArgumentException("Invalid month for" + this.getClass() + ": " + month);
        }
    }

    @Override
    public void setNamedMonth(NamedMonth namedMonth) {
        if (namedMonth instanceof GregorianMonth) {
            setMonth(((GregorianMonth) namedMonth).getValue());
        } else {
            throw new IllegalArgumentException("NamedMonth must be a GregorianMonth");
        }
    }

    @Override
    public GregorianMonth getNamedMonth() {
        return (GregorianMonth) namedMonth;        
    }

    @Override
    public LocalDateTime calculate(int defaultYear) {
        int yearToUse = (year != UNDEFINED_NUM) ? year : defaultYear;
        ZonedDateTime defaultResult = calculateAstronomicalDate(yearToUse);
        ZonedDateTime shiftedResult = defaultResult.withZoneSameInstant(timezone);
        if (startTime != UNDEFINED_NUM) {
            LocalDate rawDate = shiftedResult.toLocalDate();
            LocalTime rawTime = minutesToLocalTime(startTime);
            return rawDate.atTime(rawTime);
        } else {
            return shiftedResult.toLocalDateTime();
        }
    }

    @Override
    public LocalDate calculateDate(int defaultYear) {
        int yearToUse = (year != UNDEFINED_NUM) ? year : defaultYear;
        ZonedDateTime defaultResult = calculateAstronomicalDate(yearToUse);
        ZonedDateTime shiftedResult = defaultResult.withZoneSameInstant(timezone);
        return shiftedResult.toLocalDate();
    }

    protected abstract ZonedDateTime calculateAstronomicalDate(int defaultYear);

    @Override
    public abstract String toNamedString();
}
