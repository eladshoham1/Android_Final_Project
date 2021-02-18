package com.example.final_project.utils;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.final_project.callbacks.CallBack_Statistics;
import com.example.final_project.callbacks.CallBack_User;
import com.example.final_project.callbacks.CallBack_UserPicture;
import com.example.final_project.objects.Statistics;
import com.example.final_project.objects.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MyDB {

    private static FirebaseUser getUser() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        return firebaseUser;
    }

    public static String getUid() {
        FirebaseUser firebaseUser = getUser();
        String uid = "";

        if (firebaseUser != null)
            uid = firebaseUser.getUid();

        return uid;
    }

    public static String getPhoneNumber() {
        FirebaseUser firebaseUser = getUser();
        String phoneNumber = "";

        if (firebaseUser != null)
            phoneNumber = firebaseUser.getPhoneNumber();

        return phoneNumber;
    }

    public static void readUserData(CallBack_User callBack_user) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(Constants.USERS_DB);
        String uid = getUid();

        if (uid.isEmpty()) {
            callBack_user.onUserFailure("User not exist");
            return;
        }

        myRef.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                callBack_user.onUserReady(user);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                callBack_user.onUserFailure("Fail to load your user");
            }
        });
    }

    public static void readUserPicture(CallBack_UserPicture callBack_userPicture) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();
        StorageReference ref = storageReference.child(Constants.IMAGES_DB + getUid());

        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                callBack_userPicture.onPictureReady(uri.toString());
            }
        });
    }

    public static void readStatisticsData(CallBack_Statistics callBack_statistics) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(Constants.STATISTICS_DB);
        String uid = getUid();

        if (uid.isEmpty()) {
            callBack_statistics.onStatisticsFailure("User not exist");
            return;
        }

        myRef.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Statistics statistics = dataSnapshot.getValue(Statistics.class);
                callBack_statistics.onStatisticsReady(statistics);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                callBack_statistics.onStatisticsFailure("Fail to load your statistics");
            }
        });
    }

}
