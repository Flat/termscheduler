package com.kennethswenson.termschedule;


import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.kennethswenson.termschedule.Db.SchedulerDatabase;
import com.kennethswenson.termschedule.Models.Mentor;

public class MentorDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mentor_details);
        setSupportActionBar(findViewById(R.id.toolbar));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        if (actionBar != null) {
            actionBar.setTitle("Mentors Details");
        }
        Integer mentorId = getIntent().getIntExtra("MENTOR_ID", -1);
        SchedulerDatabase schedulerDatabase = SchedulerDatabase.getSchedulerDatabase(this);
        if (mentorId == -1) {
            finish();
        }
        TextView mentorName = findViewById(R.id.mentorName);
        TextView phoneNumber = findViewById(R.id.phoneNumber);
        TextView emailAddress = findViewById(R.id.EmailAddress);
        AsyncTask.execute(() -> {
            Mentor mentor = schedulerDatabase.dbDao().getMentorById(mentorId);
            mentorName.setText(mentor.getName());
            phoneNumber.setText(mentor.getPhoneNumber());
            emailAddress.setText(mentor.getEmailAddress());

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
            return true;
        } else if (item.getItemId() == R.id.edit){
            Integer mentorId = getIntent().getIntExtra("MENTOR_ID", -1);
            if(mentorId == -1 ){
                return true;
            }
            Intent intent = new Intent(this, AddMentorActivity.class);
            SchedulerDatabase schedulerDatabase = SchedulerDatabase.getSchedulerDatabase(this);
            AsyncTask.execute(() -> {
                Mentor mentor = schedulerDatabase.dbDao().getMentorById(mentorId);
                intent.putExtra("MENTOR_ID", mentorId);
                intent.putExtra("CLASS_ID", mentor.getClassId());
                startActivity(intent);
            });
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
