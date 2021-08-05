package worldholidaydates.holidaydata;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import worldholidaydates.holidayparser.Rule;

public class Country {
    private     Map<String, String>      names      = null;
    private     String      name        = null;
    private     String      dayoff      = null;
    private     String[]    langs       = null;
    private     String[]    zones       = null;
    private     String[]    refDays     = null;
    private     Map<Rule, Object>        days       = null;
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

    public void setReferenceDays(String[] refDays) {
        this.refDays = refDays;
    }

    public void setDays(Map<Rule, Object> days) {
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

    public Map<Rule, Object> getDays() {
        return days;
    }

    public Map<String, Country> getStates() {
        return states;
    }

    public Map<String, Country> getRegions() {
        return regions;
    }

    /**
     * Returns the list of all dates and times of holidays of this country,
     * excluding the holidays from regions and states.
     * 
     * @param defaultYear the year that the holiday will be in, if the holiday
     *      does not have a definite year.
     * @return a list of dates and times of holidays
     */
    public List<LocalDateTime> getHolidays(int defaultYear) {
        List<LocalDateTime> result = new ArrayList<>();
        for (Map.Entry<Rule, Object> entry : days.entrySet()) {
            if (entry.getValue() instanceof Holiday) {
                result.add(entry.getKey().calculate(defaultYear));
            }
        }
        return result;
    }

    /**
     * Returns the list of all dates of holidays of this country, excluding
     * the holidays from regions and states.
     * 
     * @param defaultYear the year that the holiday will be in, if the holiday
     *      does not have a definite year.
     * @return a list of dates of holidays
     */
    public List<LocalDate> getHolidayDates(int defaultYear) {
        List<LocalDate> result = new ArrayList<>();
        for (Map.Entry<Rule, Object> entry : days.entrySet()) {
            if (entry.getValue() instanceof Holiday) {
                result.add(entry.getKey().calculateDate(defaultYear));
            }
        }
        return result;
    }
}
