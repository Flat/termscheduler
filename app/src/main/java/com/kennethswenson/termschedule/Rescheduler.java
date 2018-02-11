package com.kennethswenson.termschedule;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.kennethswenson.termschedule.Db.SchedulerDatabase;
import com.kennethswenson.termschedule.Models.ScheduledNotification;
import com.kennethswenson.termschedule.utils.NotificationHelper;

import java.util.List;

public class Rescheduler extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        AsyncTask.execute( () -> {
            SchedulerDatabase schedulerDatabase = SchedulerDatabase.getSchedulerDatabase(context);
            List<ScheduledNotification> scheduledNotifications = schedulerDatabase.dbDao().getAllNotifications();
            for (ScheduledNotification notification: scheduledNotifications) {
                NotificationHelper.schedule(context, notification);
            }
        });
    }
}
