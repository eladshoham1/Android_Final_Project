package com.example.final_project.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.final_project.R;
import com.example.final_project.fragments.Fragment_Edit_Profile;
import com.example.final_project.fragments.Fragment_Friends;
import com.example.final_project.fragments.Fragment_Profile;
import com.example.final_project.fragments.Fragment_Progress;
import com.example.final_project.fragments.Fragment_Running;
import com.example.final_project.fragments.Fragment_Settings;
import com.example.final_project.objects.User;
import com.example.final_project.utils.Constants;
import com.example.final_project.utils.database.MyDB;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

public class Activity_Menu extends Activity_Base {
    private BottomNavigationView main_NVG_bottomNavigation;
    private Fragment selectedFragment = null;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        findViews();
        initViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            updateTile(R.id.action_settings);
            openSettingsPage();
            return true;
        } else if (id == R.id.action_editProfile) {
            updateTile(R.id.action_editProfile);
            openEditProfilePage();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void openSettingsPage() {
        initFragments(new Fragment_Settings());
    }

    private void openEditProfilePage() {
        initFragments(new Fragment_Edit_Profile());
    }

    private void findViews() {
        toolbar = findViewById(R.id.toolbar);
        main_NVG_bottomNavigation = findViewById(R.id.main_NVG_bottomNavigation);
    }

    private void initViews() {
        setSupportActionBar(toolbar);
        updateTile(R.id.menu_LBL_profile);
        initFragments(new Fragment_Profile());

        main_NVG_bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                replaceFragment(item.getItemId());
                return true;
            }
        });
    }

    private void replaceFragment(int id) {
        int itemId = 0;

        switch (id) {
            case R.id.menu_LBL_profile:
                itemId = R.id.menu_LBL_profile;
                selectedFragment = new Fragment_Profile();
                break;
            case R.id.menu_LBL_friends:
                itemId = R.id.menu_LBL_friends;
                selectedFragment = new Fragment_Friends();
                break;
            case R.id.menu_LBL_activity:
                itemId = R.id.menu_LBL_activity;
                selectedFragment = new Fragment_Running();
                break;
            case R.id.menu_LBL_progress:
                itemId = R.id.menu_LBL_progress;
                selectedFragment = new Fragment_Progress();
                break;
        }

        updateTile(itemId);
        initFragments(selectedFragment);
    }

    private void updateTile(int id) {
        switch (id) {
            case R.id.menu_LBL_profile:
                getSupportActionBar().setTitle(R.string.profile);
                break;
            case R.id.menu_LBL_friends:
                getSupportActionBar().setTitle(R.string.friends);
                break;
            case R.id.menu_LBL_activity:
                getSupportActionBar().setTitle(R.string.running);
                break;
            case R.id.menu_LBL_progress:
                getSupportActionBar().setTitle(R.string.progress);
                break;
            case R.id.action_settings:
                getSupportActionBar().setTitle(R.string.settings);
                break;
            case R.id.action_editProfile:
                getSupportActionBar().setTitle(R.string.edit_profile);
                break;
        }
    }

    private void initFragments(Fragment selectedFragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_FRG_selectedFragment, selectedFragment)
                .commit();
    }
}