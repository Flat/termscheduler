package com.kennethswenson.termschedule;


import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.kennethswenson.termschedule.Db.SchedulerDatabase;
import com.kennethswenson.termschedule.Models.ClassStatus;
import com.kennethswenson.termschedule.Models.TermClass;
import com.kennethswenson.termschedule.utils.Dates;
import com.kennethswenson.termschedule.utils.Formatting;
import com.kennethswenson.termschedule.utils.NotificationHelper;
import com.kennethswenson.termschedule.utils.PreferencesHelper;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class AddClassActivity extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_class_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        if (actionBar != null) {
            actionBar.setTitle("Add Class");
        }
        SchedulerDatabase schedulerDatabase = SchedulerDatabase.getSchedulerDatabase(this);
        Integer termId = getIntent().getIntExtra("TERM_ID", -1);
        if (termId == -1){
            finish();
        }
        Button btnSave = findViewById(R.id.btnSave);
        Button btnCancel = findViewById(R.id.btnCancel);
        EditText className = findViewById(R.id.editText);
        TextView startDate = findViewById(R.id.startDate);
        TextView endDate = findViewById(R.id.endDate);
        ZonedDateTime now = ZonedDateTime.now();
        DateTimeFormatter dtf = Formatting.getDateTimeFormat();
        Spinner classType = findViewById(R.id.classType);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.ClassStatusTypes, android.R.layout.simple_spinner_dropdown_item);
        classType.setAdapter(adapter);
        int classId = getIntent().getIntExtra("CLASS_ID", -1);
        String classUUID = getIntent().getStringExtra("UUID");
        if (classId != -1){
            AsyncTask.execute(() -> {
                TermClass termClass = schedulerDatabase.dbDao().getClassById(classId);
                startDate.setText(termClass.getStartDate().format(dtf));
                endDate.setText(termClass.getEndDate().format(dtf));
                className.setText(termClass.getTitle());
                classType.setSelection(termClass.getClassStatus().getEnumnum());

            });
        } else {
            startDate.setText(now.format(dtf));
            endDate.setText(now.format(dtf));
        }
        btnSave.setOnClickListener(view -> {
            if (className.getText() != null && !className.getText().toString().equals("")){
                ClassStatus classStatus;
                switch(classType.getSelectedItem().toString()){
                    case "In Progress":
                        classStatus = ClassStatus.INPROGRESS;
                        break;
                    case "Dropped":
                        classStatus = ClassStatus.DROPPED;
                        break;
                    case "Completed":
                        classStatus = ClassStatus.COMPLETE;
                        break;
                    case "Planned":
                        classStatus = ClassStatus.PLANNED;
                        break;
                    default:
                        classStatus = ClassStatus.INPROGRESS;
                        break;
                }
                TermClass termClass = new TermClass(termId, className.getText().toString(),
                        Dates.parseTextToZonedDateTime(startDate.getText().toString()),
                        Dates.parseTextToZonedDateTime(endDate.getText().toString()), classStatus, "");
                if(classUUID != null && !classUUID.equals("")){
                    termClass.setUUID(classUUID);
                }

                if(PreferencesHelper.getNotificationsEnabled(this, termClass.getUUID())){
                    NotificationHelper.rescheduleTermClassNotification(this, termClass);
                }

                AsyncTask.execute(() -> {
                    if (classId != -1){
                        termClass.setId(classId);
                        termClass.setNotes(schedulerDatabase.dbDao().getClassById(classId).getNotes());
                        schedulerDatabase.dbDao().updateClass(termClass);
                    } else {
                        schedulerDatabase.dbDao().insertClass(termClass);
                    }
                });
                finish();
            }
        });
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
