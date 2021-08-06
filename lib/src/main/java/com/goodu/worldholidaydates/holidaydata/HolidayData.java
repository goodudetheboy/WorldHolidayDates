package com.goodu.worldholidaydates.holidaydata;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import com.goodu.worldholidaydates.Utils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class HolidayData {
    private     String      version     = null;
    private     String      license     = null;
    private     Map<String, Country>    holidays = null;

    public HolidayData() {
        // empty constructor
    }

    public void setVersion(String version) {
        this.version = version;
    }
    public void setLicense(String license) {
        this.license = license;
    }
    public void setHolidays(Map<String, Country> holidays) {
        this.holidays = holidays;
    }

    public String getVersion() {
        return version;
    }

    public String getLicense() {
        return license;
    }

    public Map<String, Country> getHolidays() {
        return holidays;
    }

    public Country getCountry(String countryCode) {
        return holidays.get(countryCode);
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append("version: ").append(version).append("\n");
        b.append("license: ").append(license).append("\n");
        b.append("holidays: ").append("\n");
        for (Map.Entry<String, Country> entry : holidays.entrySet()) {
            b.append(entry.getKey()).append(" ").append(entry.getValue()).append("\n");
        }
        return b.toString();
    }

    public static HolidayData initializeData() {
        String path = System.getProperty("user.dir") + "/src/main/resources/holidays.json";
        String holidayDataString;
        try {
            holidayDataString = Utils.readFile(path, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new HolidayInitializationException(e.getMessage(), e.getCause());
        }
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Country.class, new CountryDeserializer());
        gsonBuilder.registerTypeAdapter(Holiday.class, new HolidayDeserializer());
        Gson gson = gsonBuilder.create();
        return gson.fromJson(holidayDataString, HolidayData.class);
    }

    public static HolidayData initalizeRawData() {
        String path = System.getProperty("user.dir") + "/src/main/resources/holidays.json";
        String holidayDataString;
        try {
            holidayDataString = Utils.readFile(path, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new HolidayInitializationException(e.getMessage(), e.getCause());
        }
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Country.class, new CountryRawDeserializer());
        gsonBuilder.registerTypeAdapter(Holiday.class, new HolidayDeserializer());
        Gson gson = gsonBuilder.create();
        return gson.fromJson(holidayDataString, HolidayData.class);
    }
}
