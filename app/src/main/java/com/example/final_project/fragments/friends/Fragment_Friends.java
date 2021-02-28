package com.example.final_project.fragments.friends;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.final_project.R;
import com.example.final_project.adapters.Adapter_ViewPager;
import com.example.final_project.callbacks.CallBack_Friends;
import com.example.final_project.utils.Constants;
import com.example.final_project.utils.MyDB;
import com.example.final_project.utils.MySignal;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.tabs.TabLayout;

import java.util.HashMap;
import java.util.Map;

public class Fragment_Friends extends Fragment {
    private TabLayout friends_LAY_tabLayout;
    private ViewPager friends_VPA_selectedPage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends, container, false);
        findViews(view);
        initViews();
        readFriendsRequests();

        return view;
    }

    private void findViews(View view) {
        friends_LAY_tabLayout = view.findViewById(R.id.friends_LAY_tabLayout);
        friends_VPA_selectedPage = view.findViewById(R.id.friends_VPA_selectedPage);
    }

    private void initViews() {
        Fragment_My_Friends fragment_my_friends = new Fragment_My_Friends();
        Fragment_Friends_Request fragment_friends_request = new Fragment_Friends_Request();
        Fragment_All_Users fragment_search_friends = new Fragment_All_Users();
        friends_LAY_tabLayout.setupWithViewPager(friends_VPA_selectedPage);

        Adapter_ViewPager viewPagerAdapter = new Adapter_ViewPager(getChildFragmentManager(), 0);
        viewPagerAdapter.addFragment(fragment_my_friends, getResources().getString(R.string.friends));
        viewPagerAdapter.addFragment(fragment_friends_request, getResources().getString(R.string.friends_request));
        viewPagerAdapter.addFragment(fragment_search_friends, getResources().getString(R.string.search_friends));

        friends_VPA_selectedPage.setAdapter(viewPagerAdapter);
    }

    private void readFriendsRequests() {
        MyDB.readFriendsStatusData(new CallBack_Friends() {
            @Override
            public void onFriendsReady(HashMap<String, String> friendsStatus) {
                updateView(friendsStatus);
            }

            @Override
            public void onFriendsFailure(String msg) {
                MySignal.getInstance().toast(msg);
            }
        });
    }

    private void updateView(HashMap<String, String> friendsStatus) {
        BadgeDrawable badgeDrawable = friends_LAY_tabLayout.getTabAt(Constants.TAB_FRIENDS_REQUESTS).getOrCreateBadge();
        int countFriendsRequests = 0;

        for (Map.Entry status : friendsStatus.entrySet()) {
            if (status.getValue().equals(Constants.RECEIVED_REQUEST_DB)) {
                countFriendsRequests++;
            }
        }

        if (countFriendsRequests > 0) {
            badgeDrawable.setVisible(true);
            badgeDrawable.setNumber(countFriendsRequests);
        } else {
            badgeDrawable.setVisible(false);
        }
    }

}