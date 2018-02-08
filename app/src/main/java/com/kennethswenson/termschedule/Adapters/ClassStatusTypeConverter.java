package com.kennethswenson.termschedule.Adapters;

import android.arch.persistence.room.TypeConverter;

import com.kennethswenson.termschedule.Models.ClassStatus;


public class ClassStatusTypeConverter {

    @TypeConverter
    public static ClassStatus toClassStatus(int enumnum){
        switch (enumnum){
            case 0:
                return ClassStatus.INPROGRESS;
            case 1:
                return ClassStatus.COMPLETE;
            case 2:
                return ClassStatus.DROPPED;
            case 3:
                return ClassStatus.PLANNED;
            default:
                throw new IllegalArgumentException("Invalid enum integer");
        }
    }

    @TypeConverter
    public static int toInteger(ClassStatus classStatus){
        return classStatus.getEnumnum();
    }
}
