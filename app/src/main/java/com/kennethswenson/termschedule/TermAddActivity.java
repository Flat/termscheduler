package com.kennethswenson.termschedule;

import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.DatePicker;
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
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Add Term");
        }

        DatePickerDialog.OnDateSetListener startDateListener, endDateListener;

        startDateListener = (datePicker, i, i1, i2) -> startDate.setText(LocalDate.parse(i + "/" + i1 + "/" + i2, DateTimeFormatter.ofPattern("yyyy/L/d")).atStartOfDay(ZoneId.systemDefault()).format(Formatting.getDateTimeFormat()));

        endDateListener = (datePicker, i, i1, i2) -> endDate.setText(LocalDate.parse(i + "/" + i1 + "/" + i2, DateTimeFormatter.ofPattern("yyyy/L/d")).atStartOfDay(ZoneId.systemDefault()).format(Formatting.getDateTimeFormat()));

        startDate.setOnClickListener(view -> {
            DatePickerDialog dpd = new DatePickerDialog(this, startDateListener, now.getYear(), now.getMonthValue(), now.getDayOfMonth());
            dpd.show();
        });

        endDate.setOnClickListener(view -> {
            DatePickerDialog dpd = new DatePickerDialog(this, endDateListener, now.getYear(), now.getMonthValue(), now.getDayOfMonth());
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
            finishActivity(0);
        });
    }
}
