package com.kennethswenson.termschedule;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.kennethswenson.termschedule.Db.SchedulerDatabase;

public class NotificationReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = intent.getParcelableExtra("notification");
        String UUID = intent.getStringExtra("UUID");
        Integer reqcode = intent.getIntExtra("REQCODE", -1);
        if (notification != null && !UUID.isEmpty() && reqcode != -1) {
            if (notificationManager != null) {
                notificationManager.notify(1, notification);
                AsyncTask.execute(() -> {
                    SchedulerDatabase schedulerDatabase = SchedulerDatabase.getSchedulerDatabase(context);
                    schedulerDatabase.dbDao().deleteNotificationByUUIDAndRequestCode(UUID, reqcode);
                });
            }
        }
    }
}
