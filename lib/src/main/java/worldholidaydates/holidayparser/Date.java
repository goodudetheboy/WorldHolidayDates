package worldholidaydates.holidayparser;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface Date {
    public LocalDateTime calculate();

    public LocalDate calculateDate();
}
