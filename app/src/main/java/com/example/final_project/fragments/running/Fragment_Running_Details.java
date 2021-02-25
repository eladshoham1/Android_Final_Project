package com.example.final_project.fragments.running;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.final_project.R;
import com.example.final_project.activities.Activity_Run_Details;
import com.example.final_project.callbacks.CallBack_Map;
import com.example.final_project.objects.Run;
import com.example.final_project.utils.Constants;
import com.example.final_project.utils.LocationService;
import com.example.final_project.utils.MyDB;
import com.example.final_project.utils.MySP;
import com.example.final_project.utils.MySignal;
import com.example.final_project.utils.MyStrings;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;

import java.util.ArrayList;

public class Fragment_Running_Details extends Fragment {

    private enum RUNNING_STATE {
        START,
        PAUSE,
        CONTINUE,
        STOP
    }

    private Context context;
    private MaterialButton running_BTN_start;
    private MaterialButton running_BTN_pause;
    private MaterialButton running_BTN_continue;
    private MaterialButton running_BTN_stop;
    private TextView running_LBL_duration;
    private TextView running_LBL_distance;
    private TextView running_LBL_speed;
    private TextView running_LBL_calories;
    private LinearLayout running_LAY_loadingLocation;

    private CallBack_Map callBack_map;

    private RUNNING_STATE running_state;

    private Intent serviceIntent;
    private Handler handler = new Handler();
    private long startTime = 0L;
    private long duration = 0L;
    private long timeInMilliseconds = 0L;
    private long timeSwapBuff = 0L;

    private Run run;
    private ArrayList<Location> locations = null;
    private double totalDistance = 0.0;
    private double maxSpeed = 0.0;
    private int calories = 0;

    public void setCallBack_map(CallBack_Map callBack_map) {
        this.callBack_map = callBack_map;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_running_details, container, false);
        context = view.getContext();
        findViews(view);
        initViews();

        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == Constants.LOCATION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startService();
            } else {
                MySignal.getInstance().toast("You need to allow location");
            }
        }
    }

    private void findViews(View view) {
        running_BTN_start = view.findViewById(R.id.running_BTN_start);
        running_BTN_pause = view.findViewById(R.id.running_BTN_pause);
        running_BTN_continue = view.findViewById(R.id.running_BTN_continue);
        running_BTN_stop = view.findViewById(R.id.running_BTN_stop);
        running_LBL_duration = view.findViewById(R.id.running_LBL_duration);
        running_LBL_distance = view.findViewById(R.id.running_LBL_distance);
        running_LBL_speed = view.findViewById(R.id.running_LBL_speed);
        running_LBL_calories = view.findViewById(R.id.running_LBL_calories);
        running_LAY_loadingLocation = view.findViewById(R.id.running_LAY_loadingLocation);
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

    private void updateView() {
        switch (running_state) {
            case START:
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
                running_BTN_continue.setVisibility(View.GONE);
                running_BTN_pause.setVisibility(View.GONE);
                running_BTN_stop.setVisibility(View.GONE);
                break;
        }
    }

    private void startRunning() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[] { Manifest.permission.ACCESS_FINE_LOCATION }, Constants.LOCATION_REQUEST_CODE);
            } else {
                startService();
            }
        } else {
            startService();
        }
    }

    private void pauseRunning() {
        timeSwapBuff += timeInMilliseconds;
        running_state = RUNNING_STATE.PAUSE;
        updateView();
        stop();
    }

    private void continueRunning() {
        startTime = System.currentTimeMillis();
        running_state = RUNNING_STATE.CONTINUE;
        updateView();
        start();
    }

    private void stopRunning() {
        stop();
        running_state = RUNNING_STATE.STOP;
        updateView();
        endRun();
    }

    private void startService() {
        if (!statusCheck()) {
            return;
        }

        running_state = RUNNING_STATE.START;
        running_BTN_start.setVisibility(View.GONE);
        running_LAY_loadingLocation.setVisibility(View.VISIBLE);

        LocationBroadcastReceiver receiver = new LocationBroadcastReceiver();
        IntentFilter filter = new IntentFilter(Constants.ACT_LOC);
        context.registerReceiver(receiver, filter);
        serviceIntent = new Intent(context, LocationService.class);
        getActivity().startService(serviceIntent);
    }

    public boolean statusCheck() {
        final LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            MySignal.getInstance().toast("You must turn on location in order to run");
            return false;
        }

        return true;
    }

    private void startTimer() {
        startTime = System.currentTimeMillis();
        run = new Run().setStartTime(startTime);
        start();

        running_state = RUNNING_STATE.START;
        updateView();
    }

    private Runnable runnable = new Runnable() {

        public void run() {
            runTimer();
        }
    };

    private void runTimer() {
        timeInMilliseconds = System.currentTimeMillis() - startTime;
        duration = timeSwapBuff + timeInMilliseconds;
        running_LBL_duration.setText(MyStrings.makeDurationString(duration));
        handler.postDelayed(runnable, 0);
    }

    private void start() {
        handler.postDelayed(runnable, 0);
    }

    private void stop() {
        handler.removeCallbacks(runnable);
    }

    private void updateRunDetails(Location currentLocation) {
        double distance, speed = 0.0;

        if (locations == null) {
            MySignal.getInstance().playSound(R.raw.snd_start_running);
            running_LAY_loadingLocation.setVisibility(View.GONE);
            startTimer();
            locations = new ArrayList<>();
        } else {
            distance = getDistance(currentLocation);
            totalDistance += distance;
            speed = getSpeed(currentLocation, distance);
            calories = getCalories();

            if (speed > maxSpeed)
                maxSpeed = speed;
        }

        locations.add(currentLocation);
        updateMap();
        updateViewDetails(speed);
    }

    private void updateMap() {
        if (callBack_map != null) {
            callBack_map.updateMap(locations);
        }
    }

    private void updateViewDetails(double speed) {
        running_LBL_speed.setText(MyStrings.twoDigitsAfterPoint(speed));
        running_LBL_distance.setText(MyStrings.threeDigitsAfterPoint(totalDistance));
        running_LBL_calories.setText("" + calories);
    }

    private Location getLastLocation() {
        if (locations != null && locations.size() > 0) {
            return locations.get(locations.size() - 1);
        }

        return null;
    }

    private double getDistance(Location currentLocation) {
        return currentLocation.distanceTo(getLastLocation()) / Constants.METERS_TO_KILOMETERS;
    }

    private double getSpeed(Location currentLocation, double distance) {
        double timeInMilliseconds = currentLocation.getTime() - getLastLocation().getTime();
        double timeInHours = timeInMilliseconds / Constants.MILLISECOND_TO_HOURS;
        double speed = 0.0;

        if (timeInHours != 0) {
            speed = distance / timeInHours;
        }

        return speed;
    }

    private int getCalories() {
        float weight = MySP.getInstance().getFloat(MySP.KEYS.MY_WEIGHT, 0f);
        return (int) Math.floor(Constants.CALORIES_COEFFICIENT * weight * totalDistance);
    }

    private void endRun() {
        getActivity().stopService(serviceIntent);
        MySignal.getInstance().playSound(R.raw.snd_end_running);
        setRunDetails();
        MyDB.updateRun(run);
        openRunDetailsActivity(run);
    }

    private Run setRunDetails() {
        double timeInHours = duration / Constants.MILLISECOND_TO_HOURS;
        double averageSpeed = totalDistance / timeInHours;

        run.setEndTime(System.currentTimeMillis())
                .setDuration(duration)
                .setDistance(totalDistance)
                .setAverageSpeed(averageSpeed)
                .setMaxSpeed(maxSpeed)
                .setCalories(calories);

        return run;
    }

    private void openRunDetailsActivity(Run run) {
        Intent myIntent = new Intent(context, Activity_Run_Details.class);
        myIntent.putExtra(Constants.EXTRA_RUN_DETAILS, new Gson().toJson(run));
        myIntent.putExtra(Constants.EXTRA_OPEN_FROM_RUNNING, true);
        startActivity(myIntent);
        getActivity().finish();
    }

    public class LocationBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (running_state == RUNNING_STATE.PAUSE || running_state == RUNNING_STATE.STOP) {
                return;
            }

            if (intent.getAction().equals(Constants.ACT_LOC)) {
                double latitude = intent.getDoubleExtra(Constants.LATITUDE, 0.0);
                double longitude = intent.getDoubleExtra(Constants.LONGITUDE, 0.0);
                long durationTime = intent.getLongExtra(Constants.DURATION, 0L);

                Location currentLocation = new Location("");
                currentLocation.setLatitude(latitude);
                currentLocation.setLongitude(longitude);
                currentLocation.setTime(durationTime);

                updateRunDetails(currentLocation);
            }
        }

    }

}
