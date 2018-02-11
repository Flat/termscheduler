package com.kennethswenson.termschedule.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.UUID;

public class PreferencesHelper {
    public static String getUUID(){
        return UUID.randomUUID().toString();
    }

    public static boolean getNotificationsEnabled(Context context, String UUID){
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(UUID, false);
    }

    public static void setNotificationsEnabled(Context context, String UUID, boolean enabled){
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean(UUID, enabled).apply();
    }
}
