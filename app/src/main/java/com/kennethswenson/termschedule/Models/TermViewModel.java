package com.kennethswenson.termschedule.Models;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.kennethswenson.termschedule.Db.SchedulerDatabase;

import java.util.List;

public class TermViewModel extends AndroidViewModel {
    private LiveData<List<Term>> terms;

    public TermViewModel(Application application){
        super(application);
        SchedulerDatabase schedulerDatabase = SchedulerDatabase.getSchedulerDatabase(this.getApplication());
        terms = schedulerDatabase.dbDao().loadTerms();
    }

    public LiveData<List<Term>> getTerms() {
        return terms;
    }
}
