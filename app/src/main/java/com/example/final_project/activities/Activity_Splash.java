package com.example.final_project.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.final_project.R;
import com.example.final_project.callbacks.CallBack_User;
import com.example.final_project.objects.User;
import com.example.final_project.utils.MyDB;
import com.example.final_project.utils.MySP;
import com.google.gson.Gson;

public class Activity_Splash extends AppCompatActivity {

    private final int ANIMATION_DURATION = 1000;
    private ImageView splash_IMG_logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        findViews();
        showView(splash_IMG_logo);
    }

    private void findViews() {
        splash_IMG_logo = findViewById(R.id.splash_IMG_logo);
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
            myIntent = new Intent(getBaseContext(), Activity_Menu.class);
            MySP.getInstance().putString(MySP.KEYS.USER_DATA, new Gson().toJson(user));
        } else {
            myIntent = new Intent(getBaseContext(), Activity_Login.class);
        }

        startActivity(myIntent);
        finish();
    }

    public void showView(final View view) {
        view.setScaleX(0.0f);
        view.setScaleY(0.0f);
        view.setAlpha(0.0f);

        view.animate()
                .alpha(1.0f)
                .scaleY(1.0f)
                .scaleX(1.0f)
                .setDuration(ANIMATION_DURATION)
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

}