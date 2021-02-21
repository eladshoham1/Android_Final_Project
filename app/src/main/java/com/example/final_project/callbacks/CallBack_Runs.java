package com.example.final_project.callbacks;

import com.example.final_project.objects.Run;

import java.util.ArrayList;

public interface CallBack_Runs {
    void onRunsReady(ArrayList<Run> allRuns);
    void onRunsFailure(String msg);
}
