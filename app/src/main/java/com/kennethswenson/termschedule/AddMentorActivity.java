package com.kennethswenson.termschedule;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.kennethswenson.termschedule.Db.SchedulerDatabase;
import com.kennethswenson.termschedule.Models.Mentor;

public class AddMentorActivity extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_mentor_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        if (actionBar != null) {
            actionBar.setTitle("Add Mentor");
        }
        SchedulerDatabase schedulerDatabase = SchedulerDatabase.getSchedulerDatabase(this);
        Integer classId = getIntent().getIntExtra("CLASS_ID", -1);
        if (classId == -1){
            finish();
        }
        Button btnSave = findViewById(R.id.btnSave);
        Button btnCancel = findViewById(R.id.btnCancel);
        EditText mentorName = findViewById(R.id.editText);
        EditText phoneNumber = findViewById(R.id.phoneNumber);
        EditText emailAddress = findViewById(R.id.EmailAddress);
        int mentorId = getIntent().getIntExtra("MENTOR_ID", -1);
        if (mentorId != -1){
            AsyncTask.execute(() -> {
                Mentor mentor = schedulerDatabase.dbDao().getMentorById(mentorId);
                mentorName.setText(mentor.getName());
                phoneNumber.setText(mentor.getPhoneNumber());
                emailAddress.setText(mentor.getEmailAddress());
            });
        }
        btnSave.setOnClickListener(view -> {
            if (mentorName.getText() != null && !mentorName.getText().toString().equals("")){
                Mentor mentor = new Mentor(classId, mentorName.getText().toString(), phoneNumber.getText().toString(), emailAddress.getText().toString());
                if (mentorId != -1){
                    mentor.setId(mentorId);
                }
                AsyncTask.execute(() -> {
                    schedulerDatabase.dbDao().insertMentor(mentor);
                });
                finish();
            }
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
