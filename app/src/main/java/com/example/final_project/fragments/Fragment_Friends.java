package com.example.final_project.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.final_project.R;
import com.google.android.material.button.MaterialButton;

public class Fragment_Friends extends Fragment {
    private MaterialButton friends_BTN_myFriends;
    private MaterialButton friends_BTN_myFriendsRequest;
    private MaterialButton friends_BTN_friendsRequest;
    private MaterialButton friends_BTN_searchFriends;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends, container, false);
        findViews(view);
        initViews();

        return view;
    }

    private void findViews(View view) {
        friends_BTN_myFriends = view.findViewById(R.id.friends_BTN_myFriends);
        friends_BTN_myFriendsRequest = view.findViewById(R.id.friends_BTN_myFriendsRequest);
        friends_BTN_friendsRequest = view.findViewById(R.id.friends_BTN_friendsRequest);
        friends_BTN_searchFriends = view.findViewById(R.id.friends_BTN_searchFriends);
    }

    private void initViews() {
        initFragments(new Fragment_My_Friends());

        friends_BTN_myFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initFragments(new Fragment_My_Friends());
            }
        });

        friends_BTN_myFriendsRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initFragments(new Fragment_My_Friends_Request());
            }
        });

        friends_BTN_friendsRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initFragments(new Fragment_Friends_Request());
            }
        });

        friends_BTN_searchFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initFragments(new Fragment_Search_Friends());
            }
        });
    }

    private void initFragments(Fragment selectedFragment) {
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.friends_FRG_selectedFragment, selectedFragment)
                .commit();
    }
}