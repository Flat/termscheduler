package com.kennethswenson.termschedule.utils;


import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class Dates {
    public static ZonedDateTime parseTextToZonedDateTime(String dateString){
        return LocalDate.parse(dateString, Formatting.getDateTimeFormat()).atStartOfDay(ZoneId.systemDefault());
    }
}
