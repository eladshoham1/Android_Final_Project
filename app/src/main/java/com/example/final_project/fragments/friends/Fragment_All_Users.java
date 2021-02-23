package com.example.final_project.fragments.friends;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.final_project.R;
import com.example.final_project.activities.Activity_Friend_Profile;
import com.example.final_project.adapters.Adapter_User;
import com.example.final_project.callbacks.CallBack_Friends;
import com.example.final_project.objects.User;
import com.example.final_project.utils.Constants;
import com.example.final_project.utils.MyDB;
import com.example.final_project.utils.MySignal;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Fragment_All_Users extends Fragment {
    private TextInputEditText all_users_EDT_searchFriends;
    private RecyclerView all_users_LST_searchFriends;

    private ArrayList<User> allUsers;
    private ArrayList<String> allFriendsKeys;
    private ArrayList<String> friendsRequestsKeys;
    private Adapter_User adapter_user;

    private String myID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_users, container, false);
        findViews(view);
        initViews();
        setUserID();
        readAllFriendsStatus();

        return view;
    }

    private void findViews(View view) {
        all_users_EDT_searchFriends = view.findViewById(R.id.all_users_EDT_searchUsers);
        all_users_LST_searchFriends = view.findViewById(R.id.all_users_LST_allUsers);
    }

    private void initViews() {
        all_users_EDT_searchFriends.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });
    }

    private void filter(String text) {
        String fullName;
        ArrayList<User> filteredUsers = new ArrayList<>();

        for (User user : allUsers) {
            fullName = user.getFirstName() + " " + user.getLastName();

            if (fullName.toLowerCase().contains(text.toLowerCase()))
                filteredUsers.add(user);
        }

        adapter_user.filterList(filteredUsers);
    }

    private void setUserID() {
        myID = MyDB.getUid();
    }

    private void readAllFriendsStatus() {
        MyDB.readFriendsStatusData(new CallBack_Friends() {
            @Override
            public void onFriendsReady(HashMap<String, String> friendsStatus) {
                readAllUsersData(friendsStatus);
            }

            @Override
            public void onFriendsFailure(String msg) {
                MySignal.getInstance().toast(msg);
            }
        });
    }

    private void readAllUsersData(HashMap<String, String> friendsStatus) {
        allFriendsKeys = new ArrayList<>();
        friendsRequestsKeys = new ArrayList<>();

        for (Map.Entry status : friendsStatus.entrySet()) {
            if (status.getValue().equals(Constants.CURRENT_FRIEND_DB) || status.getValue().equals(Constants.RECEIVED_REQUEST_DB)) {
                allFriendsKeys.add((String) status.getKey());
            } else if (status.getValue().equals(Constants.SENT_REQUEST_DB)) {
                friendsRequestsKeys.add((String) status.getKey());
            }
        }

        readAllUsers();
    }

    private void readAllUsers() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(Constants.USERS_DB);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                allUsers = new ArrayList<>();
                for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                    User user = userSnapshot.getValue(User.class);

                    if (!userInFriends(user.getUid())) {
                        allUsers.add(user);
                    }
                }

                showUsersView();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                MySignal.getInstance().toast("Failed to read the all users data");
            }
        });
    }

    private boolean userInFriends(String uid) {
        if (myID.equals(uid)) {
            return true;
        }

        for (String friendID : allFriendsKeys) {
            if (friendID.equals(uid)) {
                return true;
            }
        }

        return false;
    }

    private void showUsersView() {
        all_users_LST_searchFriends.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter_user = new Adapter_User(getContext(), allUsers, friendsRequestsKeys);
        adapter_user.setClickListener(new Adapter_User.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                openFriendFragment(adapter_user.getItem(position));
            }

            @Override
            public void onSendRequest(int position) {
                sendFriendRequest(adapter_user.getItem(position).getUid());
            }

            @Override
            public void onCancelRequest(int position) {
                cancelFriendRequest(adapter_user.getItem(position).getUid());
            }
        });
        all_users_LST_searchFriends.setAdapter(adapter_user);
    }

    private void openFriendFragment(User user) {
        Intent myIntent = new Intent(getContext(), Activity_Friend_Profile.class);
        myIntent.putExtra(Constants.EXTRA_USER_DETAILS, new Gson().toJson(user));
        startActivity(myIntent);
    }

    private void sendFriendRequest(String userID) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(Constants.FRIENDS_DB);

        myRef.child(myID).child(userID).setValue(Constants.SENT_REQUEST_DB);
        myRef.child(userID).child(myID).setValue(Constants.RECEIVED_REQUEST_DB);
        friendsRequestsKeys.add(userID);
        adapter_user.notifyDataSetChanged();
    }

    private void cancelFriendRequest(String userID) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(Constants.FRIENDS_DB);

        myRef.child(myID).child(userID).removeValue();
        myRef.child(userID).child(myID).removeValue();
        allFriendsKeys.remove(userID);
        adapter_user.notifyDataSetChanged();
    }
}