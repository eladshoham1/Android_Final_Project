package com.example.final_project.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.final_project.R;
import com.example.final_project.fragments.Fragment_Map;

public class Activity_Running extends AppCompatActivity {
    private Fragment_Map fragment_map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running);
        initFragments();
    }

    private void initFragments() {
        fragment_map = new Fragment_Map();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.running_LAY_map, fragment_map)
                .commit();
    }
}