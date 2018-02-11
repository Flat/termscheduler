package com.kennethswenson.termschedule.Models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.kennethswenson.termschedule.Adapters.ZonedDateTimeTypeConverter;

import java.time.ZonedDateTime;

@Entity
public class ScheduledNotification {
    @PrimaryKey(autoGenerate = true)
    private Integer id;
    private String UUIDofCaller;
    private Integer requestCode;
    private String title;
    private String content;
    @TypeConverters(ZonedDateTimeTypeConverter.class)
    private ZonedDateTime notificationDateTime;

    public ScheduledNotification(String UUIDofCaller, Integer requestCode, String title, String content, ZonedDateTime notificationDateTime) {
        this.UUIDofCaller = UUIDofCaller;
        this.requestCode = requestCode;
        this.title = title;
        this.content = content;
        this.notificationDateTime = notificationDateTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUUIDofCaller() {
        return UUIDofCaller;
    }

    public void setUUIDofCaller(String UUIDofCaller) {
        this.UUIDofCaller = UUIDofCaller;
    }

    public Integer getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(Integer requestCode) {
        this.requestCode = requestCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ZonedDateTime getNotificationDateTime() {
        return notificationDateTime;
    }

    public void setNotificationDateTime(ZonedDateTime notificationDateTime) {
        this.notificationDateTime = notificationDateTime;
    }
}
