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
import com.example.final_project.fragments.user.Fragment_Achievements;
import com.example.final_project.fragments.friends.Fragment_Friends;
import com.example.final_project.fragments.user.Fragment_Profile;
import com.example.final_project.fragments.running.Fragment_Running;
import com.example.final_project.utils.Constants;
import com.example.final_project.utils.MySP;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class Activity_Menu extends AppCompatActivity {
    private DrawerLayout menu_LAY_drawerLayout;
    private NavigationView menu_NAV_navigationDrawer;
    private BottomNavigationView menu_NVG_bottomNavigation;
    private Fragment selectedFragment = null;
    private Toolbar menu_TLB_toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        findViews();
        initViews();
        navigationDrawer();
    }

    @Override
    protected void onStop() {
        if (menu_LAY_drawerLayout.isDrawerOpen(GravityCompat.START)) {
            menu_LAY_drawerLayout.closeDrawer(GravityCompat.START);
        }

        super.onStop();
    }

    private void findViews() {
        menu_LAY_drawerLayout = findViewById(R.id.menu_LAY_drawerLayout);
        menu_NAV_navigationDrawer = findViewById(R.id.menu_NAV_navigationDrawer);
        menu_TLB_toolbar = findViewById(R.id.menu_TLB_toolbar);
        menu_NVG_bottomNavigation = findViewById(R.id.menu_NVG_bottomNavigation);
    }

    private void initViews() {
        setSupportActionBar(menu_TLB_toolbar);

        if (getIntent().getBooleanExtra(Constants.EXTRA_GO_TO_ACHIEVEMENTS, false)) {
            replaceFragment(R.id.menu_LBL_achievements);
        } else {
            replaceFragment(R.id.menu_LBL_profile);
        }

        menu_NVG_bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                replaceFragment(item.getItemId());
                return true;
            }
        });
    }

    private void navigationDrawer() {
        menu_NAV_navigationDrawer.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                menu_LAY_drawerLayout,
                menu_TLB_toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

        menu_LAY_drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        menu_NAV_navigationDrawer.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                menu_LAY_drawerLayout.closeDrawer(menu_NAV_navigationDrawer);
                switch (item.getItemId()) {
                    case R.id.navigation_editProfile:
                        openEditProfile();
                        break;
                    case R.id.navigation_settings:
                        openSettings();
                        break;
                    case R.id.navigation_logout:
                        logOut();
                        break;
                }

                return true;
            }
        });
    }

    private void openSettings() {
        Intent myIntent = new Intent(this, Activity_Settings.class);
        startActivity(myIntent);
    }

    private void openEditProfile() {
        Intent myIntent = new Intent(this, Activity_Edit_Profile.class);
        startActivity(myIntent);
    }

    private void logOut() {
        FirebaseAuth.getInstance().signOut();
        MySP.getInstance().removeKey(MySP.KEYS.MY_WEIGHT);

        Intent myIntent = new Intent(this, Activity_Login.class);
        startActivity(myIntent);
        finish();
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
        }

        if (itemId != 0) {
            updateTile(itemId);
            initFragments(selectedFragment);
        }
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
        }
    }

    private void initFragments(Fragment selectedFragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.menu_FRG_selectedFragment, selectedFragment)
                .commit();
    }
}