package com.example.final_project.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.final_project.R;
import com.example.final_project.adapters.Adapter_Friend;
import com.example.final_project.adapters.Adapter_History;
import com.example.final_project.objects.Run;
import com.example.final_project.utils.MySignal;

import java.util.ArrayList;

public class Fragment_Progress extends Fragment {
    private RecyclerView progress_LST_allRuns;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_progress, container, false);
        findViews(view);
        initViews();

        return view;
    }

    private void findViews(View view) {
        progress_LST_allRuns = view.findViewById(R.id.progress_LST_allRuns);
    }

    private void initViews() {
        ArrayList<Run> allRuns = new ArrayList<Run>();
        allRuns.add(new Run("1234", 0, 0, 0, 0, 0, 0));

        progress_LST_allRuns.setLayoutManager(new LinearLayoutManager(getContext()));
        Adapter_History adapter_history = new Adapter_History(getContext(), allRuns);
        adapter_history.setClickListener(new Adapter_History.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                MySignal.getInstance().toast("" + position);
            }
        });
        progress_LST_allRuns.setAdapter(adapter_history);
    }
}