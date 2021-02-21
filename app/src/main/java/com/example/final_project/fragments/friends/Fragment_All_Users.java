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
import com.example.final_project.objects.User;
import com.example.final_project.utils.Constants;
import com.example.final_project.utils.MyDB;
import com.example.final_project.utils.MySP;
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

    private HashMap<String, String> friendsStatus;
    private ArrayList<User> allUsers;
    private ArrayList<String> allFriendsKeys;
    private ArrayList<String> myFriendsRequestsKeys;
    private Adapter_User adapter_user;

    private String myID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_users, container, false);
        findViews(view);
        initViews();
        setUserID();
        setAllFriends();

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

    private void setAllFriends() {
        String friendsStatusString = MySP.getInstance().getString(MySP.KEYS.FRIENDS_DATA, "");

        if (!friendsStatusString.isEmpty()) {
            friendsStatus = new Gson().fromJson(friendsStatusString, HashMap.class);
            readAllUsersData();
        }
    }

    private void readAllUsersData() {
        allFriendsKeys = new ArrayList<>();
        myFriendsRequestsKeys = new ArrayList<>();

        for (Map.Entry status : friendsStatus.entrySet()) {
            if (status.getValue().equals(Constants.CURRENT_FRIEND_DB) || status.getValue().equals(Constants.RECEIVED_REQUEST_DB)) {
                allFriendsKeys.add((String) status.getKey());
            } else if (status.getValue().equals(Constants.SENT_REQUEST_DB)) {
                myFriendsRequestsKeys.add((String) status.getKey());
            }
        }

        readAllUsers();
    }

    private void readAllUsers() {
        allUsers = new ArrayList<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(Constants.USERS_DB);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                    User user = userSnapshot.getValue(User.class);

                    if (!userInFriends(user.getUid())) {
                        allUsers.add(user);
                    }
                }

                updateUsersView();
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

    private void updateUsersView() {
        all_users_LST_searchFriends.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter_user = new Adapter_User(getContext(), allUsers, myFriendsRequestsKeys);
        adapter_user.setClickListener(new Adapter_User.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                openFriendFragment(allUsers.get(position));
            }

            @Override
            public void onSendRequest(int position) {
                sendFriendRequest(allUsers.get(position).getUid());
            }

            @Override
            public void onCancelRequest(int position) {
                cancelFriendRequest(allUsers.get(position).getUid());
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

        friendsStatus.put(userID, Constants.SENT_REQUEST_DB);
        MySP.getInstance().putString(MySP.KEYS.FRIENDS_DATA, new Gson().toJson(friendsStatus));
    }

    private void cancelFriendRequest(String userID) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(Constants.FRIENDS_DB);

        myRef.child(myID).child(userID).removeValue();
        myRef.child(userID).child(myID).removeValue();

        friendsStatus.remove(userID);
        MySP.getInstance().putString(MySP.KEYS.FRIENDS_DATA, new Gson().toJson(friendsStatus));
    }
}