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
import com.example.final_project.objects.User;
import com.example.final_project.utils.Constants;
import com.example.final_project.utils.LocationService;
import com.example.final_project.utils.MySP;
import com.example.final_project.utils.MySignal;
import com.example.final_project.utils.MyStrings;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

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
    private TextView running_LBL_distance;
    private TextView running_LBL_speed;
    private TextView running_LBL_calories;
    private LinearLayout running_LAY_loadingLocation;

    private CallBack_Map callBack_map;

    private RUNNING_STATE running_state;

    private Handler handler = new Handler();
    private long startTime = 0L;
    private long duration = 0L;
    private long timeInMilliseconds = 0L;
    private long timeSwapBuff = 0L;

    private User user;
    private Location previousLocation = null;
    private double totalDistance = 0.0;
    private double speed = 0.0;
    private double maxSpeed = 0.0;
    private int calories = 0;

    public void setCallBack_map(CallBack_Map callBack_map) {
        this.callBack_map = callBack_map;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_running_details, container, false);
        findViews(view);
        initViews();
        initUser();

        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 1:
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

    private void initUser() {
        String userString = MySP.getInstance().getString(MySP.KEYS.USER_DATA, "");
        user = new Gson().fromJson(userString, User.class);
    }

    private void startService() {
        if (!statusCheck()) {
            return;
        }

        running_BTN_start.setVisibility(View.GONE);
        running_LAY_loadingLocation.setVisibility(View.VISIBLE);

        LocationBroadcastReceiver receiver = new LocationBroadcastReceiver();
        IntentFilter filter = new IntentFilter(Constants.ACT_LOC);
        getContext().registerReceiver(receiver, filter);
        Intent intent = new Intent(getContext(), LocationService.class);
        getContext().startService(intent);
    }

    public boolean statusCheck() {
        final LocationManager manager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            MySignal.getInstance().toast("You must turn on location in order to run");
            return false;
        }

        return true;
    }

    private void startTimer() {
        startTime = System.currentTimeMillis();
        start();

        running_state = RUNNING_STATE.START;
        updateView();
    }

    private void startRunning() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[] { Manifest.permission.ACCESS_FINE_LOCATION }, 1);
            } else {
                startService();
            }
        } else {
            startService();
        }
    }

    private void pauseRunning() {
        timeSwapBuff += timeInMilliseconds;
        stop();

        running_state = RUNNING_STATE.PAUSE;
        updateView();
    }

    private void continueRunning() {
        startTime = System.currentTimeMillis();
        start();

        running_state = RUNNING_STATE.CONTINUE;
        updateView();
    }

    private void stopRunning() {
        running_state = RUNNING_STATE.STOP;
        updateView();

        stop();
        endRun();

        timeInMilliseconds = 0L;
        timeSwapBuff = 0L;
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

    private void endRun() {
        MySignal.getInstance().playSound(R.raw.snd_end_running);
        Run run = getRunDetails();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(Constants.RUNS_DB);
        myRef.child(user.getUid()).child("" + run.getStartTime()).setValue(run);

        openRunDetailsActivity(run);
    }

    private void openRunDetailsActivity(Run run) {
        Intent myIntent = new Intent(getContext(), Activity_Run_Details.class);
        myIntent.putExtra(Constants.EXTRA_RUN_DETAILS, new Gson().toJson(run));
        startActivity(myIntent);
        getActivity().finish();
    }

    private Run getRunDetails() {
        double averageSpeed = totalDistance / (duration / (1000.0 * 60.0 * 60.0));

        Run run = new Run()
                .setStartTime(startTime)
                .setDuration(duration)
                .setDistance(totalDistance)
                .setAverageSpeed(averageSpeed)
                .setMaxSpeed(maxSpeed)
                .setCalories(calories);

        return run;
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
        double distance;

        if (previousLocation == null) {
            MySignal.getInstance().playSound(R.raw.snd_start_running);
            running_LAY_loadingLocation.setVisibility(View.GONE);
            startTimer();
            previousLocation = new Location("");
        } else {
            distance = getDistance(currentLocation);
            totalDistance += distance;
            speed = currentLocation.getSpeed() * 3.6;
            calories = getCalories();

            if (speed > maxSpeed)
                maxSpeed = speed;
        }

        updateMap(currentLocation);
        updateViewDetails();
        setPreviousLocation(currentLocation);
    }

    private void setPreviousLocation(Location currentLocation) {
        previousLocation.setLatitude(currentLocation.getLatitude());
        previousLocation.setLongitude(currentLocation.getLongitude());
        previousLocation.setTime(currentLocation.getTime());
        previousLocation.setAccuracy(currentLocation.getAccuracy());
    }

    private void updateMap(Location currentLocation) {
        if (callBack_map != null) {
            callBack_map.addMarker(currentLocation.getLatitude(), currentLocation.getLongitude());
        }
    }

    private void updateViewDetails() {
        running_LBL_speed.setText(MyStrings.twoDigitsAfterPoint(speed));
        running_LBL_distance.setText(MyStrings.twoDigitsAfterPoint(totalDistance));
        running_LBL_calories.setText("" + calories);
    }

    private double getDistance(Location currentLocation) {
        return currentLocation.distanceTo(previousLocation) / 1000.0;
    }

    private int getCalories() {
        final double COEFFICIENT = 0.5474896193;

        return (int) Math.floor(COEFFICIENT * user.getWeight() * totalDistance);
    }

    public class LocationBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            onReceiveLocation(intent);
        }

        private void onReceiveLocation(Intent intent) {
            if (running_state == RUNNING_STATE.PAUSE || running_state == RUNNING_STATE.STOP) {
                return;
            }

            if (intent.getAction().equals(Constants.ACT_LOC)) {
                double latitude = intent.getDoubleExtra(Constants.LATITUDE, 0.0);
                double longitude = intent.getDoubleExtra(Constants.LONGITUDE, 0.0);
                long durationTime = intent.getLongExtra(Constants.DURATION, 0L);
                float speed = intent.getFloatExtra(Constants.SPEED, 0f);

                Location currentLocation = new Location("");
                currentLocation.setLatitude(latitude);
                currentLocation.setLongitude(longitude);
                currentLocation.setTime(durationTime);
                currentLocation.setSpeed(speed);

                updateRunDetails(currentLocation);
            }
        }
    }

}
