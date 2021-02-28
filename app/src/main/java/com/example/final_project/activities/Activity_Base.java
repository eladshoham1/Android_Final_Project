package com.example.final_project.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.final_project.utils.Constants;

public class Activity_Base extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void openMenuActivity() {
        Intent myIntent = new Intent(this, Activity_Menu.class);
        myIntent.putExtra(Constants.EXTRA_GO_TO_ACTIVITY, Constants.PROFILE_CODE);
        startActivity(myIntent);
        finish();
    }
}