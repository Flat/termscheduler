package com.kennethswenson.termschedule.Adapters;

import android.arch.persistence.room.TypeConverter;

import com.kennethswenson.termschedule.Models.AssessmentType;

public class AssessmentTypeConverter {

    @TypeConverter
    public static AssessmentType toAssessmentType(int enumnum){
        if(enumnum == AssessmentType.OBJECTIVE.getEnumnum()){
            return AssessmentType.OBJECTIVE;
        } else if (enumnum == AssessmentType.PERFORMANCE.getEnumnum()){
            return AssessmentType.PERFORMANCE;
        } else {
            throw new IllegalArgumentException("Not a valid enum");
        }
    }

    @TypeConverter
    public static int toInteger(AssessmentType assessmentType){
        return assessmentType.getEnumnum();
    }
}
