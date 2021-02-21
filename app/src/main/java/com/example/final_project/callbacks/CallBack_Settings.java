package com.example.final_project.callbacks;

import com.example.final_project.objects.Settings;

public interface CallBack_Settings {
    void onSettingsReady(Settings settings);
    void onSettingsFailure(String msg);
}
