package com.example.final_project.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.final_project.R;
import com.example.final_project.adapters.Adapter_Friend;
import com.example.final_project.callbacks.Callback_UserData;
import com.example.final_project.database.MyDB;
import com.example.final_project.objects.User;
import com.example.final_project.utils.MySignal;

import java.util.ArrayList;

public class Fragment_Friends extends Fragment {
    private RecyclerView friends_LST_allUsers;
    private User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends, container, false);
        findViews(view);

        MyDB.readUserData(new Callback_UserData() {
            @Override
            public void onUserReady(User theUser) {
                user = theUser;
                initViews();
            }

            @Override
            public void onUserFailure(String msg) {
                MySignal.getInstance().toast(msg);
            }
        });

        return view;
    }

    private void findViews(View view) {
        friends_LST_allUsers = view.findViewById(R.id.friends_LST_allUsers);
    }

    private void initViews() {
        MyDB.readAllFriends(new Callback_UserData() {
            @Override
            public void onUserReady(User theUser) {
                Log.d("check2", "yes");
            }

            @Override
            public void onUserFailure(String msg) {
                Log.d("check2", "no");
            }
        });

        ArrayList<User> friends = null;//user.getAllFriends();
        if (friends == null)
            return;;

        friends_LST_allUsers.setLayoutManager(new LinearLayoutManager(getContext()));
        Adapter_Friend adapter_post = new Adapter_Friend(getContext(), friends);
        adapter_post.setClickListener(new Adapter_Friend.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                MySignal.getInstance().toast("Position " + position);
            }

            /*@Override
            public void onReportClick(int position) { onRequestFriends
                Toast.makeText(MainActivity.this, "Report " + position, Toast.LENGTH_SHORT).show();

            }*/
        });
        friends_LST_allUsers.setAdapter(adapter_post);
    }
}