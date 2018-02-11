package com.kennethswenson.termschedule.Models;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.kennethswenson.termschedule.Db.SchedulerDatabase;

import java.util.List;

public class MentorViewModel extends AndroidViewModel{
    private LiveData<List<Mentor>> mentors;

    public MentorViewModel(@NonNull Application application) {
        super(application);
    }

    public void init (Integer classId){
        SchedulerDatabase schedulerDatabase = SchedulerDatabase.getSchedulerDatabase(this.getApplication());
        mentors = schedulerDatabase.dbDao().getMentorsByClassId(classId);
    }

    public LiveData<List<Mentor>> getMentors() {return mentors;}

}
