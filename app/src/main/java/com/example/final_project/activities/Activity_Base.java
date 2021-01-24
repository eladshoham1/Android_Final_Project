package com.example.final_project.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.final_project.utils.Constants;
import com.example.final_project.utils.MyScreenUtils;
import com.example.final_project.utils.MySignal;

public class Activity_Base extends AppCompatActivity {
    protected boolean isDoubleBackPressToClose = false;
    private long mBackPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        /*if (hasFocus) {
            MyScreenUtils.hideSystemUI2(this);
        }*/
    }

    @Override
    public void onBackPressed() {
        if (isDoubleBackPressToClose) {
            if (mBackPressed + Constants.TIME_INTERVAL > System.currentTimeMillis()) {
                super.onBackPressed();
                return;
            } else {
                MySignal.getInstance().toast("Tap back button again to exit");
            }

            mBackPressed = System.currentTimeMillis();
        } else {
            super.onBackPressed();
        }
    }

    protected void closeActivity() {
        finish();
    }
}