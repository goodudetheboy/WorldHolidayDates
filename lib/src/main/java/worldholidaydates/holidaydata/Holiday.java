package worldholidaydates.holidaydata;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

import worldholidaydates.Utils;
import worldholidaydates.holidayparser.Rule;

public class Holiday {
    Rule        rule        = null;
    Map<String, String>     name = null;
    // the "_name" section
    String      refName     = null;
    String      type        = null;
    String      note        = null;
    Boolean     substitute  = null;
    String[]    disable     = null;
    String[]    enable      = null;

    public Holiday() {
        // empty
    }

    public Rule getRule() {
        return rule;
    }

    public Map<String, String> getName() {
        return name;
    }

    public String getRefName() {
        return refName;
    }

    public String getType() {
        return type;
    }

    public String getNote() {
        return note;
    }

    public Boolean getSubstitute() {
        return substitute;
    }

    public String[] getDisable() {
        return disable;
    }

    public String[] getEnable() {
        return enable;
    }

    public void setRule(Rule rule) {
        this.rule = rule;
    }

    // setter for all
    public void setName(Map<String, String> name) {
        this.name = name;
    }

    public void setReferenceName(String refName) {
        this.refName = refName;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setSubstitute(Boolean substitute) {
        this.substitute = substitute;
    }

    public void setDisable(String[] disable) {
        this.disable = disable;
    }

    public void setEnable(String[] enable) {
        this.enable = enable;
    }

    public LocalDateTime calculate(int defaultYear) {
        return rule.calculate(defaultYear);
    }

    public LocalDate calculateDate(int defaultYear) {
        return rule.calculateDate(defaultYear);
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append("name: ").append(name).append(Utils.LINE_SEPARATOR);
        b.append("_name: ").append(refName).append(Utils.LINE_SEPARATOR);
        b.append("type: ").append(type).append(Utils.LINE_SEPARATOR);
        b.append("note: ").append(note).append(Utils.LINE_SEPARATOR);
        b.append("substitute: ").append(substitute).append(Utils.LINE_SEPARATOR);
        b.append("disable: ").append(disable).append(Utils.LINE_SEPARATOR);
        b.append("enable: ").append(enable).append(Utils.LINE_SEPARATOR);
        return b.toString();
    }
}
