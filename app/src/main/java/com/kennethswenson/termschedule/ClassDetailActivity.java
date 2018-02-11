package com.kennethswenson.termschedule;


import android.app.AlertDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kennethswenson.termschedule.Db.SchedulerDatabase;
import com.kennethswenson.termschedule.Models.Assessment;
import com.kennethswenson.termschedule.Models.AssessmentViewModel;
import com.kennethswenson.termschedule.Models.TermClass;
import com.kennethswenson.termschedule.utils.Formatting;
import com.kennethswenson.termschedule.utils.NotificationHelper;
import com.kennethswenson.termschedule.utils.PreferencesHelper;

import java.time.format.DateTimeFormatter;

public class ClassDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.class_details_layout);
        setSupportActionBar(findViewById(R.id.toolbar));
        ActionBar actionBar = getSupportActionBar();
        FloatingActionButton fab = findViewById(R.id.fab);
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        if (actionBar != null) {
            actionBar.setTitle("Class Details");
        }
        Integer classId = getIntent().getIntExtra("CLASS_ID", -1);
        if (classId == -1){
            finish();
        }
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddAssessmentActivity.class);
            intent.putExtra("CLASS_ID", classId);
            startActivity(intent);
        });
        TextView className = findViewById(R.id.className);
        TextView startDate = findViewById(R.id.startDate);
        TextView endDate = findViewById(R.id.endDate);
        TextView classStatus = findViewById(R.id.status);
        ListView assessmentsList = findViewById(R.id.ListView1);
        SchedulerDatabase schedulerDatabase = SchedulerDatabase.getSchedulerDatabase(this);
        AssessmentViewModel assessmentViewModel = ViewModelProviders.of(this).get(AssessmentViewModel.class);
        assessmentViewModel.init(classId);
        assessmentViewModel.getAssessments().observe(this, assessments -> {
            ArrayAdapter<Assessment> assessmentArrayAdapter = null;
            if (assessments != null) {
                assessmentArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, assessments);
            }
            assessmentsList.setAdapter(assessmentArrayAdapter);
        });
        assessmentsList.setOnItemClickListener((parent, view, position, id) -> {
            Assessment assessment = (Assessment) assessmentsList.getItemAtPosition(position);
            Intent intent = new Intent(this, AssessmentDetailActivity.class);
            intent.putExtra("ASSESSMENT_ID", assessment.getId());
            startActivity(intent);
        });
        assessmentsList.setLongClickable(true);
        assessmentsList.setOnItemLongClickListener((adapterView, view, i, l) -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure you want to remove this assessment?").setTitle("Delete Assessment");
            builder.setPositiveButton(android.R.string.ok, (dialogInterface, i1) -> {
                Assessment assessment = (Assessment) assessmentsList.getItemAtPosition(i1);
                AsyncTask.execute(() -> schedulerDatabase.dbDao().deleteAssessments(assessment));
            });
            builder.setNegativeButton(android.R.string.cancel, null);
            AlertDialog dialog = builder.create();
            dialog.show();
            return true;
        });
        DateTimeFormatter dtf = Formatting.getDateTimeFormat();
        AsyncTask.execute(() ->{
            TermClass termClass = schedulerDatabase.dbDao().getClassById(classId);
            className.setText(termClass.getTitle());
            startDate.setText(termClass.getStartDate().format(dtf));
            endDate.setText(termClass.getEndDate().format(dtf));
            classStatus.setText(termClass.getClassStatus().toString());
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.class_details_menu, menu);
        MenuItem item = menu.findItem(R.id.class_notifications);
        AsyncTask.execute(() -> {
            Integer classId = getIntent().getIntExtra("CLASS_ID", -1);
            if(classId != -1){
                SchedulerDatabase schedulerDatabase = SchedulerDatabase.getSchedulerDatabase(this);
                TermClass termClass = schedulerDatabase.dbDao().getClassById(classId);
                if(PreferencesHelper.getNotificationsEnabled(this, termClass.getUUID())){
                    runOnUiThread(() -> item.setIcon(R.drawable.ic_notifications_black_24dp));
                }
            }

        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Integer classId = getIntent().getIntExtra("CLASS_ID", -1);
        switch (id){
            case R.id.class_notifications:
                AsyncTask.execute(() -> {
                    SchedulerDatabase schedulerDatabase = SchedulerDatabase.getSchedulerDatabase(this);
                    if (classId == -1){
                        return;
                    }
                    TermClass termClass = schedulerDatabase.dbDao().getClassById(classId);
                    if(PreferencesHelper.getNotificationsEnabled(this, termClass.getUUID())){
                        NotificationHelper.cancelAndRemove(this, termClass.getUUID());
                        runOnUiThread(() -> item.setIcon(R.drawable.ic_notifications_none_black_24dp));
                        PreferencesHelper.setNotificationsEnabled(this, termClass.getUUID(), false);
                    } else {
                        NotificationHelper.addTermClassNotifications(this, termClass);
                        runOnUiThread(() -> item.setIcon(R.drawable.ic_notifications_black_24dp));
                        PreferencesHelper.setNotificationsEnabled(this, termClass.getUUID(), true);
                    }
                });
                return true;
            case R.id.notes:
                Intent intent = new Intent(this, NotesActivity.class);
                if (classId == -1){
                    return true;
                }
                intent.putExtra("CLASS_ID", classId);
                startActivity(intent);
                return true;
            case R.id.mentors:
                Intent mentorIntent = new Intent(this, MentorsListActivity.class);
                if (classId == -1){
                    return true;
                }
                mentorIntent.putExtra("CLASS_ID", classId);
                startActivity(mentorIntent);
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.edit:
                AsyncTask.execute(() -> {
                    SchedulerDatabase schedulerDatabase = SchedulerDatabase.getSchedulerDatabase(this);
                    if (classId == -1){
                        Toast.makeText(this, "Unable to edit class", Toast.LENGTH_LONG).show();
                        return;
                    }
                    TermClass termClass = schedulerDatabase.dbDao().getClassById(classId);
                    Intent editIntent = new Intent(this, AddClassActivity.class);
                    editIntent.putExtra("TERM_ID", termClass.getTermId());
                    editIntent.putExtra("CLASS_ID",termClass.getId());
                    editIntent.putExtra("UUID", termClass.getUUID());
                    startActivity(editIntent);
                    finish();
                });
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        View topPanel = findViewById(R.id.topPanel);
        View bottomPanel = findViewById(R.id.bottomPanel);

        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) topPanel.getLayoutParams();
            params.weight = 3.6f;
            topPanel.setLayoutParams(params);
            LinearLayout.LayoutParams paramsBot = (LinearLayout.LayoutParams) bottomPanel.getLayoutParams();
            paramsBot.weight = 1.4f;
            bottomPanel.setLayoutParams(paramsBot);
        } else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) topPanel.getLayoutParams();
            params.weight = 3f;
            topPanel.setLayoutParams(params);
            LinearLayout.LayoutParams paramsBot = (LinearLayout.LayoutParams) bottomPanel.getLayoutParams();
            paramsBot.weight = 2f;
            bottomPanel.setLayoutParams(paramsBot);
        }
        super.onConfigurationChanged(newConfig);
    }
}
