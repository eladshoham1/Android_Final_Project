package com.example.final_project.activities;

import android.os.Bundle;

import com.example.final_project.R;
import com.example.final_project.fragments.Fragment_Edit_Profile;

public class Activity_Create_User extends Activity_Base {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        findViews();
    }

    private void findViews() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.create_user_FRG_editUser, new Fragment_Edit_Profile())
                .commit();
    }
}