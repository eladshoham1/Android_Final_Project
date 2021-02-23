package com.example.final_project.utils;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.example.final_project.callbacks.CallBack_Friends;
import com.example.final_project.callbacks.CallBack_Runs;
import com.example.final_project.callbacks.CallBack_Settings;
import com.example.final_project.callbacks.CallBack_User;
import com.example.final_project.callbacks.CallBack_UserPicture;
import com.example.final_project.objects.Run;
import com.example.final_project.objects.Settings;
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
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;

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

    public static void readMyUserData(CallBack_User callBack_user) {
        readUserData(getUid(), callBack_user);
    }

    public static void readUserData(String uid, CallBack_User callBack_user) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(Constants.USERS_DB);

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
                callBack_user.onUserFailure("Fail to load user");
            }
        });
    }

    private static void readMyUserPicture(CallBack_UserPicture callBack_userPicture) {
        readUserPicture(getUid(), callBack_userPicture);
    }

    private static void readUserPicture(String uid, CallBack_UserPicture callBack_userPicture) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();
        StorageReference myRef = storageReference.child(Constants.IMAGES_DB + uid);

        myRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                callBack_userPicture.onPictureReady(uri.toString());
            }
        });
    }

    public static void readFriendsStatusData(CallBack_Friends callBack_friends) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(Constants.FRIENDS_DB);
        String uid = getUid();

        if (uid.isEmpty()) {
            callBack_friends.onFriendsFailure("User not exist");
            return;
        }

        myRef.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, String> friendsStatus = new HashMap<>();
                for (DataSnapshot statusSnapshot : dataSnapshot.getChildren()) {
                    if (!statusSnapshot.getKey().equals(getUid())) {
                        friendsStatus.put(statusSnapshot.getKey(), (String) statusSnapshot.getValue());
                    }
                }

                callBack_friends.onFriendsReady(friendsStatus);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                callBack_friends.onFriendsFailure("Fail to load your friends");
            }
        });
    }

    public static void readMyRunsData(CallBack_Runs callBack_runs) {
        readRunsData(getUid(), callBack_runs);
    }

    public static void readRunsData(String uid, CallBack_Runs callBack_runs) {
        ArrayList<Run> allRuns = new ArrayList<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(Constants.RUNS_DB);

        if (uid.isEmpty()) {
            callBack_runs.onRunsFailure("User not exist");
            return;
        }

        myRef.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                allRuns.clear();
                for (DataSnapshot runSnapshot : dataSnapshot.getChildren()) {
                    Run run = runSnapshot.getValue(Run.class);
                    allRuns.add(run);
                }

                callBack_runs.onRunsReady(allRuns);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                callBack_runs.onRunsFailure("Fail to load runs");
            }
        });
    }

    public static void readMySettings(CallBack_Settings callBack_settings) {
        readSettings(getUid(), callBack_settings);
    }

    public static void readSettings(String uid, CallBack_Settings callBack_settings) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(Constants.USERS_DB);

        if (uid.isEmpty()) {
            callBack_settings.onSettingsFailure("User not exist");
            return;
        }

        myRef.child(uid).child(Constants.SETTINGS_DB).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Settings settings = dataSnapshot.getValue(Settings.class);
                callBack_settings.onSettingsReady(settings);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                callBack_settings.onSettingsFailure("Fail to load settings");
            }
        });
    }

    public static void updateUser(User user, CallBack_User callBack_user) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(Constants.USERS_DB);
        myRef.child(user.getUid()).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                callBack_user.onUserReady(user);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callBack_user.onUserFailure("Fail to update your user");
            }
        });
    }

    public static void updateMyUserPicture(Uri imageUri, CallBack_UserPicture callBack_userPicture) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();
        StorageReference ref = storageReference.child(Constants.IMAGES_DB + getUid());

        ref.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                readMyUserPicture(new CallBack_UserPicture() {
                    @Override
                    public void onPictureReady(String urlString) {
                        callBack_userPicture.onPictureReady(urlString);
                    }
                });
            }
        });
    }

    public static void updateSettings(Settings settings) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(Constants.USERS_DB);

        myRef.child(getUid()).child(Constants.SETTINGS_DB).setValue(settings);
    }

    public static void deleteMyUserPicture() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();
        StorageReference myRef = storageReference.child(Constants.IMAGES_DB + getUid());

        myRef.delete();
    }

    public static void deleteRun(Run run) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(Constants.RUNS_DB);
        myRef.child(getUid()).child("" + run.getStartTime()).removeValue();
    }

    public static void updateRun(Run run) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(Constants.RUNS_DB);
        myRef.child(getUid()).child("" + run.getStartTime()).setValue(run);
    }
}
