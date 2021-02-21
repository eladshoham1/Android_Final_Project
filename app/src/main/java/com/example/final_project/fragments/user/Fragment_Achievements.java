package com.example.final_project.fragments.user;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.final_project.R;
import com.example.final_project.adapters.Adapter_ViewPager;
import com.example.final_project.fragments.running.Fragment_Runs_History;
import com.google.android.material.tabs.TabLayout;

public class Fragment_Achievements extends Fragment {
    private TabLayout achievements_LAY_tabLayout;
    private ViewPager achievements_VPA_selectedPage;

    private Fragment_Runs_History fragment_runs_history;
    private Fragment_Statistics fragment_statistics;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_achievements, container, false);
        findViews(view);
        initViews();

        return view;
    }

    private void findViews(View view) {
        achievements_LAY_tabLayout = view.findViewById(R.id.achievements_LAY_tabLayout);
        achievements_VPA_selectedPage = view.findViewById(R.id.achievements_VPA_selectedPage);
    }

    private void initViews() {
        fragment_runs_history = new Fragment_Runs_History();
        fragment_statistics = new Fragment_Statistics();
        achievements_LAY_tabLayout.setupWithViewPager(achievements_VPA_selectedPage);

        Adapter_ViewPager viewPagerAdapter = new Adapter_ViewPager(getChildFragmentManager(), 0);
        viewPagerAdapter.addFragment(fragment_runs_history, getResources().getString(R.string.history));
        viewPagerAdapter.addFragment(fragment_statistics, getResources().getString(R.string.statistics));

        achievements_VPA_selectedPage.setAdapter(viewPagerAdapter);
    }
}