package com.example.final_project.fragments.friends;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.final_project.R;
import com.example.final_project.adapters.Adapter_ViewPager;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.tabs.TabLayout;

public class Fragment_Friends extends Fragment {
    private TabLayout friends_LAY_tabLayout;
    private ViewPager friends_VPA_selectedPage;

    private Fragment_My_Friends fragment_my_friends;
    private Fragment_Friends_Request fragment_friends_request;
    private Fragment_Search_Friends fragment_search_friends;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends, container, false);
        findViews(view);
        initViews();

        return view;
    }

    private void findViews(View view) {
        friends_LAY_tabLayout = view.findViewById(R.id.friends_LAY_tabLayout);
        friends_VPA_selectedPage = view.findViewById(R.id.friends_VPA_selectedPage);
    }

    private void initViews() {
        fragment_my_friends = new Fragment_My_Friends();
        fragment_friends_request = new Fragment_Friends_Request();
        fragment_search_friends = new Fragment_Search_Friends();
        friends_LAY_tabLayout.setupWithViewPager(friends_VPA_selectedPage);

        Adapter_ViewPager viewPagerAdapter = new Adapter_ViewPager(getChildFragmentManager(), 0);
        viewPagerAdapter.addFragment(fragment_my_friends, getResources().getString(R.string.friends));
        viewPagerAdapter.addFragment(fragment_friends_request, getResources().getString(R.string.friends_request));
        viewPagerAdapter.addFragment(fragment_search_friends, getResources().getString(R.string.search_friends));

        friends_VPA_selectedPage.setAdapter(viewPagerAdapter);

        BadgeDrawable badgeDrawable = friends_LAY_tabLayout.getTabAt(0).getOrCreateBadge(); //TODO for requests
        badgeDrawable.setVisible(true);
        badgeDrawable.setNumber(2);
    }
}