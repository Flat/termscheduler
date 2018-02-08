package com.kennethswenson.termschedule.Models;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.kennethswenson.termschedule.Db.SchedulerDatabase;

import java.util.List;

public class AssessmentViewModel extends AndroidViewModel {
    private LiveData<List<Assessment>> assessments;

    public AssessmentViewModel(@NonNull Application application) {
        super(application);
    }
    public void init(Integer classId){
        SchedulerDatabase schedulerDatabase = SchedulerDatabase.getSchedulerDatabase(this.getApplication());
        assessments = schedulerDatabase.dbDao().getAssessmentsByClassId(classId);
    }

    public LiveData<List<Assessment>> getAssessments() {
        return assessments;
    }
}
