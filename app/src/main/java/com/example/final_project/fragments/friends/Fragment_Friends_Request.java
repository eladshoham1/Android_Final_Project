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
import com.example.final_project.adapters.Adapter_Friend_Request;
import com.example.final_project.callbacks.CallBack_User;
import com.example.final_project.objects.User;
import com.example.final_project.utils.Constants;
import com.example.final_project.utils.MyDB;
import com.example.final_project.utils.MySP;
import com.example.final_project.utils.MySignal;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Fragment_Friends_Request extends Fragment {
    private RecyclerView friends_request_LST_friendsRequest;

    private HashMap<String, String> friendsStatus;
    private ArrayList<User> friendsRequests;
    private Adapter_Friend_Request adapter_friend_request;
    private String myID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends_request, container, false);
        findViews(view);
        setUserID();
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
        String friendsStatusString = MySP.getInstance().getString(MySP.KEYS.FRIENDS_DATA, "");

        if (!friendsStatusString.isEmpty()) {
            friendsStatus = new Gson().fromJson(friendsStatusString, HashMap.class);
            readAllFriendsRequestData();
        }
    }

    private void readAllFriendsRequestData() {
        friendsRequests = new ArrayList<>();

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
                friendsRequests.add(user);
                showAllFriendsRequests();
            }

            @Override
            public void onUserFailure(String msg) {
                MySignal.getInstance().toast(msg);
            }
        });
    }

    private void showAllFriendsRequests() {
        friends_request_LST_friendsRequest.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter_friend_request = new Adapter_Friend_Request(getContext(), friendsRequests);
        adapter_friend_request.setClickListener(new Adapter_Friend_Request.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                openFriendFragment(friendsRequests.get(position));
            }

            @Override
            public void onAcceptRequest(int position) {
                acceptFriendRequest(friendsRequests.get(position).getUid());
            }

            @Override
            public void onRejectRequest(int position) {
                rejectFriendRequest(friendsRequests.get(position).getUid());
            }
        });
        friends_request_LST_friendsRequest.setAdapter(adapter_friend_request);
    }

    private void openFriendFragment(User user) {
        Intent myIntent = new Intent(getContext(), Activity_Friend_Profile.class);
        myIntent.putExtra(Constants.EXTRA_USER_DETAILS, new Gson().toJson(user));
        startActivity(myIntent);
    }

    private void acceptFriendRequest(String userID) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(Constants.FRIENDS_DB);

        myRef.child(myID).child(userID).setValue(Constants.CURRENT_FRIEND_DB);
        myRef.child(userID).child(myID).setValue(Constants.CURRENT_FRIEND_DB);
    }

    private void rejectFriendRequest(String userID) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(Constants.FRIENDS_DB);

        myRef.child(myID).child(userID).removeValue();
        myRef.child(userID).child(myID).removeValue();

        MySP.getInstance().putString(MySP.KEYS.FRIENDS_DATA, new Gson().toJson(friendsStatus));
    }

}