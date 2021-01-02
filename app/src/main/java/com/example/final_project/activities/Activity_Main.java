package com.example.final_project.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.final_project.R;
import com.example.final_project.utils.MySignal;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;

public class Activity_Main extends AppCompatActivity {
    private MaterialButton main_BTN_run;
    private BottomNavigationView main_NVG_bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        initViews();
    }

    private void findViews() {
        main_BTN_run = findViewById(R.id.main_BTN_run);
        main_NVG_bottomNavigation = findViewById(R.id.main_NVG_bottomNavigation);
    }

    private void initViews() {
        main_BTN_run.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNewRunning();
            }
        });
        
        main_NVG_bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                MySignal.getInstance().toast(item.getTitle().toString());
                return true;
            }
        });
    }

    private void startNewRunning() {
        Intent myIntent = new Intent(this, Activity_Running.class);
        startActivity(myIntent);
    }
}