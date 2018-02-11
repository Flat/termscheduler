package com.kennethswenson.termschedule.Models;

public enum AssessmentType {
    OBJECTIVE(0),
    PERFORMANCE(1),;

    private int enumnum;

    AssessmentType(int enumnum) {
        this.enumnum = enumnum;
    }

    public int getEnumnum() {
        return enumnum;
    }

    @Override
    public String toString() {
        if (enumnum == 0){
            return "Objective";
        } else {
            return "Performance";
        }
    }
}
