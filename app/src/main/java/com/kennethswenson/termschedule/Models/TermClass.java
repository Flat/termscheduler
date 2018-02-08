package com.kennethswenson.termschedule.Models;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.kennethswenson.termschedule.Adapters.ClassStatusTypeConverter;
import com.kennethswenson.termschedule.Adapters.ZonedDateTimeTypeConverter;

import java.time.ZonedDateTime;

@Entity(foreignKeys = @ForeignKey(entity = Term.class, parentColumns = "id", childColumns = "termId", onDelete = ForeignKey.RESTRICT))
@TypeConverters(ZonedDateTimeTypeConverter.class)
public class TermClass {
    @PrimaryKey(autoGenerate = true)
    private Integer id;
    private Integer termId;
    private String title;
    private ZonedDateTime startDate;
    private ZonedDateTime endDate;
    @TypeConverters(ClassStatusTypeConverter.class)
    private ClassStatus classStatus;
    private String notes;
    private boolean startNotification;
    private boolean endNotification;

    public TermClass(Integer termId, String title, ZonedDateTime startDate, ZonedDateTime endDate, ClassStatus classStatus, String notes, boolean startNotification, boolean endNotification) {
        this.termId = termId;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.classStatus = classStatus;
        this.notes = notes;
        this.startNotification = startNotification;
        this.endNotification = endNotification;
    }

    public Integer getId() {
        return id;
    }

    public Integer getTermId() {
        return termId;
    }

    public void setTermId(Integer termId) {
        this.termId = termId;
    }

    public String getTitle() {
        return title;
    }

    public ZonedDateTime getStartDate() {
        return startDate;
    }

    public ZonedDateTime getEndDate() {
        return endDate;
    }

    public ClassStatus getClassStatus() {
        return classStatus;
    }

    public String getNotes() {
        return notes;
    }

    public boolean getStartNotification() {
        return startNotification;
    }

    public boolean getEndNotification() {
        return endNotification;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setStartDate(ZonedDateTime startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(ZonedDateTime endDate) {
        this.endDate = endDate;
    }

    public void setClassStatus(ClassStatus classStatus) {
        this.classStatus = classStatus;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setStartNotification(boolean startNotification) {
        this.startNotification = startNotification;
    }

    public void setEndNotification(boolean endNotification) {
        this.endNotification = endNotification;
    }


    @Override
    public String toString() {
        return title;
    }

}
