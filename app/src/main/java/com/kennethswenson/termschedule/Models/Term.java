package com.kennethswenson.termschedule.Models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.kennethswenson.termschedule.Adapters.ZonedDateTimeTypeConverter;
import com.kennethswenson.termschedule.utils.PreferencesHelper;

import java.time.ZonedDateTime;

@Entity
@TypeConverters(ZonedDateTimeTypeConverter.class)
public class Term {

    @PrimaryKey(autoGenerate = true)
    private Integer id;
    private String UUID;
    private String term;
    private ZonedDateTime startDate;
    private ZonedDateTime endDate;

    public Term(String term, ZonedDateTime startDate, ZonedDateTime endDate){
        this.term = term;
        this.startDate = startDate;
        this.endDate = endDate;
        this.UUID = PreferencesHelper.getUUID();
    }

    public Integer getId() {
        return id;
    }

    public String getTerm(){
        return term;
    }

    public ZonedDateTime getStartDate(){
        return startDate;
    }

    public ZonedDateTime getEndDate(){
        return endDate;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public void setStartDate(ZonedDateTime startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(ZonedDateTime endDate) {
        this.endDate = endDate;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    @Override
    public String toString() {
        return term;
    }
}
