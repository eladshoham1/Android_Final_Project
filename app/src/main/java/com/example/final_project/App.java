package com.example.final_project;

import android.app.Application;

import com.example.final_project.utils.MySignal;
import com.example.final_project.utils.MySP;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        MySignal.init(this);
        MySP.init(this);
    }
}