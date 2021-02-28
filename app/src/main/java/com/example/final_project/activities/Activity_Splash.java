package com.example.final_project.activities;

import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.final_project.R;
import com.example.final_project.callbacks.CallBack_User;
import com.example.final_project.objects.User;
import com.example.final_project.utils.Constants;
import com.example.final_project.utils.MyDB;
import com.google.gson.Gson;

public class Activity_Splash extends Activity_Base {
    private ImageView splash_IMG_logo;
    private TextView splash_LBL_internetErrorMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        findViews();
        initViews();
    }

    private void findViews() {
        splash_IMG_logo = findViewById(R.id.splash_IMG_logo);
        splash_LBL_internetErrorMessage = findViewById(R.id.splash_LBL_internetErrorMessage);
    }

    private void initViews() {
        if (isNetworkConnected()) {
            showView(splash_IMG_logo);
        } else {
            splash_LBL_internetErrorMessage.setVisibility(View.VISIBLE);
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    public void showView(final View view) {
        view.setScaleX(0.0f);
        view.setScaleY(0.0f);
        view.setAlpha(0.0f);

        view.animate()
                .alpha(1.0f)
                .scaleY(1.0f)
                .scaleX(1.0f)
                .setDuration(Constants.ANIMATION_DURATION)
                .setInterpolator(new LinearOutSlowInInterpolator())
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {
                        view.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        readUserDate();
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) { }

                    @Override
                    public void onAnimationRepeat(Animator animator) { }
                });
    }

    private void readUserDate() {
        MyDB.readMyUserData(new CallBack_User() {
            @Override
            public void onUserReady(User user) {
                startApp(user);
            }

            @Override
            public void onUserFailure(String msg) {
                startApp(null);
            }
        });
    }

    private void startApp(User user) {
        Intent myIntent;

        if (user != null) {
            myIntent = new Intent(this, Activity_Menu.class);
            myIntent.putExtra(Constants.EXTRA_USER_DETAILS, new Gson().toJson(user));
        } else {
            myIntent = new Intent(this, Activity_Login.class);
        }

        startActivity(myIntent);
        finish();
    }

}