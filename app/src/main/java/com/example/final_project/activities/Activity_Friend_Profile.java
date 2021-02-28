package com.example.final_project.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.final_project.R;
import com.example.final_project.fragments.user.Fragment_Profile;
import com.example.final_project.utils.Constants;

public class Activity_Friend_Profile extends Activity_Base {
    private Toolbar friend_profile_TLB_toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_profile);

        findViews();
        initViews();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        moveToFriends();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        moveToFriends();
    }

    private void findViews() {
        friend_profile_TLB_toolbar = findViewById(R.id.friend_profile_TLB_toolbar);
    }

    private void initViews() {
        setSupportActionBar(friend_profile_TLB_toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String userString = getIntent().getStringExtra(Constants.EXTRA_USER_DETAILS);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.BUNDLE_USER_DETAILS, userString);

        Fragment_Profile fragment_profile = new Fragment_Profile();
        fragment_profile.setArguments(bundle);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.friend_profile_LAY_profile, fragment_profile)
                .commit();
    }

    private void moveToFriends() {
        Intent myIntent = new Intent(this, Activity_Menu.class);
        myIntent.putExtra(Constants.EXTRA_GO_TO_ACTIVITY, Constants.FRIENDS_CODE);
        startActivity(myIntent);
        finish();
    }

}