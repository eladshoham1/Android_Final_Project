package com.example.final_project.callbacks;

import java.util.ArrayList;

public interface Callback_RunsData {
    void onRunsReady(ArrayList<String> runs);
    void onRunsFailure(String msg);
}
