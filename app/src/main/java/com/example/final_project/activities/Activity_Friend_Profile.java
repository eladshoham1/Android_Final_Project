package com.example.final_project.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.final_project.R;
import com.example.final_project.fragments.user.Fragment_Profile;
import com.example.final_project.utils.Constants;

public class Activity_Friend_Profile extends AppCompatActivity {
    private Toolbar friend_profile_TLB_toolbar;
    private Fragment_Profile fragment_profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_profile);

        findViews();
        initViews();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
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
        bundle.putString(Constants.EXTRA_USER_DETAILS, userString);

        fragment_profile = new Fragment_Profile();
        fragment_profile.setArguments(bundle);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.friend_profile_LAY_profile, fragment_profile)
                .commit();
    }

}