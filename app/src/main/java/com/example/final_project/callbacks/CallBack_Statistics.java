package com.example.final_project.callbacks;

import com.example.final_project.objects.Statistics;

public interface CallBack_Statistics {
    void onStatisticsReady(Statistics statistics);
    void onStatisticsFailure(String msg);
}
