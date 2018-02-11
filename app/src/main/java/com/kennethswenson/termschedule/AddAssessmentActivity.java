package com.kennethswenson.termschedule;


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

import com.kennethswenson.termschedule.Db.SchedulerDatabase;
import com.kennethswenson.termschedule.Models.Assessment;
import com.kennethswenson.termschedule.Models.AssessmentType;

public class AddAssessmentActivity extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_assessment_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        if (actionBar != null) {
            actionBar.setTitle("Add Assessment");
        }
        SchedulerDatabase schedulerDatabase = SchedulerDatabase.getSchedulerDatabase(this);
        Integer classId = getIntent().getIntExtra("CLASS_ID", -1);
        if (classId == -1){
            finish();
        }
        Button btnSave = findViewById(R.id.btnSave);
        Button btnCancel = findViewById(R.id.btnCancel);
        EditText assessmentName = findViewById(R.id.editText);
        Spinner assessmentType = findViewById(R.id.assessmentType);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.AssessmentTypes, android.R.layout.simple_spinner_dropdown_item);
        assessmentType.setAdapter(adapter);
        Integer assessmentId = getIntent().getIntExtra("ASSESSMENT_ID", -1);
        if (assessmentId != -1){
            AsyncTask.execute(() -> {
                Assessment assessment = schedulerDatabase.dbDao().getAssessmentById(assessmentId);
                assessmentName.setText(assessment.getTitle());
                assessmentType.setSelection(assessment.getAssessmentType().getEnumnum());

            });
        }
        btnSave.setOnClickListener(view -> {
            if (assessmentName.getText() != null && !assessmentName.getText().toString().equals("")){
                AssessmentType assessmentType1;
                switch(assessmentType.getSelectedItem().toString()){
                    case "Objective":
                        assessmentType1 = AssessmentType.OBJECTIVE;
                        break;
                    case "Performance":
                        assessmentType1 = AssessmentType.PERFORMANCE;
                        break;
                    default:
                        assessmentType1 = AssessmentType.OBJECTIVE;
                        break;
                }
                Assessment assessment = new Assessment(classId, assessmentName.getText().toString(), assessmentType1);

                AsyncTask.execute(() -> {
                    if (assessmentId != -1){
                        assessment.setId(assessmentId);
                        schedulerDatabase.dbDao().updateAssessment(assessment);
                    } else {
                        schedulerDatabase.dbDao().insertAssessment(assessment);
                    }
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
