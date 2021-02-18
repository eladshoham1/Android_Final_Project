package com.example.final_project.fragments.achievements;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.final_project.R;
import com.example.final_project.objects.Run;
import com.example.final_project.objects.Statistics;
import com.example.final_project.objects.User;
import com.example.final_project.utils.Constants;
import com.example.final_project.utils.MyDB;
import com.example.final_project.utils.MySP;
import com.example.final_project.utils.MySignal;
import com.example.final_project.utils.MyStrings;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;

public class Fragment_Statistics extends Fragment {
    private TextView statistics_LBL_averageTime;
    private TextView statistics_LBL_maxTime;
    private TextView statistics_LBL_averageSpeed;
    private TextView statistics_LBL_maxSpeed;
    private TextView statistics_LBL_averageDistance;
    private TextView statistics_LBL_maxDistance;
    private TextView statistics_LBL_averageCalories;
    private TextView statistics_LBL_maxCalories;
    private TextView statistics_LBL_totalTime;
    private TextView statistics_LBL_totalDistance;
    private TextView statistics_LBL_totalCalories;

    private User user;
    private Statistics statistics;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);
        findViews(view);
        initUser();
        readStatistics();

        return view;
    }

    private void findViews(View view) {
        statistics_LBL_averageTime = view.findViewById(R.id.statistics_LBL_averageTime);
        statistics_LBL_maxTime = view.findViewById(R.id.statistics_LBL_maxTime);
        statistics_LBL_averageSpeed = view.findViewById(R.id.statistics_LBL_averageSpeed);
        statistics_LBL_maxSpeed = view.findViewById(R.id.statistics_LBL_maxSpeed);
        statistics_LBL_averageDistance = view.findViewById(R.id.statistics_LBL_averageDistance);
        statistics_LBL_maxDistance = view.findViewById(R.id.statistics_LBL_maxDistance);
        statistics_LBL_averageCalories = view.findViewById(R.id.statistics_LBL_averageCalories);
        statistics_LBL_maxCalories = view.findViewById(R.id.statistics_LBL_maxCalories);
        statistics_LBL_totalTime = view.findViewById(R.id.statistics_LBL_totalTime);
        statistics_LBL_totalDistance = view.findViewById(R.id.statistics_LBL_totalDistance);
        statistics_LBL_totalCalories = view.findViewById(R.id.statistics_LBL_totalCalories);
    }

    private void initUser() {
        String userString = MySP.getInstance().getString(MySP.KEYS.USER_DATA, "");
        user = new Gson().fromJson(userString, User.class);
    }

    private void readStatistics() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(Constants.STATISTICS_DB);

        myRef.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                statistics = dataSnapshot.getValue(Statistics.class);

                if (statistics == null) {
                    statistics = new Statistics();
                }

                updateView();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                MySignal.getInstance().toast("Failed to read the all users data");
            }
        });
    }

    /*private void calculateStatistics(ArrayList<Run> allRuns) { //TODO duplicated code
        double speed = 0.0, distance = 0.0;
        long time = 0;
        int calories = 0, numOfRuns = allRuns.size();

        if (numOfRuns == 0) {
            return;
        }

        for (Run run : allRuns) {
            time = run.getDuration();
            totalTime += time;
            if (time > maxTime)
                maxTime = time;

            speed = run.getMaxSpeed();
            totalAverageSpeed += run.getAverageSpeed();
            if (speed > maxSpeed)
                maxSpeed = speed;

            distance = run.getDistance();
            totalDistance += distance;
            if (distance > maxDistance)
                maxDistance = distance;

            calories = run.getCalories();
            totalCalories += calories;
            if (calories > maxCalories)
                maxCalories = calories;

            totalAverageSpeed += run.getAverageSpeed();
            totalDistance += run.getDistance();
            totalCalories += run.getCalories();
        }

        averageTime = totalTime / numOfRuns;
        averageSpeed = totalAverageSpeed / numOfRuns;
        averageDistance = totalDistance / numOfRuns;
        averageCalories = totalCalories / numOfRuns;

        updateView();
    }*/

    private void updateView() {
        statistics_LBL_averageTime.setText(MyStrings.makeDateString(statistics.getAverageTime()));
        statistics_LBL_maxTime.setText(MyStrings.makeDateString(statistics.getMaxTime()));
        statistics_LBL_totalTime.setText(MyStrings.makeDateString(statistics.getTotalTime()));
        statistics_LBL_averageSpeed.setText(MyStrings.twoNumbersAfterPoint(statistics.getAverageSpeed()));
        statistics_LBL_maxSpeed.setText(MyStrings.twoNumbersAfterPoint(statistics.getMaxSpeed()));
        statistics_LBL_averageDistance.setText(MyStrings.twoNumbersAfterPoint(statistics.getAverageDistance()));
        statistics_LBL_maxDistance.setText(MyStrings.twoNumbersAfterPoint(statistics.getMaxDistance()));
        statistics_LBL_totalDistance.setText(MyStrings.twoNumbersAfterPoint(statistics.getTotalDistance()));
        statistics_LBL_averageCalories.setText("" + statistics.getAverageCalories());
        statistics_LBL_maxCalories.setText("" + statistics.getMaxCalories());
        statistics_LBL_totalCalories.setText("" + statistics.getTotalCalories());
    }
}
