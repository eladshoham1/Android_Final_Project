package com.example.final_project.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.final_project.R;
import com.example.final_project.fragments.achievements.Fragment_Achievements;
import com.example.final_project.fragments.friends.Fragment_Friends;
import com.example.final_project.fragments.user.Fragment_Edit_Profile;
import com.example.final_project.fragments.user.Fragment_Profile;
import com.example.final_project.fragments.running.Fragment_Running;
import com.example.final_project.fragments.settings.Fragment_Settings;
import com.example.final_project.objects.User;
import com.example.final_project.utils.MySP;
import com.example.final_project.utils.MySignal;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

public class Activity_Menu extends AppCompatActivity {
    private DrawerLayout menu_LAY_drawerLayout;
    private NavigationView menu_NAV_navigationDrawer;
    private BottomNavigationView main_NVG_bottomNavigation;
    private Fragment selectedFragment = null;
    private Toolbar main_TLB_toolbar;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        initUser();
        findViews();
        initViews();
        navigationDrawer();
    }

    @Override
    public void onBackPressed() {
        if (menu_LAY_drawerLayout.isDrawerOpen(GravityCompat.START)) {
            menu_LAY_drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void findViews() {
        menu_LAY_drawerLayout = findViewById(R.id.menu_LAY_drawerLayout);
        menu_NAV_navigationDrawer = findViewById(R.id.menu_NAV_navigationDrawer);
        main_TLB_toolbar = findViewById(R.id.main_TLB_toolbar);
        main_NVG_bottomNavigation = findViewById(R.id.main_NVG_bottomNavigation);
    }

    private void initUser() {
        String userString = MySP.getInstance().getString(MySP.KEYS.USER_DATA, "");

        if (!userString.isEmpty()) {
            user = new Gson().fromJson(userString, User.class);
        } else {
            completeUserData();
        }
    }

    private void completeUserData() {
        Intent myIntent = new Intent(this, Activity_Create_User.class);
        startActivity(myIntent);
        finish();
    }

    private void navigationDrawer() {
        menu_NAV_navigationDrawer.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                menu_LAY_drawerLayout,
                main_TLB_toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

        menu_LAY_drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        menu_NAV_navigationDrawer.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                menu_LAY_drawerLayout.closeDrawer(menu_NAV_navigationDrawer);
                replaceFragment(item.getItemId());
                return true;
            }
        });
    }

    private void initViews() {
        setSupportActionBar(main_TLB_toolbar);
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
            case R.id.menu_LBL_running:
                itemId = R.id.menu_LBL_running;
                selectedFragment = new Fragment_Running();
                break;
            case R.id.menu_LBL_achievements:
                itemId = R.id.menu_LBL_achievements;
                selectedFragment = new Fragment_Achievements();
                break;
            case R.id.navigation_editProfile:
                itemId = R.id.navigation_editProfile;
                selectedFragment = new Fragment_Edit_Profile();
                break;
            case R.id.navigation_settings:
                itemId = R.id.navigation_settings;
                selectedFragment = new Fragment_Settings();
                break;
            case R.id.navigation_logout:
                logOut();
                return;
        }

        updateTile(itemId);
        initFragments(selectedFragment);
    }

    private void logOut() {
        FirebaseAuth.getInstance().signOut();
        MySP.getInstance().removeKey(MySP.KEYS.USER_DATA);
        MySP.getInstance().removeKey(MySP.KEYS.STATISTICS_DATA);

        Intent myIntent = new Intent(this, Activity_Login.class);
        startActivity(myIntent);
        finish();
    }

    private void updateTile(int id) {
        switch (id) {
            case R.id.menu_LBL_profile:
                getSupportActionBar().setTitle(R.string.profile);
                break;
            case R.id.menu_LBL_friends:
                getSupportActionBar().setTitle(R.string.friends);
                break;
            case R.id.menu_LBL_running:
                getSupportActionBar().setTitle(R.string.running);
                break;
            case R.id.menu_LBL_achievements:
                getSupportActionBar().setTitle(R.string.achievements);
                break;
            case R.id.navigation_editProfile:
                getSupportActionBar().setTitle(R.string.edit_profile);
                break;
            case R.id.navigation_settings:
                getSupportActionBar().setTitle(R.string.settings);
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