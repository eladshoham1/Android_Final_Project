package com.example.final_project.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.final_project.R;
import com.example.final_project.callbacks.CallBack_Runs;
import com.example.final_project.objects.Run;
import com.example.final_project.objects.Statistics;
import com.example.final_project.utils.Constants;
import com.example.final_project.utils.MyDB;
import com.example.final_project.utils.MySignal;
import com.example.final_project.utils.MyStrings;

import java.util.ArrayList;

public class Activity_Compete_Friend extends AppCompatActivity {
    private ProgressBar compete_friend_PRG_numberOfRuns;
    private ProgressBar compete_friend_PRG_averageTime;
    private ProgressBar compete_friend_PRG_maxTime;
    private ProgressBar compete_friend_PRG_totalTime;
    private ProgressBar compete_friend_PRG_averageSpeed;
    private ProgressBar compete_friend_PRG_maxSpeed;
    private ProgressBar compete_friend_PRG_averageDistance;
    private ProgressBar compete_friend_PRG_maxDistance;
    private ProgressBar compete_friend_PRG_totalDistance;
    private ProgressBar compete_friend_PRG_averageCalories;
    private ProgressBar compete_friend_PRG_maxCalories;
    private ProgressBar compete_friend_PRG_totalCalories;
    private TextView compete_friend_LBL_myNumberOfRuns;
    private TextView compete_friend_LBL_friendNumberOfRuns;
    private TextView compete_friend_LBL_myAverageTime;
    private TextView compete_friend_LBL_friendAverageTime;
    private TextView compete_friend_LBL_myMaxTime;
    private TextView compete_friend_LBL_friendMaxTime;
    private TextView compete_friend_LBL_myTotalTime;
    private TextView compete_friend_LBL_friendTotalTime;
    private TextView compete_friend_LBL_myAverageSpeed;
    private TextView compete_friend_LBL_friendAverageSpeed;
    private TextView compete_friend_LBL_myMaxSpeed;
    private TextView compete_friend_LBL_friendMaxSpeed;
    private TextView compete_friend_LBL_myAverageDistance;
    private TextView compete_friend_LBL_friendAverageDistance;
    private TextView compete_friend_LBL_myMaxDistance;
    private TextView compete_friend_LBL_friendMaxDistance;
    private TextView compete_friend_LBL_myTotalDistance;
    private TextView compete_friend_LBL_friendTotalDistance;
    private TextView compete_friend_LBL_myAverageCalories;
    private TextView compete_friend_LBL_friendAverageCalories;
    private TextView compete_friend_LBL_myMaxCalories;
    private TextView compete_friend_LBL_friendMaxCalories;
    private TextView compete_friend_LBL_myTotalCalories;
    private TextView compete_friend_LBL_friendTotalCalories;
    private Toolbar compete_friend_TLB_toolbar;

    private Statistics myStatistics;
    private Statistics friendStatistics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compete_friend);

        findViews();
        initViews();
        readStatistics();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

    private void findViews() {
        compete_friend_PRG_numberOfRuns = findViewById(R.id.compete_friend_PRG_numberOfRuns);
        compete_friend_PRG_averageTime = findViewById(R.id.compete_friend_PRG_averageTime);
        compete_friend_PRG_maxTime = findViewById(R.id.compete_friend_PRG_maxTime);
        compete_friend_PRG_totalTime = findViewById(R.id.compete_friend_PRG_totalTime);
        compete_friend_PRG_averageSpeed = findViewById(R.id.compete_friend_PRG_averageSpeed);
        compete_friend_PRG_maxSpeed = findViewById(R.id.compete_friend_PRG_maxSpeed);
        compete_friend_PRG_averageDistance = findViewById(R.id.compete_friend_PRG_averageDistance);
        compete_friend_PRG_maxDistance = findViewById(R.id.compete_friend_PRG_maxDistance);
        compete_friend_PRG_totalDistance = findViewById(R.id.compete_friend_PRG_totalDistance);
        compete_friend_PRG_averageCalories = findViewById(R.id.compete_friend_PRG_averageCalories);
        compete_friend_PRG_maxCalories = findViewById(R.id.compete_friend_PRG_maxCalories);
        compete_friend_PRG_totalCalories = findViewById(R.id.compete_friend_PRG_totalCalories);
        compete_friend_LBL_myNumberOfRuns = findViewById(R.id.compete_friend_LBL_myNumberOfRuns);
        compete_friend_LBL_friendNumberOfRuns = findViewById(R.id.compete_friend_LBL_friendNumberOfRuns);
        compete_friend_LBL_myAverageTime = findViewById(R.id.compete_friend_LBL_myAverageTime);
        compete_friend_LBL_friendAverageTime = findViewById(R.id.compete_friend_LBL_friendAverageTime);
        compete_friend_LBL_myMaxTime = findViewById(R.id.compete_friend_LBL_myMaxTime);
        compete_friend_LBL_friendMaxTime = findViewById(R.id.compete_friend_LBL_friendMaxTime);
        compete_friend_LBL_myNumberOfRuns = findViewById(R.id.compete_friend_LBL_myNumberOfRuns);
        compete_friend_LBL_friendNumberOfRuns = findViewById(R.id.compete_friend_LBL_friendNumberOfRuns);
        compete_friend_LBL_myAverageTime = findViewById(R.id.compete_friend_LBL_myAverageTime);
        compete_friend_LBL_friendAverageTime = findViewById(R.id.compete_friend_LBL_friendAverageTime);
        compete_friend_LBL_myMaxTime = findViewById(R.id.compete_friend_LBL_myMaxTime);
        compete_friend_LBL_friendMaxTime = findViewById(R.id.compete_friend_LBL_friendMaxTime);
        compete_friend_LBL_myTotalTime = findViewById(R.id.compete_friend_LBL_myTotalTime);
        compete_friend_LBL_friendTotalTime = findViewById(R.id.compete_friend_LBL_friendTotalTime);
        compete_friend_LBL_myAverageSpeed = findViewById(R.id.compete_friend_LBL_myAverageSpeed);
        compete_friend_LBL_friendAverageSpeed = findViewById(R.id.compete_friend_LBL_friendAverageSpeed);
        compete_friend_LBL_myMaxSpeed = findViewById(R.id.compete_friend_LBL_myMaxSpeed);
        compete_friend_LBL_friendMaxSpeed = findViewById(R.id.compete_friend_LBL_friendMaxSpeed);
        compete_friend_LBL_myAverageDistance = findViewById(R.id.compete_friend_LBL_myAverageDistance);
        compete_friend_LBL_friendAverageDistance = findViewById(R.id.compete_friend_LBL_friendAverageDistance);
        compete_friend_LBL_myMaxDistance = findViewById(R.id.compete_friend_LBL_myMaxDistance);
        compete_friend_LBL_friendMaxDistance = findViewById(R.id.compete_friend_LBL_friendMaxDistance);
        compete_friend_LBL_myTotalDistance = findViewById(R.id.compete_friend_LBL_myTotalDistance);
        compete_friend_LBL_friendTotalDistance = findViewById(R.id.compete_friend_LBL_friendTotalDistance);
        compete_friend_LBL_myAverageCalories = findViewById(R.id.compete_friend_LBL_myAverageCalories);
        compete_friend_LBL_friendAverageCalories = findViewById(R.id.compete_friend_LBL_friendAverageCalories);
        compete_friend_LBL_myMaxCalories = findViewById(R.id.compete_friend_LBL_myMaxCalories);
        compete_friend_LBL_friendMaxCalories = findViewById(R.id.compete_friend_LBL_friendMaxCalories);
        compete_friend_LBL_myTotalCalories = findViewById(R.id.compete_friend_LBL_myTotalCalories);
        compete_friend_LBL_friendTotalCalories = findViewById(R.id.compete_friend_LBL_friendTotalCalories);
        compete_friend_TLB_toolbar = findViewById(R.id.compete_friend_TLB_toolbar);
    }

    private void initViews() {
        setSupportActionBar(compete_friend_TLB_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void readStatistics() {
        MyDB.readMyRunsData(new CallBack_Runs() {
            @Override
            public void onRunsReady(ArrayList<Run> allRuns) {
                myStatistics = new Statistics();
                myStatistics.calculateStatistics(allRuns);

                readFriendStatistics();
            }

            @Override
            public void onRunsFailure(String msg) {
                MySignal.getInstance().toast(msg);
            }
        });
    }

    private void readFriendStatistics() {
        String friendID = getIntent().getStringExtra(Constants.EXTRA_FRIEND_KEY);

        MyDB.readRunsData(friendID, new CallBack_Runs() {
            @Override
            public void onRunsReady(ArrayList<Run> allRuns) {
                friendStatistics = new Statistics();
                friendStatistics.calculateStatistics(allRuns);

                updateView();
            }

            @Override
            public void onRunsFailure(String msg) {
                MySignal.getInstance().toast(msg);
            }
        });
    }

    private void updateView() {
        updateNumberOfRuns();
        updateAverageTime();
        updateMaxTime();
        updateTotalTime();
        updateAverageSpeed();
        updateMaxSpeed();
        updateAverageDistance();
        updateMaxDistance();
        updateTotalDistance();
        updateAverageCalories();
        updateMaxCalories();
        updateTotalCalories();
    }

    private void updateNumberOfRuns() {
        int myNumOfRuns = myStatistics.getNumOfRuns();
        int friendNumOfRuns = friendStatistics.getNumOfRuns();
        int sumNumOfRuns = myNumOfRuns + friendNumOfRuns;

        compete_friend_LBL_myNumberOfRuns.setText("" + myNumOfRuns);
        compete_friend_LBL_friendNumberOfRuns.setText("" + friendNumOfRuns);

        if (sumNumOfRuns != 0) {
            int progress = (int) Math.round(((double)myNumOfRuns / (double)sumNumOfRuns) * Constants.FULL_PERCENTAGE);
            compete_friend_PRG_numberOfRuns.setProgress(progress);
        } else {
            compete_friend_PRG_numberOfRuns.setProgress(Constants.EQUAL_PERCENTAGE);
        }
    }

    private void updateAverageTime() {
        long myAverageTime = myStatistics.getAverageTime();
        long friendAverageTime = friendStatistics.getAverageTime();
        long sumAverageTime = myAverageTime + friendAverageTime;

        compete_friend_LBL_myAverageTime.setText(MyStrings.makeDurationString(myAverageTime));
        compete_friend_LBL_friendAverageTime.setText(MyStrings.makeDurationString(friendAverageTime));

        if (sumAverageTime != 0) {
            int progress = (int) Math.round(((double)myAverageTime / (double)sumAverageTime) * Constants.FULL_PERCENTAGE);
            compete_friend_PRG_averageTime.setProgress(progress);
        } else {
            compete_friend_PRG_averageTime.setProgress(Constants.EQUAL_PERCENTAGE);
        }
    }

    private void updateMaxTime() {
        long myMaxTime = myStatistics.getMaxTime();
        long friendMaxTime = friendStatistics.getMaxTime();
        long sumMaxTime = myMaxTime + friendMaxTime;

        compete_friend_LBL_myMaxTime.setText(MyStrings.makeDurationString(myMaxTime));
        compete_friend_LBL_friendMaxTime.setText(MyStrings.makeDurationString(friendMaxTime));

        if (sumMaxTime != 0) {
            int progress = (int) Math.round(((double)myMaxTime / (double)sumMaxTime) * Constants.FULL_PERCENTAGE);
            compete_friend_PRG_maxTime.setProgress(progress);
        } else {
            compete_friend_PRG_maxTime.setProgress(Constants.EQUAL_PERCENTAGE);
        }
    }

    private void updateTotalTime() {
        long myTotalTime = myStatistics.getTotalTime();
        long friendTotalTime = friendStatistics.getTotalTime();
        long sumTotalTime = myTotalTime + friendTotalTime;

        compete_friend_LBL_myTotalTime.setText(MyStrings.makeDurationString(myTotalTime));
        compete_friend_LBL_friendTotalTime.setText(MyStrings.makeDurationString(friendTotalTime));

        if (sumTotalTime != 0) {
            int progress = (int) Math.round(((double)myTotalTime / (double)sumTotalTime) * Constants.FULL_PERCENTAGE);
            compete_friend_PRG_totalTime.setProgress(progress);
        } else {
            compete_friend_PRG_totalTime.setProgress(Constants.EQUAL_PERCENTAGE);
        }
    }

    private void updateAverageSpeed() {
        double myAverageSpeed = myStatistics.getAverageSpeed();
        double friendAverageSpeed = friendStatistics.getAverageSpeed();
        double sumAverageSpeed = myAverageSpeed + friendAverageSpeed;

        compete_friend_LBL_myAverageSpeed.setText(MyStrings.twoDigitsAfterPoint(myAverageSpeed));
        compete_friend_LBL_friendAverageSpeed.setText(MyStrings.twoDigitsAfterPoint(friendAverageSpeed));

        if (sumAverageSpeed != 0) {
            int progress = (int) Math.round((myAverageSpeed / sumAverageSpeed) * Constants.FULL_PERCENTAGE);
            compete_friend_PRG_averageSpeed.setProgress(progress);
        } else {
            compete_friend_PRG_averageSpeed.setProgress(Constants.EQUAL_PERCENTAGE);
        }
    }

    private void updateMaxSpeed() {
        double myMaxSpeed = myStatistics.getMaxSpeed();
        double friendMaxSpeed = friendStatistics.getMaxSpeed();
        double sumMaxSpeed = myMaxSpeed + friendMaxSpeed;

        compete_friend_LBL_myMaxSpeed.setText(MyStrings.twoDigitsAfterPoint(myMaxSpeed));
        compete_friend_LBL_friendMaxSpeed.setText(MyStrings.twoDigitsAfterPoint(friendMaxSpeed));

        if (sumMaxSpeed != 0) {
            int progress = (int) Math.round((myMaxSpeed / sumMaxSpeed) * Constants.FULL_PERCENTAGE);
            compete_friend_PRG_maxSpeed.setProgress(progress);
        } else {
            compete_friend_PRG_maxSpeed.setProgress(Constants.EQUAL_PERCENTAGE);
        }
    }

    private void updateAverageDistance() {
        double myAverageDistance = myStatistics.getAverageDistance();
        double friendAverageDistance = friendStatistics.getAverageDistance();
        double sumAverageDistance = myAverageDistance + friendAverageDistance;

        compete_friend_LBL_myAverageDistance.setText(MyStrings.threeDigitsAfterPoint(myAverageDistance));
        compete_friend_LBL_friendAverageDistance.setText(MyStrings.threeDigitsAfterPoint(friendAverageDistance));

        if (sumAverageDistance != 0) {
            int progress = (int) Math.round((myAverageDistance / sumAverageDistance) * Constants.FULL_PERCENTAGE);
            compete_friend_PRG_averageDistance.setProgress(progress);
        } else {
            compete_friend_PRG_averageDistance.setProgress(Constants.EQUAL_PERCENTAGE);
        }
    }

    private void updateMaxDistance() {
        double myMaxDistance = myStatistics.getMaxDistance();
        double friendMaxDistance = friendStatistics.getMaxDistance();
        double sumMaxDistance = myMaxDistance + friendMaxDistance;

        compete_friend_LBL_myMaxDistance.setText(MyStrings.threeDigitsAfterPoint(myMaxDistance));
        compete_friend_LBL_friendMaxDistance.setText(MyStrings.threeDigitsAfterPoint(friendMaxDistance));

        if (sumMaxDistance != 0) {
            int progress = (int) Math.round((myMaxDistance / sumMaxDistance) * Constants.FULL_PERCENTAGE);
            compete_friend_PRG_maxDistance.setProgress(progress);
        } else {
            compete_friend_PRG_maxDistance.setProgress(Constants.EQUAL_PERCENTAGE);
        }
    }

    private void updateTotalDistance() {
        double myTotalDistance = myStatistics.getTotalDistance();
        double friendTotalDistance = friendStatistics.getTotalDistance();
        double sumTotalDistance = myTotalDistance + friendTotalDistance;

        compete_friend_LBL_myTotalDistance.setText(MyStrings.threeDigitsAfterPoint(myTotalDistance));
        compete_friend_LBL_friendTotalDistance.setText(MyStrings.threeDigitsAfterPoint(friendTotalDistance));

        if (sumTotalDistance != 0) {
            int progress = (int) Math.round((myTotalDistance / sumTotalDistance) * Constants.FULL_PERCENTAGE);
            compete_friend_PRG_totalDistance.setProgress(progress);
        } else {
            compete_friend_PRG_totalDistance.setProgress(Constants.EQUAL_PERCENTAGE);
        }
    }

    private void updateAverageCalories() {
        int myAverageCalories = myStatistics.getAverageCalories();
        int friendAverageCalories = friendStatistics.getAverageCalories();
        int sumAverageCalories = myAverageCalories + friendAverageCalories;

        compete_friend_LBL_myAverageCalories.setText("" + myAverageCalories);
        compete_friend_LBL_friendAverageCalories.setText("" + friendAverageCalories);

        if (sumAverageCalories != 0) {
            int progress = (int) Math.round(((double)myAverageCalories / (double)sumAverageCalories) * Constants.FULL_PERCENTAGE);
            compete_friend_PRG_averageCalories.setProgress(progress);
        } else {
            compete_friend_PRG_averageCalories.setProgress(Constants.EQUAL_PERCENTAGE);
        }
    }

    private void updateMaxCalories() {
        int myMaxCalories = myStatistics.getMaxCalories();
        int friendMaxCalories = friendStatistics.getMaxCalories();
        int sumMaxCalories = myMaxCalories + friendMaxCalories;

        compete_friend_LBL_myMaxCalories.setText("" + myMaxCalories);
        compete_friend_LBL_friendMaxCalories.setText("" + friendMaxCalories);

        if (sumMaxCalories != 0) {
            int progress = (int) Math.round(((double)myMaxCalories / (double)sumMaxCalories) * Constants.FULL_PERCENTAGE);
            compete_friend_PRG_maxCalories.setProgress(progress);
        } else {
            compete_friend_PRG_maxCalories.setProgress(Constants.EQUAL_PERCENTAGE);
        }
    }

    private void updateTotalCalories() {
        int myTotalCalories = myStatistics.getTotalCalories();
        int friendTotalCalories = friendStatistics.getTotalCalories();
        int sumTotalCalories = myTotalCalories + friendTotalCalories;

        compete_friend_LBL_myTotalCalories.setText("" + myTotalCalories);
        compete_friend_LBL_friendTotalCalories.setText("" + friendTotalCalories);

        if (sumTotalCalories != 0) {
            int progress = (int) Math.round(((double)myTotalCalories / (double)sumTotalCalories) * Constants.FULL_PERCENTAGE);
            compete_friend_PRG_totalCalories.setProgress(progress);
        } else {
            compete_friend_PRG_totalCalories.setProgress(Constants.EQUAL_PERCENTAGE);
        }
    }
}