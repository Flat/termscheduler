package com.kennethswenson.termschedule;


import android.app.AlertDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.kennethswenson.termschedule.Db.SchedulerDatabase;
import com.kennethswenson.termschedule.Models.Assessment;
import com.kennethswenson.termschedule.Models.AssessmentViewModel;
import com.kennethswenson.termschedule.Models.TermClass;
import com.kennethswenson.termschedule.utils.Formatting;

import java.time.format.DateTimeFormatter;

public class ClassDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.class_details_layout);
        Integer classId = getIntent().getIntExtra("CLASS_ID", -1);
        if (classId == -1){
            finish();
        }
        TextView className = findViewById(R.id.className);
        TextView startDate = findViewById(R.id.startDate);
        TextView endDate = findViewById(R.id.endDate);
        ListView assessmentsList = findViewById(R.id.ListView1);
        SchedulerDatabase schedulerDatabase = SchedulerDatabase.getSchedulerDatabase(this);
        AssessmentViewModel assessmentViewModel = ViewModelProviders.of(this).get(AssessmentViewModel.class);
        assessmentViewModel.init(classId);
        assessmentViewModel.getAssessments().observe(this, assessments -> {
            ArrayAdapter<Assessment> assessmentArrayAdapter = new ArrayAdapter<Assessment>(this, android.R.layout.simple_list_item_1, assessments);
            assessmentsList.setAdapter(assessmentArrayAdapter);
        });
        assessmentsList.setLongClickable(true);
        assessmentsList.setOnItemLongClickListener((adapterView, view, i, l) -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure you want to remove this assessment?").setTitle("Delete Assessment");
            builder.setPositiveButton(android.R.string.ok, (dialogInterface, i1) -> {
                Assessment assessment = (Assessment) assessmentsList.getItemAtPosition(i1);
                schedulerDatabase.dbDao().deleteAssessments(assessment);
            });
            AlertDialog dialog = builder.create();
            dialog.show();
            return true;
        });
        DateTimeFormatter dtf = Formatting.getDateTimeFormat();
        TermClass termClass = schedulerDatabase.dbDao().getClassById(classId);
        className.setText(termClass.getTitle());
        startDate.setText(termClass.getStartDate().format(dtf));
        endDate.setText(termClass.getEndDate().format(dtf));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.class_details_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.class_notifications:
                return true;
            case R.id.class_notes:
                return true;
            case R.id.mentors:
               return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
