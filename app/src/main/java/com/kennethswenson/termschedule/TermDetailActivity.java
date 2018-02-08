package com.kennethswenson.termschedule;


import android.app.AlertDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.kennethswenson.termschedule.Db.SchedulerDatabase;
import com.kennethswenson.termschedule.Models.Assessment;
import com.kennethswenson.termschedule.Models.ClassViewModel;
import com.kennethswenson.termschedule.Models.Term;
import com.kennethswenson.termschedule.Models.TermClass;


public class TermDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.term_details_layout);
        Integer currentTermId = getIntent().getIntExtra("TERMID", -1);
        FloatingActionButton fab = findViewById(R.id.fab);
        SchedulerDatabase schedulerDatabase = SchedulerDatabase.getSchedulerDatabase(this);
        fab.setOnClickListener((view) -> addNewClass());
        if (currentTermId == -1) {
            finish();
        }
        ListView listView = findViewById(R.id.ListView1);
        ClassViewModel classViewModel = ViewModelProviders.of(this).get(ClassViewModel.class);
        classViewModel.init(currentTermId);
        classViewModel.getClasses().observe(this, termClasses -> {
            ArrayAdapter<TermClass> arrayAdapter = new ArrayAdapter<TermClass>(this, android.R.layout.simple_list_item_1, termClasses);
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
            builder.setMessage("Are you sure you want to remove this term?").setTitle("Delete Term");
            builder.setPositiveButton(android.R.string.ok, (dialogInterface, i1) -> {
                Term term = (Term) listView.getItemAtPosition(i);
                schedulerDatabase.dbDao().deleteTerms(term);
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            return true;
        });
    }

    private void addNewClass() {

    }
}
