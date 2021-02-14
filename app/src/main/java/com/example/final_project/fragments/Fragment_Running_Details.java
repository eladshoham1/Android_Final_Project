package com.example.final_project.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.final_project.R;
import com.example.final_project.callbacks.CallBack_Map;
import com.example.final_project.objects.Run;
import com.example.final_project.objects.User;
import com.example.final_project.utils.Constants;
import com.example.final_project.utils.MySignal;
import com.example.final_project.utils.database.MyDB;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Fragment_Running_Details extends Fragment {

    private enum RUNNING_STATE {
        START,
        PAUSE,
        CONTINUE,
        STOP
    }

    private MaterialButton running_BTN_start;
    private MaterialButton running_BTN_pause;
    private MaterialButton running_BTN_continue;
    private MaterialButton running_BTN_stop;
    private TextView running_LBL_duration;

    private CallBack_Map callBack_map;

    private Handler customHandler = new Handler();
    private long startTime = 0L;
    private long timeInMilliseconds = 0L;
    private long timeSwapBuff = 0L;
    private long updatedTime = 0L;

    private double distance = 0.0;
    private double averageSpeed = 0.0;
    private double maxSpeed = 0.0;
    private double calories = 0.0;
    private Run run;
    private User user;

    public void setCallBack_map(CallBack_Map callBack_map) {
        this.callBack_map = callBack_map;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_running_details, container, false);
        findViews(view);
        initViews();

        return view;
    }

    private void findViews(View view) {
        running_BTN_start = view.findViewById(R.id.running_BTN_start);
        running_BTN_pause = view.findViewById(R.id.running_BTN_pause);
        running_BTN_continue = view.findViewById(R.id.running_BTN_continue);
        running_BTN_stop = view.findViewById(R.id.running_BTN_stop);
        running_LBL_duration = view.findViewById(R.id.running_LBL_duration);
    }

    private void initViews() {
        user = MyDB.getInstance().getUserData();

        running_BTN_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRunning();
            }
        });

        running_BTN_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseRunning();
            }
        });

        running_BTN_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                continueRunning();
            }
        });

        running_BTN_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopRunning();
            }
        });
    }

    private void startRunning() {
        if (callBack_map != null) {
            callBack_map.addMarker(30 , 32);
        }

        startTime = SystemClock.uptimeMillis();
        customHandler.postDelayed(updateTimerThread, 0);
        updateView(RUNNING_STATE.START);
    }

    private void pauseRunning() {
        timeSwapBuff += timeInMilliseconds;
        customHandler.removeCallbacks(updateTimerThread);
        updateView(RUNNING_STATE.PAUSE);
    }

    private void continueRunning() {
        startTime = SystemClock.uptimeMillis();
        customHandler.postDelayed(updateTimerThread, 0);
        updateView(RUNNING_STATE.CONTINUE);
    }

    private void stopRunning() {
        updateView(RUNNING_STATE.STOP);
        customHandler.removeCallbacksAndMessages(null);
        endRun();

        startTime = 0L;
        timeInMilliseconds = 0L;
        timeSwapBuff = 0L;
        updatedTime = 0L;
    }

    private void updateView(RUNNING_STATE state) {
        switch (state) {
            case START:
                running_BTN_start.setVisibility(View.GONE);
                running_BTN_pause.setVisibility(View.VISIBLE);
                running_BTN_stop.setVisibility(View.VISIBLE);
                break;
            case PAUSE:
                running_BTN_pause.setVisibility(View.GONE);
                running_BTN_continue.setVisibility(View.VISIBLE);
                break;
            case CONTINUE:
                running_BTN_pause.setVisibility(View.VISIBLE);
                running_BTN_continue.setVisibility(View.GONE);
                break;
            case STOP:
                running_BTN_start.setVisibility(View.VISIBLE);
                running_BTN_pause.setVisibility(View.GONE);
                running_BTN_stop.setVisibility(View.GONE);
                break;
        }
    }

    private void endRun() {
        run = new Run(timeInMilliseconds, updatedTime, distance, averageSpeed, maxSpeed, calories);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(Constants.RUNS_DB);
        myRef.child(user.getUid()).push().setValue(run);
    }

    private Runnable updateTimerThread = new Runnable() {

        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            updatedTime = timeSwapBuff + timeInMilliseconds;

            int secs = (int) (updatedTime / 1000);
            int mins = secs / 60;
            int hours = mins / 60;
            secs %= 60;
            mins %= 60;
            hours %= 24;
            running_LBL_duration.setText("" + String.format("%02d", hours) + ":"
                    + String.format("%02d", mins) + ":"
                    + String.format("%02d", secs));
            customHandler.postDelayed(this, 0);
        }
    };

}