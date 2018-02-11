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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kennethswenson.termschedule.Adapters.GoalArrayAdapter;
import com.kennethswenson.termschedule.Db.SchedulerDatabase;
import com.kennethswenson.termschedule.Models.Assessment;
import com.kennethswenson.termschedule.Models.Goal;
import com.kennethswenson.termschedule.Models.GoalViewModel;
import com.kennethswenson.termschedule.utils.NotificationHelper;

public class AssessmentDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assessment_details_layout);
        setSupportActionBar(findViewById(R.id.toolbar));
        FloatingActionButton fab = findViewById(R.id.fab);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        if (actionBar != null) {
            actionBar.setTitle("Assessment Details");
        }
        Integer assessmentId = getIntent().getIntExtra("ASSESSMENT_ID", -1);
        if (assessmentId == -1){
            finish();
        }
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddGoalActivity.class);
            intent.putExtra("ASSESSMENT_ID", assessmentId);
            startActivity(intent);
        });
        TextView assessmentName = findViewById(R.id.assessmentName);
        TextView assessmentType = findViewById(R.id.assessmentType);
        ListView goalList = findViewById(R.id.ListView1);
        SchedulerDatabase schedulerDatabase = SchedulerDatabase.getSchedulerDatabase(this);
        GoalViewModel goalViewModel = ViewModelProviders.of(this).get(GoalViewModel.class);
        goalViewModel.init(assessmentId);
        goalViewModel.getGoals().observe(this, goals -> {
            GoalArrayAdapter goalArrayAdapter = null;
            if (goals != null) {
                goalArrayAdapter = new GoalArrayAdapter(this, R.layout.goal_list_view_item, goals);
            }
            goalList.setAdapter(goalArrayAdapter);
        });
        goalList.setLongClickable(true);
        goalList.setOnItemLongClickListener((adapterView, view, i, l) -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure you want to remove this goal?").setTitle("Delete Goal?");
            builder.setPositiveButton(android.R.string.ok, (dialogInterface, i1) -> {
                Goal goal = (Goal) goalList.getItemAtPosition(i);
                AsyncTask.execute(() -> {
                    schedulerDatabase.dbDao().deleteGoals(goal);
                    NotificationHelper.cancelAndRemove(this, goal.getUUID());
                });
            });
            builder.setNegativeButton(android.R.string.cancel, null);
            AlertDialog dialog = builder.create();
            dialog.show();
            return true;
        });
        AsyncTask.execute(() ->{
            Assessment assessment = schedulerDatabase.dbDao().getAssessmentById(assessmentId);
            assessmentName.setText(assessment.getTitle());
            assessmentType.setText(assessment.getAssessmentType().toString());
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.edit:
                AsyncTask.execute(() -> {
                    SchedulerDatabase schedulerDatabase = SchedulerDatabase.getSchedulerDatabase(this);
                    Integer assessmentId = getIntent().getIntExtra("ASSESSMENT_ID", -1);
                    if (assessmentId == -1){
                        Toast.makeText(this, "Unable to edit assessment", Toast.LENGTH_LONG).show();
                        return;
                    }
                    Assessment assessment = schedulerDatabase.dbDao().getAssessmentById(assessmentId);
                    Intent intent = new Intent(this, AddAssessmentActivity.class);
                    intent.putExtra("CLASS_ID", assessment.getClassId());
                    intent.putExtra("ASSESSMENT_ID", assessmentId);
                    startActivity(intent);
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
