package com.example.final_project.fragments.achievements;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.final_project.R;
import com.example.final_project.activities.Activity_Run_Details;
import com.example.final_project.adapters.Adapter_History;
import com.example.final_project.callbacks.CallBack_Runs;
import com.example.final_project.objects.Run;
import com.example.final_project.utils.Constants;
import com.example.final_project.utils.MyDB;
import com.example.final_project.utils.MySignal;
import com.google.gson.Gson;

import java.util.ArrayList;

public class Fragment_Runs_History extends Fragment {
    private RecyclerView runs_history_LST_allRuns;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_runs_history, container, false);
        findViews(view);
        getAllRuns();

        return view;
    }

    private void findViews(View view) {
        runs_history_LST_allRuns = view.findViewById(R.id.runs_history_LST_allRuns);
    }

    private void getAllRuns() {
        MyDB.readMyRunsData(new CallBack_Runs() {
            @Override
            public void onRunsReady(ArrayList<Run> allRuns) {
                showAllRuns(allRuns);
            }

            @Override
            public void onRunsFailure(String msg) {
                MySignal.getInstance().toast(msg);
            }
        });
    }

    private void showAllRuns(ArrayList<Run> allRuns) {
        runs_history_LST_allRuns.setLayoutManager(new LinearLayoutManager(getContext()));
        Adapter_History adapter_history = new Adapter_History(getContext(), allRuns);
        runs_history_LST_allRuns.setAdapter(adapter_history);

        adapter_history.setClickListener(new Adapter_History.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                openRunDetailsActivity(allRuns.get(position));
            }
        });
    }

    private void openRunDetailsActivity(Run run) {
        Intent myIntent = new Intent(getContext(), Activity_Run_Details.class);
        myIntent.putExtra(Constants.EXTRA_RUN_DETAILS, new Gson().toJson(run));
        startActivity(myIntent);
        getActivity().finish();
    }
}