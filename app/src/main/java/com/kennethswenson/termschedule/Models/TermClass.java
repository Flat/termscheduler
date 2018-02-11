package com.kennethswenson.termschedule.Models;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.kennethswenson.termschedule.Adapters.ClassStatusTypeConverter;
import com.kennethswenson.termschedule.Adapters.ZonedDateTimeTypeConverter;
import com.kennethswenson.termschedule.utils.PreferencesHelper;

import java.time.ZonedDateTime;

@Entity(foreignKeys = @ForeignKey(entity = Term.class, parentColumns = "id", childColumns = "termId", onDelete = ForeignKey.RESTRICT))
@TypeConverters(ZonedDateTimeTypeConverter.class)
public class TermClass {
    @PrimaryKey(autoGenerate = true)
    private Integer id;
    private String UUID;
    private Integer termId;
    private String title;
    private ZonedDateTime startDate;
    private ZonedDateTime endDate;
    @TypeConverters(ClassStatusTypeConverter.class)
    private ClassStatus classStatus;
    private String notes;

    public TermClass(Integer termId, String title, ZonedDateTime startDate, ZonedDateTime endDate, ClassStatus classStatus, String notes) {
        this.termId = termId;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.classStatus = classStatus;
        this.notes = notes;
        this.UUID = PreferencesHelper.getUUID();
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

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    @Override
    public String toString() {
        return title;
    }

}
