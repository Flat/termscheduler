package com.kennethswenson.termschedule.Models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.kennethswenson.termschedule.Adapters.AssessmentTypeConverter;

@Entity(foreignKeys = @ForeignKey(entity = TermClass.class,
                                    parentColumns = "id",
                                    childColumns = "classId",
                                    onDelete = ForeignKey.RESTRICT))
public class Assessment {
    @PrimaryKey(autoGenerate = true)
    private Integer id;
    private Integer classId;
    private String title;
    @TypeConverters(AssessmentTypeConverter.class)
    private AssessmentType assessmentType;

    public Assessment(Integer classId, String title, AssessmentType assessmentType) {
        this.classId = classId;
        this.title = title;
        this.assessmentType = assessmentType;
    }

    public AssessmentType getAssessmentType() {
        return assessmentType;
    }

    public void setAssessmentType(AssessmentType assessmentType) {
        this.assessmentType = assessmentType;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getClassId() {
        return classId;
    }

    public void setClassId(Integer classId) {
        this.classId = classId;
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return title;
    }
}
