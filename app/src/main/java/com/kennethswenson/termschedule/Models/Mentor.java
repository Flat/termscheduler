package com.kennethswenson.termschedule.Models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

@Entity(foreignKeys = @ForeignKey(entity = TermClass.class, parentColumns = "id", childColumns = "classId", onDelete = ForeignKey.CASCADE))
public class Mentor {
    @PrimaryKey(autoGenerate = true)
    private Integer id;
    private Integer classId;
    private String name;
    private String phoneNumber;
    private String emailAddress;

    public Mentor(Integer classId, String name, String phoneNumber, String emailAddress) {
        this.classId = classId;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public Integer getClassId() {
        return classId;
    }

    public void setClassId(Integer classId) {
        this.classId = classId;
    }

    @Override
    public String toString() {
        return getName();
    }
}
