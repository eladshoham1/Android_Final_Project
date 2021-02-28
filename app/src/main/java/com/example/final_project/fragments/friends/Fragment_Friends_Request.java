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

import java.util.HashMap;
import java.util.Map;

public class Fragment_Friends_Request extends Fragment {
    private RecyclerView friends_request_LST_friendsRequest;

    private Adapter_Friend adapter_friend;
    private String myID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends_request, container, false);
        findViews(view);
        setUserID();
        showAllFriendsRequests();
        setAllFriendsRequests();

        return view;
    }

    private void findViews(View view) {
        friends_request_LST_friendsRequest = view.findViewById(R.id.friends_request_LST_friendsRequest);
    }

    private void setUserID() {
        myID = MyDB.getUid();
    }

    private void setAllFriendsRequests() {
        MyDB.readFriendsStatusData(new CallBack_Friends() {
            @Override
            public void onFriendsReady(HashMap<String, String> friendsStatus) {
                readAllFriendsRequestData(friendsStatus);
            }

            @Override
            public void onFriendsFailure(String msg) {
                MySignal.getInstance().toast(msg);
            }
        });
    }

    private void readAllFriendsRequestData(HashMap<String, String> friendsStatus) {
        for (Map.Entry status : friendsStatus.entrySet()) {
            if (status.getValue().equals(Constants.RECEIVED_REQUEST_DB)) {
                readFriendData((String) status.getKey());
            }
        }
    }

    private void readFriendData(String key) {
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

    private void showAllFriendsRequests() {
        friends_request_LST_friendsRequest.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter_friend = new Adapter_Friend(getContext(), Adapter_Friend.FRIENDS_STATUS.FRIENDS_REQUESTS);
        adapter_friend.setClickListener(new Adapter_Friend.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                openFriendFragment(adapter_friend.getItem(position));
            }

            @Override
            public void onCompeteClick(int position) {

            }

            @Override
            public void onRemoveClick(int position) {

            }

            @Override
            public void onAcceptRequest(int position) {
                acceptFriendRequest(adapter_friend.getItem(position));
            }

            @Override
            public void onRejectRequest(int position) {
                rejectFriendRequest(adapter_friend.getItem(position));
            }
        });
        friends_request_LST_friendsRequest.setAdapter(adapter_friend);
    }

    private void openFriendFragment(User user) {
        Intent myIntent = new Intent(getContext(), Activity_Friend_Profile.class);
        myIntent.putExtra(Constants.EXTRA_USER_DETAILS, new Gson().toJson(user));
        startActivity(myIntent);
        getActivity().finish();
    }

    private void acceptFriendRequest(User user) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(Constants.FRIENDS_DB);

        myRef.child(myID).child(user.getUid()).setValue(Constants.CURRENT_FRIEND_DB);
        myRef.child(user.getUid()).child(myID).setValue(Constants.CURRENT_FRIEND_DB);
        adapter_friend.removeItem(user);
    }

    private void rejectFriendRequest(User user) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(Constants.FRIENDS_DB);

        myRef.child(myID).child(user.getUid()).removeValue();
        myRef.child(user.getUid()).child(myID).removeValue();
        adapter_friend.removeItem(user);
    }

}