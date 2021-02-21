package com.example.final_project.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.final_project.R;
import com.example.final_project.objects.Run;
import com.example.final_project.objects.User;
import com.example.final_project.utils.Constants;
import com.example.final_project.utils.MySP;
import com.example.final_project.utils.MyStrings;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

public class Activity_Run_Details extends AppCompatActivity {
    private Toolbar run_details_TLB_toolbar;
    private TextView run_details_LBL_startTime;
    private TextView run_details_LBL_endTime;
    private TextView run_details_LBL_distance;
    private TextView run_details_LBL_duration;
    private TextView run_details_LBL_averageSpeed;
    private TextView run_details_LBL_maxSpeed;
    private TextView run_details_LBL_calories;
    private MaterialButton run_details_BTN_delete;
    private MaterialButton run_details_BTN_continue;

    private User user;
    private Run run;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run_details);

        findViews();
        initViews();
        initUser();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        checkFinishedActivity();
        return super.onOptionsItemSelected(item);
    }

    private void openMenuActivity() {
        Intent myIntent = new Intent(this, Activity_Menu.class);
        startActivity(myIntent);
        finish();
    }

    private void findViews() {
        run_details_TLB_toolbar = findViewById(R.id.run_details_TLB_toolbar);
        run_details_LBL_startTime = findViewById(R.id.run_details_LBL_startTime);
        run_details_LBL_endTime = findViewById(R.id.run_details_LBL_endTime);
        run_details_LBL_distance = findViewById(R.id.run_details_LBL_distance);
        run_details_LBL_duration = findViewById(R.id.run_details_LBL_duration);
        run_details_LBL_averageSpeed = findViewById(R.id.run_details_LBL_averageSpeed);
        run_details_LBL_maxSpeed = findViewById(R.id.run_details_LBL_maxSpeed);
        run_details_LBL_calories = findViewById(R.id.run_details_LBL_calories);
        run_details_BTN_delete = findViewById(R.id.run_details_BTN_delete);
        run_details_BTN_continue = findViewById(R.id.run_details_BTN_continue);
    }

    private void initViews() {
        run_details_BTN_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteRun();
            }
        });

        run_details_BTN_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkFinishedActivity();
            }
        });

        String runString = getIntent().getStringExtra(Constants.EXTRA_RUN_DETAILS);
        run = new Gson().fromJson(runString, Run.class);

        setSupportActionBar(run_details_TLB_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(MyStrings.makeDateString(run.getStartTime()));

        run_details_LBL_startTime.setText(MyStrings.makeTimeString(run.getStartTime()));
        run_details_LBL_endTime.setText(MyStrings.makeTimeString(run.getStartTime() + run.getDuration()));
        run_details_LBL_distance.setText(MyStrings.twoDigitsAfterPoint(run.getDistance()));
        run_details_LBL_duration.setText(MyStrings.makeDurationString(run.getDuration()));
        run_details_LBL_averageSpeed.setText(MyStrings.twoDigitsAfterPoint(run.getAverageSpeed()));
        run_details_LBL_maxSpeed.setText(MyStrings.twoDigitsAfterPoint(run.getMaxSpeed()));
        run_details_LBL_calories.setText("" + run.getCalories());
    }

    private void initUser() {
        String userString = MySP.getInstance().getString(MySP.KEYS.USER_DATA, "");
        user = new Gson().fromJson(userString, User.class);
    }

    private void deleteRun() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(Constants.RUNS_DB);
        myRef.child(user.getUid()).child("" + run.getStartTime()).removeValue();

        checkFinishedActivity();
    }

    private void checkFinishedActivity() {
        if (isTaskRoot()) {
            openMenuActivity();
        } else {
            finish();
        }
    }

}