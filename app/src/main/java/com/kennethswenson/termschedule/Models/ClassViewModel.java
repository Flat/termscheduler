package com.kennethswenson.termschedule.Models;


import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.kennethswenson.termschedule.Db.SchedulerDatabase;

import java.util.List;

public class ClassViewModel extends AndroidViewModel {
    private LiveData<List<TermClass>> classes;

    public ClassViewModel(Application application){
        super(application);
    }

   public void init(Integer termId){
       SchedulerDatabase schedulerDatabase = SchedulerDatabase.getSchedulerDatabase(this.getApplication());
       classes = schedulerDatabase.dbDao().getClassesByTermId(termId);
   }

    public LiveData<List<TermClass>> getClasses() {
        return classes;
    }

}
