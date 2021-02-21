package com.example.final_project.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.final_project.R;
import com.example.final_project.callbacks.CallBack_Settings;
import com.example.final_project.objects.Settings;
import com.example.final_project.utils.MyDB;
import com.example.final_project.utils.MySP;
import com.example.final_project.utils.MySignal;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class Activity_Settings extends AppCompatActivity {
    private SwitchMaterial settings_SWT_sound;
    private SwitchMaterial settings_SWT_picture;
    private SwitchMaterial settings_SWT_age;
    private SwitchMaterial settings_SWT_bmi;
    private SwitchMaterial settings_SWT_height;
    private SwitchMaterial settings_SWT_weight;
    private MaterialButton settings_BTN_saveChanges;
    private Toolbar settings_TLB_toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        findViews();
        initViews();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        openMenuActivity();
    }

    private void findViews() {
        settings_SWT_sound = findViewById(R.id.settings_SWT_sound);
        settings_SWT_picture = findViewById(R.id.settings_SWT_picture);
        settings_SWT_age = findViewById(R.id.settings_SWT_age);
        settings_SWT_bmi = findViewById(R.id.settings_SWT_bmi);
        settings_SWT_height = findViewById(R.id.settings_SWT_height);
        settings_SWT_weight = findViewById(R.id.settings_SWT_weight);
        settings_BTN_saveChanges = findViewById(R.id.settings_BTN_saveChanges);
        settings_TLB_toolbar = findViewById(R.id.settings_TLB_toolbar);
    }

    private void initViews() {
        setSupportActionBar(settings_TLB_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        readSettingsFromDB();

        settings_BTN_saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSettings();
            }
        });
    }

    private void readSettingsFromDB() {
        MyDB.readMySettings(new CallBack_Settings() {
            @Override
            public void onSettingsReady(Settings settings) {
                if (settings == null) {
                    settings = new Settings();
                }

                updateView(settings);
            }

            @Override
            public void onSettingsFailure(String msg) {
                MySignal.getInstance().toast(msg);
            }
        });
    }

    private void updateView(Settings settings) {
        settings_SWT_sound.setChecked(settings.isSound());
        settings_SWT_picture.setChecked(settings.isPicture());
        settings_SWT_age.setChecked(settings.isAge());
        settings_SWT_bmi.setChecked(settings.isBmi());
        settings_SWT_height.setChecked(settings.isHeight());
        settings_SWT_weight.setChecked(settings.isWeight());
    }

    private void saveSettings() {
        Settings settings = new Settings();
        settings.setSound(settings_SWT_sound.isChecked());
        settings.setPicture(settings_SWT_picture.isChecked());
        settings.setAge(settings_SWT_age.isChecked());
        settings.setBmi(settings_SWT_bmi.isChecked());
        settings.setHeight(settings_SWT_height.isChecked());
        settings.setWeight(settings_SWT_weight.isChecked());

        MySP.getInstance().putBoolean(MySP.KEYS.SOUND_ENABLE, settings_SWT_sound.isChecked());
        MyDB.updateSettings(settings);
        openMenuActivity();
    }

    private void openMenuActivity() {
        Intent myIntent = new Intent(this, Activity_Menu.class);
        startActivity(myIntent);
        finish();
    }
}