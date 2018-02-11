package com.kennethswenson.termschedule;


import android.app.AlertDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteConstraintException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.kennethswenson.termschedule.Db.SchedulerDatabase;
import com.kennethswenson.termschedule.Models.ClassViewModel;
import com.kennethswenson.termschedule.Models.Term;
import com.kennethswenson.termschedule.Models.TermClass;
import com.kennethswenson.termschedule.utils.Formatting;



public class TermDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.term_details_layout);
        setSupportActionBar(findViewById(R.id.toolbar));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        if (actionBar != null) {
            actionBar.setTitle("Term Details");
        }
        Integer currentTermId = getIntent().getIntExtra("TERMID", -1);
        FloatingActionButton fab = findViewById(R.id.fab);
        SchedulerDatabase schedulerDatabase = SchedulerDatabase.getSchedulerDatabase(this);
        fab.setOnClickListener((view) -> addNewClass(currentTermId));
        if (currentTermId == -1) {
            finish();
        }
        ListView listView = findViewById(R.id.ListView1);
        ClassViewModel classViewModel = ViewModelProviders.of(this).get(ClassViewModel.class);
        classViewModel.init(currentTermId);
        classViewModel.getClasses().observe(this, termClasses -> {
            ArrayAdapter<TermClass> arrayAdapter = null;
            if (termClasses != null) {
                arrayAdapter = new ArrayAdapter<TermClass>(this, android.R.layout.simple_list_item_1, termClasses);
            }
            listView.setAdapter(arrayAdapter);
        });
        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            TermClass termClass = (TermClass) listView.getItemAtPosition(i);
            Intent intent = new Intent(this, ClassDetailActivity.class);
            intent.putExtra("CLASS_ID", termClass.getId());
            startActivity(intent);
        });

        listView.setOnItemLongClickListener((adapterView, view, i, l) -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure you want to remove this class?").setTitle("Delete Class");
            builder.setPositiveButton(android.R.string.ok, (dialogInterface, i1) -> {
                TermClass termClass = (TermClass) listView.getItemAtPosition(i);
                AsyncTask.execute(() -> {
                    try{
                        schedulerDatabase.dbDao().deleteClasses(termClass);
                    } catch (SQLiteConstraintException e) {
                        Snackbar.make(findViewById(android.R.id.content),
                                "Unable to remove class. There are still assessments associated with it.",
                                Snackbar.LENGTH_LONG).show();
                    }
                });
            });
            builder.setNegativeButton(android.R.string.cancel, null);
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            return true;
        });
        AsyncTask.execute(() -> {
            Term currentTerm = schedulerDatabase.dbDao().getTermById(currentTermId);
            TextView termName = findViewById(R.id.termName);
            TextView startDate  = findViewById(R.id.startDate);
            TextView endDate = findViewById(R.id.endDate);
            runOnUiThread(() -> {
                termName.setText(currentTerm.getTerm());
                startDate.setText(currentTerm.getStartDate().format(Formatting.getDateTimeFormat()));
                endDate.setText(currentTerm.getEndDate().format(Formatting.getDateTimeFormat()));
            });
        });
    }

    private void addNewClass(Integer termId) {
        Intent intent = new Intent(this, AddClassActivity.class);
        intent.putExtra("TERM_ID", termId);
        startActivity(intent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
            return true;
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
