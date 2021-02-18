package com.example.final_project.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.final_project.R;
import com.example.final_project.fragments.running.Fragment_Running_Details;
import com.example.final_project.fragments.user.Fragment_Edit_Profile;
import com.example.final_project.utils.Constants;
import com.google.gson.Gson;

public class Activity_Create_User extends AppCompatActivity {
    private Fragment_Edit_Profile fragment_edit_profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        initViews();
    }

    private void initViews() {
        fragment_edit_profile = new Fragment_Edit_Profile();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.createUser_LAY_editProfile, fragment_edit_profile)
                .commit();
    }
}