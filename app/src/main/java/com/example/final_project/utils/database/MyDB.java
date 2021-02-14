package com.example.final_project.utils.database;

import com.example.final_project.callbacks.Callback_FriendsData;
import com.example.final_project.callbacks.Callback_RunsData;
import com.example.final_project.callbacks.Callback_UserData;
import com.example.final_project.objects.User;

import com.example.final_project.utils.MySP;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;

public class MyDB {
    private static MyDB instance;
    private String uid;

    public static MyDB getInstance() {
        return instance;
    }

    private MyDB() {
        this.uid = null;
    }

    public static void init() {
        if (instance == null) {
            instance = new MyDB();
        }
    }

    public boolean setUid() {
        if (this.uid == null) {
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

            if (firebaseUser != null) {
                this.uid = firebaseUser.getUid();
                return true;
            }
        }

        return false;
    }

    public User getUserData() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        User user;

        String userData = MySP.getInstance().getString(MySP.KEYS.USER_DATA, "");
        if (!userData.isEmpty()) {
            user = new Gson().fromJson(userData, User.class);
        } else {
            user = new User();
        }

        user.setUid(firebaseUser.getUid());
        user.setPhone(firebaseUser.getPhoneNumber());

        return user;
    }

    public void readAllRuns(Callback_RunsData callback_runsData) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("runs");

        myRef.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> runs = new ArrayList<String>();
                for (DataSnapshot runSnapshot : dataSnapshot.getChildren()) {
                    runs.add(runSnapshot.getValue(String.class));
                }

                callback_runsData.onRunsReady(runs);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                callback_runsData.onRunsFailure("Failed to read the runs data");
            }
        });
    }

    public void readAllFriends(Callback_FriendsData callback_friendsData) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("friends");

        myRef.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> friends = new ArrayList<String>();
                for (DataSnapshot friendSnapshot : dataSnapshot.getChildren()) {
                    friends.add(friendSnapshot.getValue(String.class));
                }

                callback_friendsData.onFriendsReady(friends);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                callback_friendsData.onFriendsFailure("Failed to read the friends data");
            }
        });
    }

    public void updateUser(User user) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users");
        myRef.child(uid).setValue(user);
    }

    public void updateRuns(ArrayList<String> runsKeys) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("runs");
        myRef.child(uid).setValue(runsKeys);
    }

    public void updateFriends(ArrayList<String> friendsKeys) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("friends");
        myRef.child(uid).setValue(friendsKeys);
    }
}
