package com.kennethswenson.termschedule.utils;


import java.time.format.DateTimeFormatter;

public class Formatting {
    public static DateTimeFormatter getDateTimeFormat(){
        return DateTimeFormatter.ofPattern("eee L dd, yyyy");
    }
}
