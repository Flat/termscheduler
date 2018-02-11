package com.kennethswenson.termschedule;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.kennethswenson.termschedule.Db.SchedulerDatabase;
import com.kennethswenson.termschedule.Models.TermClass;

public class NotesActivity extends AppCompatActivity{
    private ShareActionProvider shareActionProvider;
    private EditText notes;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notes_layout);
        setSupportActionBar(findViewById(R.id.toolbar));
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setTitle("Notes");
            actionbar.setDisplayHomeAsUpEnabled(true);
        }
        notes = findViewById(R.id.editText);
        Button saveBtn = findViewById(R.id.savebtn);
        Integer classId = getIntent().getIntExtra("CLASS_ID", -1);
        if(classId == -1) {
            finish();
        }

        SchedulerDatabase schedulerDatabase = SchedulerDatabase.getSchedulerDatabase(this);
        AsyncTask.execute(() -> {
            TermClass termClass = schedulerDatabase.dbDao().getClassById(classId);
            String classNotes = termClass.getNotes();
            if (!classNotes.isEmpty()) {
                notes.setText(termClass.getNotes());
            }
        });

        saveBtn.setOnClickListener(view -> {
            if (!notes.getText().toString().isEmpty()){
                AsyncTask.execute(() -> {
                    TermClass termClass = schedulerDatabase.dbDao().getClassById(classId);
                    termClass.setNotes(notes.getText().toString());
                    schedulerDatabase.dbDao().updateClass(termClass);
                    runOnUiThread(this::finish);
                });
            }
        });

        notes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, s.toString());
                setShareIntent(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.notes_menu, menu);
        MenuItem item = menu.findItem(R.id.notes_share);
        shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, notes.getText().toString());
        setShareIntent(intent);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setShareIntent(Intent shareIntent) {
        if (shareActionProvider != null){
            shareActionProvider.setShareIntent(shareIntent);
        }
    }
}
