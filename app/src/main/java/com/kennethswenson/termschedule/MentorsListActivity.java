package com.kennethswenson.termschedule;

import android.app.AlertDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.kennethswenson.termschedule.Db.SchedulerDatabase;
import com.kennethswenson.termschedule.Models.Mentor;
import com.kennethswenson.termschedule.Models.MentorViewModel;

public class MentorsListActivity extends AppCompatActivity{

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mentors_list);
        setSupportActionBar(findViewById(R.id.toolbar));
        ActionBar actionBar = getSupportActionBar();
        SchedulerDatabase schedulerDatabase = SchedulerDatabase.getSchedulerDatabase(this);
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        if (actionBar != null) {
            actionBar.setTitle("Class Mentors");
        }
        ListView mentorsList = findViewById(R.id.mentorsList);
        FloatingActionButton fab = findViewById(R.id.fab);
        Integer classId = getIntent().getIntExtra("CLASS_ID", -1);
        if(classId == -1){
            finish();
        }
        MentorViewModel mentorViewModel = ViewModelProviders.of(this).get(MentorViewModel.class);
        mentorViewModel.init(classId);
        mentorViewModel.getMentors().observe(this, mentors -> {
            ArrayAdapter<Mentor> mentorArrayAdapter = null;
            if (mentors != null) {
                mentorArrayAdapter = new ArrayAdapter<Mentor>(this, android.R.layout.simple_list_item_1, mentors);
            }
            mentorsList.setAdapter(mentorArrayAdapter);

        });
        mentorsList.setClickable(true);
        mentorsList.setLongClickable(true);
        mentorsList.setOnItemClickListener((parent, view, position, id) -> {
            Mentor mentor = (Mentor)mentorsList.getItemAtPosition(position);
            Intent intent = new Intent(this, MentorDetailActivity.class);
            intent.putExtra("MENTOR_ID", mentor.getId());
            startActivity(intent);
        });
        mentorsList.setOnItemLongClickListener((parent, view, position, id) -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure you want to remove this mentor?").setTitle("Delete Mentor?");
            builder.setPositiveButton(android.R.string.ok, (dialogInterface, i1) -> {
                Mentor mentor = (Mentor) mentorsList.getItemAtPosition(position);
                AsyncTask.execute(() -> {
                    schedulerDatabase.dbDao().deleteMentors(mentor);
                });
            });
            builder.setNegativeButton(android.R.string.cancel, null);
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            return true;
        });
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddMentorActivity.class);
            intent.putExtra("CLASS_ID", classId);
            startActivity(intent);
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case android.R.id.home:
                onBackPressed();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }
}
