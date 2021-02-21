package com.example.final_project.fragments.user;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.final_project.R;
import com.example.final_project.callbacks.CallBack_Settings;
import com.example.final_project.callbacks.CallBack_UserPicture;
import com.example.final_project.objects.Settings;
import com.example.final_project.objects.User;
import com.example.final_project.utils.Constants;
import com.example.final_project.utils.MyDB;
import com.example.final_project.utils.MySignal;
import com.example.final_project.utils.MyStrings;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.gson.Gson;

public class Fragment_Profile extends Fragment {
    private ShapeableImageView profile_IMG_picture;
    private TextView profile_LBL_userName;
    private TextView profile_LBL_age;
    private TextView profile_LBL_bmi;
    private TextView profile_LBL_height;
    private TextView profile_LBL_weight;
    private RelativeLayout profile_LAY_age;
    private RelativeLayout profile_LAY_bmi;
    private RelativeLayout profile_LAY_height;
    private RelativeLayout profile_LAY_weight;

    private User profile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        findViews(view);
        initUser();
        initViews();

        return view;
    }

    private void findViews(View view) {
        profile_IMG_picture = view.findViewById(R.id.profile_IMG_picture);
        profile_LBL_userName = view.findViewById(R.id.profile_LBL_userName);
        profile_LBL_age = view.findViewById(R.id.profile_LBL_age);
        profile_LBL_bmi = view.findViewById(R.id.profile_LBL_bmi);
        profile_LBL_height = view.findViewById(R.id.profile_LBL_height);
        profile_LBL_weight = view.findViewById(R.id.profile_LBL_weight);
        profile_LAY_age = view.findViewById(R.id.profile_LAY_age);
        profile_LAY_bmi = view.findViewById(R.id.profile_LAY_bmi);
        profile_LAY_height = view.findViewById(R.id.profile_LAY_height);
        profile_LAY_weight = view.findViewById(R.id.profile_LAY_weight);

    }

    private void initUser() {
        String profileString = getArguments().getString(Constants.EXTRA_USER_DETAILS);
        profile = new Gson().fromJson(profileString, User.class);
    }

    private void initViews() {
        if (MyDB.getUid().equals(profile.getUid())) {
            setView(new Settings());
        } else {
            MyDB.readSettings(profile.getUid(), new CallBack_Settings() {
                @Override
                public void onSettingsReady(Settings settings) {
                    if (settings == null) {
                        settings = new Settings();
                    }

                    setView(settings);
                }

                @Override
                public void onSettingsFailure(String msg) {
                    MySignal.getInstance().toast(msg);
                }
            });
        }
    }

    private void setView(Settings settings) {
        profile_LBL_userName.setText(profile.getFirstName() + " " + profile.getLastName());

        if (settings.isPicture()) {
            updateImage();
        } else {
            profile_IMG_picture.setImageResource(R.drawable.ic_profile);
        }

        if (settings.isAge()) {
            profile_LBL_age.setText("" + profile.getAge());
            profile_LAY_age.setVisibility(View.VISIBLE);
        } else {
            profile_LAY_age.setVisibility(View.GONE);
        }

        if (settings.isBmi()) {
            setBMI();
            profile_LAY_bmi.setVisibility(View.VISIBLE);
        } else {
            profile_LAY_bmi.setVisibility(View.GONE);
        }

        if (settings.isHeight()) {
            profile_LBL_height.setText(MyStrings.twoDigitsAfterPoint(profile.getHeight()));
            profile_LAY_height.setVisibility(View.VISIBLE);
        } else {
            profile_LAY_height.setVisibility(View.GONE);
        }

        if (settings.isWeight()) {
            profile_LBL_weight.setText(MyStrings.twoDigitsAfterPoint(profile.getWeight()));
            profile_LAY_weight.setVisibility(View.VISIBLE);
        } else {
            profile_LAY_weight.setVisibility(View.GONE);
        }
    }

    private void setBMI() {
        double bmi = profile.getBMI();
        String status;
        int color;

        if (bmi < 18.5) {
            status = Constants.UNDER_WEIGHT;
            color = Color.RED;
        } else if (bmi < 25) {
            status = Constants.NORMAL_WEIGHT;
            color = Color.GREEN;
        } else if (bmi < 30) {
            status = Constants.OVER_WEIGHT;
            color = Color.rgb(255,170, 0);
        } else {
            status = Constants.OBESITY;
            color = Color.RED;
        }

        profile_LBL_bmi.setText(MyStrings.twoDigitsAfterPoint(bmi));
        profile_LBL_bmi.setTextColor(color);
        profile_LBL_bmi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MySignal.getInstance().toast(status);
            }
        });
    }

    private void updateImage() {
        if (profile == null) {
            return;
        }

        MyDB.readUserPicture(profile.getUid(), new CallBack_UserPicture() {
            @Override
            public void onPictureReady(String urlString) {
                MySignal.getInstance().loadPicture(urlString, profile_IMG_picture);
            }
        });
    }

}