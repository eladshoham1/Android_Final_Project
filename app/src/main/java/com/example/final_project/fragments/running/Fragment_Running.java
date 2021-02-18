package com.example.final_project.fragments.running;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.final_project.R;
import com.example.final_project.callbacks.CallBack_Map;

public class Fragment_Running extends Fragment {
    private Fragment_Map fragment_map;
    private Fragment_Running_Details fragment_running_details;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_running, container, false);
        findViews(view);

        return view;
    }

    private void findViews(View view) {
        fragment_running_details = new Fragment_Running_Details();
        fragment_running_details.setCallBack_map(callBack_map);
        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.running_LAY_details, fragment_running_details)
                .commit();

        fragment_map = new Fragment_Map();
        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.running_LAY_map, fragment_map)
                .commit();
    }

    private CallBack_Map callBack_map = new CallBack_Map() {
        @Override
        public void addMarker(double latitude, double longitude) {
            fragment_map.addMarker(latitude, longitude);
        }
    };

}