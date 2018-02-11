package com.kennethswenson.termschedule.Models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.kennethswenson.termschedule.Adapters.ZonedDateTimeTypeConverter;
import com.kennethswenson.termschedule.utils.PreferencesHelper;

import java.time.ZonedDateTime;

@Entity(foreignKeys = @ForeignKey(entity = Assessment.class,
        parentColumns = "id",
        childColumns = "assessmentId",
        onDelete = ForeignKey.CASCADE))
public class Goal {
    @PrimaryKey(autoGenerate = true)
    private Integer id;
    private String UUID;
    private String title;
    private Integer assessmentId;
    @TypeConverters(ZonedDateTimeTypeConverter.class)
    private ZonedDateTime goalDateTime;

    public Goal(String title,Integer assessmentId, ZonedDateTime goalDateTime) {
        this.title = title;
        this.assessmentId = assessmentId;
        this.goalDateTime = goalDateTime;
        this.UUID = PreferencesHelper.getUUID();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAssessmentId() {
        return assessmentId;
    }

    public void setAssessmentId(Integer assessmentId) {
        this.assessmentId = assessmentId;
    }

    public ZonedDateTime getGoalDateTime() {
        return goalDateTime;
    }

    public void setGoalDateTime(ZonedDateTime goalDateTime) {
        this.goalDateTime = goalDateTime;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }
}
