package com.kennethswenson.termschedule.Models;


public enum ClassStatus {
    INPROGRESS(0),
    COMPLETE(1),
    DROPPED(2),
    PLANNED(3);

    private int enumnum;

    ClassStatus(int enumnum) {
        this.enumnum = enumnum;
    }

    public int getEnumnum() {
        return enumnum;
    }
}
