package com.kennethswenson.termschedule;

import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.kennethswenson.termschedule.Db.SchedulerDatabase;
import com.kennethswenson.termschedule.Models.Term;
import com.kennethswenson.termschedule.utils.Dates;
import com.kennethswenson.termschedule.utils.Formatting;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;


public class TermAddActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_term_layout);
        setSupportActionBar(findViewById(R.id.toolbar));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        if (actionBar != null) {
            actionBar.setTitle("Add Term");
        }
        SchedulerDatabase schedulerDatabase = SchedulerDatabase.getSchedulerDatabase(getApplicationContext());
        Button btnSave = findViewById(R.id.btnSave);
        Button btnCancel = findViewById(R.id.btnCancel);
        EditText termName = findViewById(R.id.editText);
        TextView startDate = findViewById(R.id.startDate);
        TextView endDate = findViewById(R.id.endDate);
        ZonedDateTime now = ZonedDateTime.now();
        DateTimeFormatter dtf = Formatting.getDateTimeFormat();
        startDate.setText(now.format(dtf));
        endDate.setText(now.format(dtf));

        DatePickerDialog.OnDateSetListener startDateListener, endDateListener;

        startDateListener = (datePicker, i, i1, i2) -> startDate.setText(LocalDate.parse(i + "/" + (i1 + 1) + "/" + i2, DateTimeFormatter.ofPattern("yyyy/L/d")).atStartOfDay(ZoneId.systemDefault()).format(Formatting.getDateTimeFormat()));

        endDateListener = (datePicker, i, i1, i2) -> endDate.setText(LocalDate.parse(i + "/" + (i1 + 1) + "/" + i2, DateTimeFormatter.ofPattern("yyyy/L/d")).atStartOfDay(ZoneId.systemDefault()).format(Formatting.getDateTimeFormat()));

        startDate.setOnClickListener(view -> {
            DatePickerDialog dpd = new DatePickerDialog(this, startDateListener, now.getYear(), now.getMonthValue() -1, now.getDayOfMonth());
            dpd.show();
        });

        endDate.setOnClickListener(view -> {
            DatePickerDialog dpd = new DatePickerDialog(this, endDateListener, now.getYear(), now.getMonthValue() -1, now.getDayOfMonth());
            dpd.show();
        });

        btnSave.setOnClickListener(view -> {
            if (!termName.getText().toString().equals("") && termName.getText() != null){
                Term term = new Term(termName.getText().toString(), Dates.parseTextToZonedDateTime(startDate.getText().toString()), Dates.parseTextToZonedDateTime(endDate.getText().toString()));
                AsyncTask.execute(() -> schedulerDatabase.dbDao().insertTerm(term));
                finish();
            }
        });
        btnCancel.setOnClickListener(view -> {
            finish();
        });
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
