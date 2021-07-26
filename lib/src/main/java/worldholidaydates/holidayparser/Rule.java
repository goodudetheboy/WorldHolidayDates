package worldholidaydates.holidayparser;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Rule {
    private Date mainDate = null;

    public Rule() {
        // empty
    }

    public void setMainDate(Date mainDate) {
        this.mainDate = mainDate;
    }

    public LocalDateTime calculate() {
        return mainDate.calculate();
    }

    public LocalDate calculateDate() {
        return mainDate.calculateDate();
    }

}
