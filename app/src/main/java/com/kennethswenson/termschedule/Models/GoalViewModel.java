package com.kennethswenson.termschedule.Models;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.kennethswenson.termschedule.Db.SchedulerDatabase;

import java.util.List;

public class GoalViewModel extends AndroidViewModel {
    private LiveData<List<Goal>> goals;

    public GoalViewModel(@NonNull Application application) {
        super(application);
    }
    public void init(Integer assessmentId){
        SchedulerDatabase schedulerDatabase = SchedulerDatabase.getSchedulerDatabase(this.getApplication());
        goals = schedulerDatabase.dbDao().getGoalsByAssessmentId(assessmentId);
    }

    public LiveData<List<Goal>> getGoals() {
        return goals;
    }
}
