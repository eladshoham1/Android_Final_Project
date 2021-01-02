package com.example.final_project.objects;

import java.util.ArrayList;

public class History {
    private ArrayList<Run> allRuns = null;

    public History() {}

    public History(ArrayList<Run> allRuns) {
        this.allRuns = allRuns;
    }

    public ArrayList<Run> getAllRuns() {
        return allRuns;
    }

    public void setAllRuns(ArrayList<Run> allRuns) {
        this.allRuns = allRuns;
    }

    public boolean addRun(Run run) {
        return allRuns.add(run);
    }

    public boolean deleteRun(Run run) {
        return allRuns.remove(run);
    }
}
