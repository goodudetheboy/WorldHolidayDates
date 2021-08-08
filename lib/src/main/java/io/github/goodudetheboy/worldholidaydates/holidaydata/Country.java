package io.github.goodudetheboy.worldholidaydates.holidaydata;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import io.github.goodudetheboy.worldholidaydates.holidayparser.Rule;

/**
 * A class to represent a country, containing its metadata, such as name,
 * language, day off... along with its national holidays and its containing
 * states and regions.
 */
public class Country {
    private     Map<String, String>      names      = null;
    private     String      name        = null;
    private     String      dayoff      = null;
    private     String[]    langs       = null;
    private     String[]    zones       = null;
    private     String[]    refDays     = null;
    private     List<Holiday>           days       = null;
    private     Map<Rule, Object>       rawDays    = null;
    private     Map<String, Country>    states     = null;
    private     Map<String, Country>    regions    = null;

    /**
     * Default constructor
     */
    public Country() {
        // empty
    }

    /**
     * @return name of this {@link Country} in English
     */
    public String getName() {
        return name;
    }

    /**
     * @return the day off of this {@link Country}
     */
    public String getDayoff() {
        return dayoff;
    }

    /**
     * @return the languages used by this {@link Country}
     */
    public String[] getLangs() {
        return langs;
    }

    /**
     * @return the timezones used by this {@link Country}
     */
    public String[] getZones() {
        return zones;
    }

    /**
     * @return reference days
     */
    public String[] getReferenceDays() {
        return refDays;
    }

    /**
     * @return the national holidays of this {@link Country}
     */
    public List<Holiday> getDays() {
        return days;
    }

    /**
     * @return the raw data of the national holidays of this {@link Country},
     *      useful during debugging
     */
    public Map<Rule, Object> getRawDays() {
        return rawDays;
    }

    /**
     * @return the states of this {@link Country}
     */
    public Map<String, Country> getStates() {
        return states;
    }

    /**
     * @return the regions of this {@link Country}
     */
    public Map<String, Country> getRegions() {
        return regions;
    }

    /**
     * @return the names of this {@link Country} by language
     */
    public Map<String, String> getNames() {
        return names;
    }

    /**
     * Gets the name of this holiday in a given language.
     * 
     * @param lang the language
     * @return the name of this holiday in the given language
     */
    public String getNameByLang(String lang) {
        if (names == null) {
            return null;
        }
        return names.get(lang);
    }

    /**
     * Sets the names of this {@link Country} by language.
     * 
     * @param names the names of this {@link Country} by language
     */
    public void setNames(Map<String, String> names) {
        this.names = names;
    }

    /**
     * Sets the name of this {@link Country} in English.
     * 
     * @param name the name of this {@link Country} in English
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the day off of this {@link Country}.
     * 
     * @param dayoff the day off of this {@link Country}
     */
    public void setDayoff(String dayoff) {
        this.dayoff = dayoff;
    }

    /**
     * Sets the languages used by this {@link Country}.
     * 
     * @param langs the languages used by this {@link Country}
     */
    public void setLangs(String[] langs) {
        this.langs = langs;
    }

    /**
     * Sets the timezones used by this {@link Country}.
     * 
     * @param zones the timezones used by this {@link Country}
     */
    public void setZones(String[] zones) {
        this.zones = zones;
    }

    /**
     * Sets the reference days.
     * 
     * @param refDays the reference days
     */
    public void setReferenceDays(String[] refDays) {
        this.refDays = refDays;
    }

    /**
     * Sets the national holidays of this {@link Country}.
     * 
     * @param days the national holidays of this {@link Country}
     */
    public void setDays(List<Holiday> days) {
        this.days = days;
    }

    /**
     * Sets the raw data of the national holidays of this {@link Country},
     * 
     * @param rawDays the raw data of the national holidays of this {@link Country}
     */
    public void setRawDays(Map<Rule, Object> rawDays) {
        this.rawDays = rawDays;
    }

    /**
     * Sets the states of this {@link Country}.
     * 
     * @param states the states of this {@link Country}
     */
    public void setStates(Map<String, Country> states) {
        this.states = states;
    }

    /**
     * Sets the regions of this {@link Country}.
     * 
     * @param regions the regions of this {@link Country}
     */
    public void setRegions(Map<String, Country> regions) {
        this.regions = regions;
    }

    /**
     * Gets the state of this {@link Country} by its shortened name.
     * 
     * @param name the shortened name of the state
     * @return the state of this {@link Country}
     */
    @Nullable
    public Country getStateByName(String name) {
        if (states != null) {
            return states.get(name);
        }
        return null;                
    }

    /**
     * Gets the region of this {@link Country} by its shortened name.
     * 
     * @param name the shortened name of the region
     * @return the state of this {@link Country}
     */
    @Nullable
    public Country getRegionByName(String name) {
        if (regions != null) {
            return regions.get(name);
        }
        return null;
    }

    /**
     * Gets the subregion of this {@link Country} by name. The subregion can be
     * either a state or a regions. The subRegionName will be checked
     * against the states and the regions.
     * 
     * @param subRegionName the name of the subregion
     * @return the subregion of this {@link Country} by name, or null none found
     */
    @Nullable
    public Country getSubRegion(String subRegionName) {
        Country state = getStateByName(subRegionName);
        if (state != null) {
            return state;
        }
        Country region = getRegionByName(subRegionName);
        if (region != null) {
            return region;
        }
        return null;
    }

    /**
     * Returns the list of all dates and times of holidays of this country,
     * excluding the holidays from regions and states.
     * 
     * @param defaultYear the year that the holiday will be in, if the holiday
     *      does not have a definite year.
     * @return a list of dates and times of holidays
     */
    public List<LocalDateTime> getHolidaysList(int defaultYear) {
        List<LocalDateTime> result = new ArrayList<>();
        for (Holiday holiday : days) {
            result.add(holiday.calculate(defaultYear));
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
    public List<LocalDate> getHolidayDatesList(int defaultYear) {
        List<LocalDate> result = new ArrayList<>();
        for (Holiday holiday : days) {
            result.add(holiday.calculateDate(defaultYear));
        }
        return result;
    }
}
