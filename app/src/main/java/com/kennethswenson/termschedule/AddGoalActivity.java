package com.kennethswenson.termschedule;


import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.kennethswenson.termschedule.Db.SchedulerDatabase;
import com.kennethswenson.termschedule.Models.Goal;
import com.kennethswenson.termschedule.Models.ScheduledNotification;
import com.kennethswenson.termschedule.utils.Dates;
import com.kennethswenson.termschedule.utils.Formatting;
import com.kennethswenson.termschedule.utils.NotificationHelper;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class AddGoalActivity extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_goal_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        if (actionBar != null) {
            actionBar.setTitle("Add Goal");
        }
        SchedulerDatabase schedulerDatabase = SchedulerDatabase.getSchedulerDatabase(this);
        Integer termId = getIntent().getIntExtra("ASSESSMENT_ID", -1);
        if (termId == -1){
            finish();
        }
        Button btnSave = findViewById(R.id.btnSave);
        Button btnCancel = findViewById(R.id.btnCancel);
        EditText goalName = findViewById(R.id.editText);
        TextView startDate = findViewById(R.id.startDate);
        ZonedDateTime now = ZonedDateTime.now();
        startDate.setText(now.format(Formatting.getDateTimeFormat()));
        btnSave.setOnClickListener(view -> {
                Goal goal = new Goal(goalName.getText().toString(), termId, Dates.parseTextToZonedDateTime(startDate.getText().toString()));
                AsyncTask.execute(() -> {
                    schedulerDatabase.dbDao().insertGoal(goal);
                    NotificationHelper.scheduleAndAdd(this, new ScheduledNotification(goal.getUUID(), goal.getUUID().hashCode(), goal.getTitle(), "", goal.getGoalDateTime()));
                });
                finish();
        });
        DatePickerDialog.OnDateSetListener startDateListener;

        startDateListener = (datePicker, i, i1, i2) -> startDate.setText(LocalDate.parse(i + "/" + (i1 + 1) + "/" + i2, DateTimeFormatter.ofPattern("yyyy/L/d")).atStartOfDay(ZoneId.systemDefault()).format(Formatting.getDateTimeFormat()));

        startDate.setOnClickListener(view -> {
            DatePickerDialog dpd = new DatePickerDialog(this, startDateListener, now.getYear(), now.getMonthValue() -1, now.getDayOfMonth());
            dpd.show();
        });

        btnCancel.setOnClickListener(v -> finish());

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
