package worldholidaydates.holidayparser;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface Date {
    public LocalDateTime calculate();

    public LocalDate calculateDate();

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
}
