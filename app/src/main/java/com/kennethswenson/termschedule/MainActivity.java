package com.kennethswenson.termschedule;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_screen);
        setSupportActionBar(findViewById(R.id.toolbar));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("TermScheduler");
        }
        Button startButton = findViewById(R.id.startBtn);
        startButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, TermActivity.class);
            startActivity(intent);
        });
    }


}
