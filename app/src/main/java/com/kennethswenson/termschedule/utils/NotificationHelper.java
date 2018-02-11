package com.kennethswenson.termschedule.utils;


import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;

import com.kennethswenson.termschedule.Db.SchedulerDatabase;
import com.kennethswenson.termschedule.Models.ScheduledNotification;
import com.kennethswenson.termschedule.Models.TermClass;
import com.kennethswenson.termschedule.NotificationReceiver;
import com.kennethswenson.termschedule.R;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class NotificationHelper {

   private static final String DEFAULT_NOTIFICATION_ID = "com.kennethswenson.termschedule.notification.DEFAULT";

   private static void createNotificationChannelDefault(Context context) {
       NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
       NotificationChannel notificationChannel = new NotificationChannel(DEFAULT_NOTIFICATION_ID, "Default Notifications", NotificationManager.IMPORTANCE_DEFAULT);
       notificationChannel.setDescription("Default notification channel for this app");
       notificationChannel.enableLights(true);
       notificationChannel.setLightColor(Color.BLUE);
       notificationChannel.enableVibration(true);
       if (notificationManager != null) {
           notificationManager.createNotificationChannel(notificationChannel);
       }
   }

    public static void schedule(Context context, ScheduledNotification scheduledNotification){
        NotificationHelper.createNotificationChannelDefault(context);
        Notification.Builder notificationBuilder = new Notification.Builder(context, NotificationHelper.DEFAULT_NOTIFICATION_ID);
        notificationBuilder.setContentTitle(scheduledNotification.getTitle());
        notificationBuilder.setContentText(scheduledNotification.getContent());
        notificationBuilder.setSmallIcon(R.drawable.ic_alarm_black_24dp);
        Notification notification = notificationBuilder.build();
        Intent intent = new Intent(context, NotificationReceiver.class);
        intent.putExtra("notification", notification);
        intent.putExtra("UUID", scheduledNotification.getUUIDofCaller());
        intent.putExtra("REQCODE", scheduledNotification.getRequestCode());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, scheduledNotification.getRequestCode(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.set(AlarmManager.RTC, scheduledNotification.getNotificationDateTime().toInstant().toEpochMilli(), pendingIntent);
        }
    }
    static void cancel(Context context, Integer requestCode){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.cancel(PendingIntent.getBroadcast(context, requestCode, new Intent(context, NotificationReceiver.class), PendingIntent.FLAG_UPDATE_CURRENT));
        }
    }

    public static void reschedule(Context context, String UUID, ScheduledNotification NewNotification){
        cancelAndRemove(context, UUID);
        scheduleAndAdd(context, NewNotification);
    }

    public static void scheduleAndAdd(Context context, ScheduledNotification scheduledNotification){
        AsyncTask.execute(() -> {
            SchedulerDatabase schedulerDatabase = SchedulerDatabase.getSchedulerDatabase(context);
            schedulerDatabase.dbDao().insertNotification(scheduledNotification);
            schedule(context, scheduledNotification);
        });
    }

    public static void cancelAndRemove(Context context, String UUID){
        AsyncTask.execute(() -> {
            SchedulerDatabase schedulerDatabase = SchedulerDatabase.getSchedulerDatabase(context);
            List<ScheduledNotification> scheduledNotifications = schedulerDatabase.dbDao().getNotificationsByUUID(UUID);
            for (ScheduledNotification notification: scheduledNotifications) {
                cancel(context, notification.getRequestCode());
                schedulerDatabase.dbDao().deleteNotifications(notification);
            }
        });
    }


    public static void addTermClassNotifications(Context context, TermClass termClass){
        for (ScheduledNotification notification: getTermClassNotifications(context, termClass)){
            scheduleAndAdd(context, notification);
        }
    }

    public static void rescheduleTermClassNotification(Context context, TermClass termClass){
        for (ScheduledNotification notification: getTermClassNotifications(context, termClass)) {
            reschedule(context,termClass.getUUID(), notification);
        }
    }

    private static ArrayList<ScheduledNotification> getTermClassNotifications(Context context, TermClass termClass){
        ArrayList<ScheduledNotification> notifications = new ArrayList<>();
        if (termClass.getStartDate().toInstant().toEpochMilli() < Instant.now().toEpochMilli()){
            notifications.add(new ScheduledNotification(termClass.getUUID(), termClass.getUUID().hashCode()*2, termClass.getTitle(), "Class ends tomorrow!", termClass.getEndDate().minusDays(1)));
        } else {
            notifications.add(new ScheduledNotification(termClass.getUUID(), termClass.getUUID().hashCode(), termClass.getTitle(), "Class starts tomorrow!", termClass.getStartDate().minusDays(1)));
            notifications.add(new ScheduledNotification(termClass.getUUID(), termClass.getUUID().hashCode()*2, termClass.getTitle(), "Class ends tomorrow!", termClass.getEndDate().minusDays(1)));
        }
        return notifications;
    }
}
