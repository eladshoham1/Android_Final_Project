package com.example.final_project.fragments.friends;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.final_project.R;
import com.example.final_project.activities.Activity_Compete_Friend;
import com.example.final_project.activities.Activity_Friend_Profile;
import com.example.final_project.adapters.Adapter_Friend;
import com.example.final_project.callbacks.CallBack_Friends;
import com.example.final_project.callbacks.CallBack_User;
import com.example.final_project.objects.User;
import com.example.final_project.utils.Constants;
import com.example.final_project.utils.MyDB;
import com.example.final_project.utils.MySignal;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Fragment_My_Friends extends Fragment {
    private RecyclerView my_friends_LST_myFriends;

    private Adapter_Friend adapter_friend;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_friends, container, false);
        findViews(view);
        showAllFriends();
        setAllFriends();

        return view;
    }

    private void findViews(View view) {
        my_friends_LST_myFriends = view.findViewById(R.id.my_friends_LST_myFriends);
    }

    private void setAllFriends() {
        MyDB.readFriendsStatusData(new CallBack_Friends() {
            @Override
            public void onFriendsReady(HashMap<String, String> friendsStatus) {
                setMyFriendsKeys(friendsStatus);
            }

            @Override
            public void onFriendsFailure(String msg) {
                MySignal.getInstance().toast(msg);
            }
        });
    }

    private void setMyFriendsKeys(HashMap<String, String> friendsStatus) {
        ArrayList<String> friendsKeys = new ArrayList<>();

        for (Map.Entry status : friendsStatus.entrySet()) {
            if (status.getValue().equals(Constants.CURRENT_FRIEND_DB)) {
                friendsKeys.add((String) status.getKey());
            }
        }

        readFriendsData(friendsKeys);
    }

    private void readFriendsData(ArrayList<String> friendsStatus) {
        for (String key : friendsStatus) {
            MyDB.readUserData(key, new CallBack_User() {
                @Override
                public void onUserReady(User user) {
                    adapter_friend.addItem(user);
                }

                @Override
                public void onUserFailure(String msg) {
                    MySignal.getInstance().toast(msg);
                }
            });
        }
    }

    private void showAllFriends() {
        my_friends_LST_myFriends.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter_friend = new Adapter_Friend(getContext(), Adapter_Friend.FRIENDS_STATUS.MY_FRIENDS);
        adapter_friend.setClickListener(new Adapter_Friend.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                openFriendFragment(adapter_friend.getItem(position));
            }

            @Override
            public void onCompeteClick(int position) {
                friendsCompetition(adapter_friend.getItem(position).getUid());
            }

            @Override
            public void onRemoveClick(int position) {
                removeFriend(adapter_friend.getItem(position));
            }

            @Override
            public void onAcceptRequest(int position) {

            }

            @Override
            public void onRejectRequest(int position) {

            }
        });
        my_friends_LST_myFriends.setAdapter(adapter_friend);
    }

    private void openFriendFragment(User user) {
        Intent myIntent = new Intent(getContext(), Activity_Friend_Profile.class);
        myIntent.putExtra(Constants.EXTRA_USER_DETAILS, new Gson().toJson(user));
        startActivity(myIntent);
    }

    private void friendsCompetition(String userID) {
        Intent myIntent = new Intent(getContext(), Activity_Compete_Friend.class);
        myIntent.putExtra(Constants.EXTRA_FRIEND_KEY, userID);
        startActivity(myIntent);
    }

    private void removeFriend(User user) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(Constants.FRIENDS_DB);

        myRef.child(MyDB.getUid()).child(user.getUid()).removeValue();
        myRef.child(user.getUid()).child(MyDB.getUid()).removeValue();

        adapter_friend.removeItem(user);
    }

}