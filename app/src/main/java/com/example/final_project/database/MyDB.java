package com.example.final_project.database;

import com.example.final_project.callbacks.Callback_UserData;
import com.example.final_project.objects.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import javax.security.auth.callback.Callback;

public class MyDB {
    public static boolean validateUser() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        return firebaseUser != null;
    }

    public static void readUserData(Callback_UserData callback_userData) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users");

        myRef.child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                callback_userData.onUserReady((User)dataSnapshot.getValue(User.class));
            }

            @Override
            public void onCancelled(DatabaseError error) {
                callback_userData.onUserFailure("Failed to read the user data");
            }
        });
    }

    public static void readAllFriends(Callback_UserData callback_userData) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("friends");

        myRef.child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                callback_userData.onUserReady((User)dataSnapshot.getValue(User.class));
            }

            @Override
            public void onCancelled(DatabaseError error) {
                callback_userData.onUserFailure("Failed to read the user data");
            }
        });
    }

    public static void updateUser(User user) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users");
        myRef.child(user.getUid()).setValue(user);
    }
}
