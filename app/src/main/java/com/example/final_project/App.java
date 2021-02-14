package com.example.final_project;

import android.app.Application;

import com.example.final_project.utils.MySignal;
import com.example.final_project.utils.MySP;
import com.example.final_project.utils.database.MyDB;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        MyDB.init();
        MySignal.init(this);
        MySP.init(this);
    }
}