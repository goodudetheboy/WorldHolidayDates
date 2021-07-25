package worldholidaydates.holidaydata;

import java.util.Map;

import worldholidaydates.Utils;

public class Holiday {
    Map<String, String>     name = null;
    // the "_name" section
    private     String      refName     = null;
    private     String      type        = null;
    private     String      note        = null;
    private     Boolean     substitute  = null;
    private     String[]    disable     = null;
    private     String[]    enable      = null;

    public Holiday() {
        // empty
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
