package worldholidaydates;

import java.util.Map;

public class Country {
    private     Map<String, String>      names      = null;
    private     String      name        = null;
    private     String      dayoff      = null;
    private     String[]    langs       = null;
    private     String[]    zones       = null;
    private     String[]    refDays     = null;
    private     Object      days        = null;
    private     Map<String, Country>     states     = null;
    private     Map<String, Country>     regions    = null;

    public Country() {
        // empty
    }

    public void setNames(Map<String, String> names) {
        this.names = names;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDayoff(String dayoff) {
        this.dayoff = dayoff;
    }

    public void setLangs(String[] langs) {
        this.langs = langs;
    }
    public void setZones(String[] zones) {
        this.zones = zones;
    }

    public void set_days(String[] _days) {
        this.refDays = _days;
    }

    public void setDays(Object days) {
        this.days = days;
    }

    public void setStates(Map<String, Country> states) {
        this.states = states;
    }

    public void setRegions(Map<String, Country> regions) {
        this.regions = regions;
    }

    public Map<String, String> getNames() {
        return names;
    }

    public String getName() {
        return name;
    }

    public String getDayoff() {
        return dayoff;
    }

    public String[] getLangs() {
        return langs;
    }

    public String[] getZones() {
        return zones;
    }

    public String[] getReferenceDays() {
        return refDays;
    }

    public Object getDays() {
        return days;
    }

    public Map<String, Country> getStates() {
        return states;
    }

    public Map<String, Country> getRegions() {
        return regions;
    }
}
