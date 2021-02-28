package com.example.final_project.fragments.achievements;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.final_project.R;
import com.example.final_project.callbacks.CallBack_Runs;
import com.example.final_project.objects.Run;
import com.example.final_project.objects.Statistics;
import com.example.final_project.utils.MyDB;
import com.example.final_project.utils.MySignal;
import com.example.final_project.utils.MyStrings;

import java.util.ArrayList;

public class Fragment_Statistics extends Fragment {
    private TextView statistics_LBL_numOfRuns;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);
        findViews(view);
        readStatistics();

        return view;
    }

    private void findViews(View view) {
        statistics_LBL_numOfRuns = view.findViewById(R.id.statistics_LBL_numOfRuns);
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

    private void readStatistics() {
        MyDB.readMyRunsData(new CallBack_Runs() {
            @Override
            public void onRunsReady(ArrayList<Run> allRuns) {
                Statistics statistics = new Statistics();
                statistics.calculateStatistics(allRuns);
                updateView(statistics);
            }

            @Override
            public void onRunsFailure(String msg) {
                MySignal.getInstance().toast(msg);
            }
        });
    }

    private void updateView(Statistics statistics) {
        statistics_LBL_numOfRuns.setText("" + statistics.getNumOfRuns());
        statistics_LBL_averageTime.setText(MyStrings.makeDurationString(statistics.getAverageTime()));
        statistics_LBL_maxTime.setText(MyStrings.makeDurationString(statistics.getMaxTime()));
        statistics_LBL_totalTime.setText(MyStrings.makeDurationString(statistics.getTotalTime()));
        statistics_LBL_averageSpeed.setText(MyStrings.twoDigitsAfterPoint(statistics.getAverageSpeed()));
        statistics_LBL_maxSpeed.setText(MyStrings.twoDigitsAfterPoint(statistics.getMaxSpeed()));
        statistics_LBL_averageDistance.setText(MyStrings.threeDigitsAfterPoint(statistics.getAverageDistance()));
        statistics_LBL_maxDistance.setText(MyStrings.threeDigitsAfterPoint(statistics.getMaxDistance()));
        statistics_LBL_totalDistance.setText(MyStrings.threeDigitsAfterPoint(statistics.getTotalDistance()));
        statistics_LBL_averageCalories.setText("" + statistics.getAverageCalories());
        statistics_LBL_maxCalories.setText("" + statistics.getMaxCalories());
        statistics_LBL_totalCalories.setText("" + statistics.getTotalCalories());
    }
}
