package com.kennethswenson.termschedule.Adapters;


import android.arch.persistence.room.TypeConverter;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.TimeZone;

public class ZonedDateTimeTypeConverter {

    @TypeConverter
    public static Long toLong(ZonedDateTime zonedDateTime){
        return zonedDateTime.toInstant().toEpochMilli();
    }

    @TypeConverter
    public static ZonedDateTime toZonedDateTime(Long epochMiliSeconds){
        return ZonedDateTime.ofInstant(Instant.ofEpochMilli(epochMiliSeconds), TimeZone.getDefault().toZoneId());
    }
}
