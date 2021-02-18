package com.example.final_project.fragments.user;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.final_project.R;
import com.example.final_project.callbacks.CallBack_UserPicture;
import com.example.final_project.objects.User;
import com.example.final_project.utils.Constants;
import com.example.final_project.utils.MyDB;
import com.example.final_project.utils.MySP;
import com.example.final_project.utils.MySignal;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Fragment_Profile extends Fragment {
    private ShapeableImageView profile_IMG_picture;
    private TextView profile_LBL_userName;
    private TextView profile_LBL_age;
    private TextView profile_LBL_bmi;
    private TextView profile_LBL_height;
    private TextView profile_LBL_weight;

    private User user;

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
    }

    private void initUser() {
        String userString = MySP.getInstance().getString(MySP.KEYS.USER_DATA, "");
        user = new Gson().fromJson(userString, User.class);
    }

    private void initViews() {
        updateImage();

        profile_LBL_userName.setText(user.getFirstName() + " " + user.getLastName());
        profile_LBL_age.setText("" + user.getAge());
        profile_LBL_bmi.setText(new DecimalFormat("##.##").format(user.getBmi()));
        profile_LBL_height.setText("" + user.getHeight());
        profile_LBL_weight.setText("" + user.getWeight());
    }

    private void updateImage() {
        if (user == null) {
            return;
        }

        MyDB.readUserPicture(new CallBack_UserPicture() {
            @Override
            public void onPictureReady(String urlString) {
                MySignal.getInstance().loadPicture(urlString, profile_IMG_picture);
            }
        });
    }

}