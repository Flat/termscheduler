package com.kennethswenson.termschedule.Db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {com.kennethswenson.termschedule.Models.Term.class,
        com.kennethswenson.termschedule.Models.Assessment.class,
        com.kennethswenson.termschedule.Models.TermClass.class,
        com.kennethswenson.termschedule.Models.Mentor.class},
        version = 1, exportSchema = false)
public abstract class SchedulerDatabase extends RoomDatabase {
    private static SchedulerDatabase schedulerDatabase;
    public abstract DbDao dbDao();

    public static SchedulerDatabase getSchedulerDatabase(Context context) {
        if (schedulerDatabase == null) {
            schedulerDatabase = Room.databaseBuilder(context.getApplicationContext(),
                    SchedulerDatabase.class, "database").build();
        }
        return schedulerDatabase;
    }

    public static void closeSchedulerDatabase() {
        schedulerDatabase = null;
    }
}
