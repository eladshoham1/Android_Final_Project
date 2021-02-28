package com.example.final_project.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.location.Location;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.final_project.R;
import com.example.final_project.callbacks.CallBack_Map;
import com.example.final_project.callbacks.CallBack_Run_Mode;
import com.example.final_project.fragments.running.Fragment_Map;
import com.example.final_project.fragments.running.Fragment_Running_Details;

import java.util.ArrayList;

public class Activity_Running extends Activity_Base {
    private Toolbar running_TLB_toolbar;
    private Fragment_Map fragment_map;

    private boolean runningStartMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running);
        
        findViews();
        initViews();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (runningStartMode) {
            new AlertDialog.Builder(this)
                    .setTitle("End Run")
                    .setMessage("Are you sure you want to end this run?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            openMenuActivity();
                        }
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        } else {
            openMenuActivity();
        }
    }

    private void findViews() {
        running_TLB_toolbar = findViewById(R.id.running_TLB_toolbar);
    }

    private void initViews() {
        setSupportActionBar(running_TLB_toolbar);
        setToolBar();

        Fragment_Running_Details fragment_running_details = new Fragment_Running_Details();
        fragment_running_details.setCallBack_map(callBack_map);
        fragment_running_details.setCallBack_run_mode(callBack_run_mode);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.running_LAY_details, fragment_running_details)
                .commit();

        fragment_map = new Fragment_Map();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.running_LAY_map, fragment_map)
                .commit();
    }

    public void setToolBar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(!runningStartMode);
    }

    private final CallBack_Map callBack_map = new CallBack_Map() {
        @Override
        public void updateMap(ArrayList<Location> locations) {
            fragment_map.updateMap(locations);
        }
    };

    private final CallBack_Run_Mode callBack_run_mode = new CallBack_Run_Mode() {
        @Override
        public void setRunMode(boolean startMode) {
            runningStartMode = startMode;
            setToolBar();
        }
    };

}