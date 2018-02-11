package com.kennethswenson.termschedule;


import android.app.AlertDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.kennethswenson.termschedule.Db.SchedulerDatabase;
import com.kennethswenson.termschedule.Models.Term;
import com.kennethswenson.termschedule.Models.TermViewModel;

public class TermActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar(findViewById(R.id.toolbar));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Terms");
        }
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), TermAddActivity.class);
            startActivity(intent);
        });
        ListView terms = findViewById(R.id.termList);
        SchedulerDatabase schedulerDatabase = SchedulerDatabase.getSchedulerDatabase(this);
        TermViewModel termViewModel = ViewModelProviders.of(this).get(TermViewModel.class);
        termViewModel.getTerms().observe(this, termList -> {
            ArrayAdapter<Term> arrayAdapter = null;
            if (termList != null) {
                arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, termList);
            }
            terms.setAdapter(arrayAdapter);
        });
        terms.setClickable(true);
        terms.setOnItemClickListener((adapterView, view, i, l) -> {
            Term term = (Term) terms.getItemAtPosition(i);
            Intent intent = new Intent(this, TermDetailActivity.class);
            intent.putExtra("TERMID", term.getId());
            startActivity(intent);
        });

        terms.setOnItemLongClickListener((adapterView, view, i, l) -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure you want to remove this term?").setTitle("Delete Term");
            builder.setPositiveButton(android.R.string.ok, (dialogInterface, i1) -> {
                Term term = (Term) terms.getItemAtPosition(i);
                AsyncTask.execute(() -> {
                    try{
                        schedulerDatabase.dbDao().deleteTerms(term);
                    } catch (SQLiteConstraintException e) {
                        Snackbar.make(findViewById(android.R.id.content),
                                "Unable to remove term. There are still classes associated with it.",
                                Snackbar.LENGTH_LONG).show();
                    }
                });
            });
            builder.setNegativeButton(android.R.string.cancel, null);
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            return true;
        });
    }
}
