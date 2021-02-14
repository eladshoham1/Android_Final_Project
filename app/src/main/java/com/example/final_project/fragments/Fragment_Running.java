package com.example.final_project.fragments;

import android.location.Location;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.final_project.R;
import com.example.final_project.callbacks.CallBack_Map;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;

public class Fragment_Running extends Fragment {
    private Fragment_Map fragment_map;
    private Fragment_Running_Details fragment_running_details;

    //private LocationCallback locationCallback;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_running, container, false);
        findViews(view);

        /*locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    // Update UI with location data
                    // ...
                }
            }
        };*/

        //updateValuesFromBundle(savedInstanceState);

        return view;
    }

    private void findViews(View view) {
        fragment_running_details = new Fragment_Running_Details();
        fragment_running_details.setCallBack_map(callBack_map);
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.running_LAY_details, fragment_running_details)
                .commit();

        fragment_map = new Fragment_Map();
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.running_LAY_map, fragment_map)
                .commit();
    }

    /*@Override
    protected void onResume() {
        super.onResume();
        if (requestingLocationUpdates) {
            startLocationUpdates();
        }
    }*/

    /*private void updateValuesFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            return;
        }

        // Update the value of requestingLocationUpdates from the Bundle.
        if (savedInstanceState.keySet().contains(REQUESTING_LOCATION_UPDATES_KEY)) {
            requestingLocationUpdates = savedInstanceState.getBoolean(
                    REQUESTING_LOCATION_UPDATES_KEY);
        }

        // ...

        // Update UI to match restored state
        updateUI();
    }

    private void startLocationUpdates() {
        fusedLocationClient.requestLocationUpdates(locationRequest,
                locationCallback,
                Looper.getMainLooper());
    }

    private void stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }*/






    private CallBack_Map callBack_map = new CallBack_Map() {
        @Override
        public void addMarker(double latitude, double longitude) {
            fragment_map.addMarker(latitude, longitude);
        }
    };

}