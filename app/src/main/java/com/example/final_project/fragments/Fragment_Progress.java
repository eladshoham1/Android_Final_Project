package com.example.final_project.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.final_project.R;
import com.example.final_project.adapters.Adapter_History;
import com.example.final_project.objects.Run;
import com.example.final_project.objects.User;
import com.example.final_project.utils.Constants;
import com.example.final_project.utils.MySignal;
import com.example.final_project.utils.database.MyDB;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Fragment_Progress extends Fragment {
    private RecyclerView progress_LST_allRuns;
    private User user;

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
        user = MyDB.getInstance().getUserData();
        getAllRuns();
    }

    private void getAllRuns() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(Constants.RUNS_DB);

        myRef.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Run> allRuns = new ArrayList<Run>();
                for (DataSnapshot runSnapshot: dataSnapshot.getChildren()) {
                    Run run = runSnapshot.getValue(Run.class);
                    allRuns.add(run);
                }

                showAllRuns(allRuns);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                MySignal.getInstance().toast("Failed to read the runs data");
            }
        });
    }

    private void showAllRuns(ArrayList<Run> allRuns) {
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