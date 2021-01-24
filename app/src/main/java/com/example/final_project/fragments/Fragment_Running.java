package com.example.final_project.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.final_project.R;
import com.example.final_project.activities.Activity_Login;
import com.example.final_project.utils.MySignal;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.button.MaterialButton;

public class Fragment_Running extends Fragment {

    private enum RUNNING_STATE {
        START,
        PAUSE,
        STOP
    }

    private Fragment_Map fragment_map;
    private MaterialButton running_BTN_start;
    private MaterialButton running_BTN_pause;
    private MaterialButton running_BTN_stop;
    private RUNNING_STATE running_state = RUNNING_STATE.STOP;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_running, container, false);
        findViews(view);
        initViews();

        return view;
    }

    private void findViews(View view) {
        fragment_map = new Fragment_Map();
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.running_LAY_map, fragment_map)
                .commit();

        running_BTN_start = view.findViewById(R.id.running_BTN_start);
        running_BTN_pause = view.findViewById(R.id.running_BTN_pause);
        running_BTN_stop = view.findViewById(R.id.running_BTN_stop);
    }

    private void initViews() {
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

        running_BTN_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopRunning();
            }
        });
    }

    private void startRunning() {
        MySignal.getInstance().toast("start");
        updateView(RUNNING_STATE.START);
    }

    private void pauseRunning() {
        MySignal.getInstance().toast("pause");
        updateView(RUNNING_STATE.PAUSE);
    }

    private void stopRunning() {
        MySignal.getInstance().toast("stop");
        updateView(RUNNING_STATE.STOP);
    }

    private void updateView(RUNNING_STATE state) {
        running_state = state;

        switch (running_state) {
            case START:
                running_BTN_start.setVisibility(View.INVISIBLE);
                running_BTN_pause.setVisibility(View.VISIBLE);
                running_BTN_stop.setVisibility(View.VISIBLE);
                break;
            case PAUSE:
                // do nothing
                break;
            case STOP:
                running_BTN_start.setVisibility(View.VISIBLE);
                running_BTN_pause.setVisibility(View.INVISIBLE);
                running_BTN_stop.setVisibility(View.INVISIBLE);
                break;
        }
    }

}