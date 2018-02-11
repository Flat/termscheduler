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

    @Override
    public String toString() {
        switch(enumnum){
            case 0:
                return "In Progress";
            case 1:
                return "Completed";
            case 2:
                return "Dropped";
            case 3:
                return "Planned";
            default:
                return "Error reading class status";
        }
    }
}
