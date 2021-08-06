# **WorldHolidayDates** #

[![build status](https://github.com/goodudetheboy/WorldHolidayDates/actions/workflows/gradle.yml/badge.svg)](https://github.com/goodudetheboy/OpeningHoursEvaluator/actions)

This project provides the dates and times of holidays around the world, specifically 168 countries. The data is taken from the processed `holidays.json` from [date-holidays](https://github.com/commenthol/date-holidays). Any interests in contributing to the holiday database should be made there, since this project is just a Java implementation to get data from it.

This project is still under construction.

## Usage ##

Initialize the holiday data as soon as possible in your project:

```java
HolidayData holidayData = HolidayData.initializeData();
```

Get a `Country` which contains its national holidays along with the holidays of its regions and states using its 2-character country code (you can look up the code for specific countries [here](https://www.iban.com/country-codes)):

```java
Country vietnam = holidayData.getCountry("VN");
```

To get holiday data from a `Country` object, use:

```java
List<Holiday> holidays = vietnam.getDays();
```

To calculate the date and name (in English) of a `Holiday` in a certain `year`, use:

```java
Holiday h = holidays.get(0);
LocalDate date = h.calculateDate(2021); // or whatever Gregorian year you want
LocalDateTime dateTime = h.calculate(2021); // or whatever Gregorian year you want
String hNameInEnglish = h.getName().get("en");
```

To get a list of `LocalDate` or `LocalDateTime` of holiday in a certain `year`, use:

```java
List<LocalDateTime> holidaysList = vietnam.getHolidaysList(2021);
List<LocalDate> holidays = vietnam.getHolidaysDatesList(2021);
```


## Building ##

The project uses gradle for building. Standard gradle tasks for the java plugin can be found [here](https://docs.gradle.org/current/userguide/java_plugin.html). They can be invoked on the command line by running `gradlew` or `gradlew.bat` with the name of the task, for example `gradlew jar` to create the jar archive.

## Testing ##

TBU
