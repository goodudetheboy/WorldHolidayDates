# **WorldHolidayDates** #

[![build status](https://github.com/goodudetheboy/WorldHolidayDates/actions/workflows/gradle.yml/badge.svg)](https://github.com/goodudetheboy/WorldHolidayDates/actions)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=goodudetheboy_WorldHolidayDates&metric=alert_status)](https://sonarcloud.io/dashboard?id=goodudetheboy_WorldHolidayDates)
[![sonarcloud bugs](https://sonarcloud.io/api/project_badges/measure?project=goodudetheboy_WorldHolidayDates&metric=bugs)](https://sonarcloud.io/component_measures?id=goodudetheboy_WorldHolidayDates&metric=bugs)
[![sonarcould maintainability](https://sonarcloud.io/api/project_badges/measure?project=goodudetheboy_WorldHolidayDates&metric=sqale_rating)](https://sonarcloud.io/component_measures?id=goodudetheboy_WorldHolidayDates&metric=Maintainability)
[![sonarcloud security](https://sonarcloud.io/api/project_badges/measure?project=goodudetheboy_WorldHolidayDates&metric=security_rating)](https://sonarcloud.io/component_measures?id=goodudetheboy_WorldHolidayDates&metric=Security)
[![sonarcloud reliability](https://sonarcloud.io/api/project_badges/measure?project=goodudetheboy_WorldHolidayDates&metric=reliability_rating)](https://sonarcloud.io/component_measures?id=goodudetheboy_WorldHolidayDates&metric=Reliability)

This project provides the dates and times of holidays around the world, specifically 168 countries. The data is taken from the processed `holidays.json` from [date-holidays](https://github.com/commenthol/date-holidays).

## Features ##

This holiday data supports holidays from 168 countries, with many other containing states and regions. Many calendar systems are also calculated, including:

- Gregorian (Easter incl.)
- Julian (Orthodox Easter incl.)
- Equinox/Solstice
- East Asian (Chinese, Korean, Vietnamese)
- Hebrew
- Hijra
- Bengali revised

The date definition itself features complex grammars that supports accurate exact date of the holiday of that countries, no matter the calendar system used.

Supports public, bank, school, optional, and observance holidays.

## Installation ##

This project is published on Maven Central. Add the following snippets of codes to your `build.gradle` find to install it:

```
repositories {
    mavenCentral()
}
```

```
dependencies {
    implementation 'io.github.goodudetheboy:WorldHolidayDates:<LATEST-VERSION>'
}
```

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

## Tests ##

The `holidayparser` that parses the [grammar](https://github.com/commenthol/date-holidays/blob/master/docs/specification.md) specified by the [date-holidays](https://github.com/commenthol/date-holidays) parses 100% (3350 out of 3350) of the date strings in the holiday data. There are plenty more unit tests to keep this parser in check.

Despite 100% succesful parse rate, expect bugs to happen in the holiday data. Were that to happen, any PRs to fix the current parser are very welcomed. Read [Contribution](#Contribution) for more.

## Contribution ##

Some of the calculation for some calendar systems are still incorrect, along with some missing features (TBU).

Any interests in contributing to the holiday database should be made there, since this project is just a Java implementation to get data from it.
