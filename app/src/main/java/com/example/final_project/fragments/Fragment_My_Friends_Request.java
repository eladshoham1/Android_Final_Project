package com.example.final_project.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.final_project.R;
import com.example.final_project.adapters.Adapter_Friend;
import com.example.final_project.objects.User;
import com.example.final_project.utils.Constants;
import com.example.final_project.utils.MySignal;
import com.example.final_project.utils.database.MyDB;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Fragment_My_Friends_Request extends Fragment {
    private RecyclerView friends_LST_myFriendsRequest;
    private User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_friends_request, container, false);
        findViews(view);
        initViews();

        return view;
    }

    private void findViews(View view) {
        friends_LST_myFriendsRequest = view.findViewById(R.id.friends_LST_myFriendsRequest);
    }

    private void initViews() {
        user = MyDB.getInstance().getUserData();
        getAllFriendsKeys();
    }

    private void getAllFriendsKeys() {
        ArrayList<String> allFriendsKeys = new ArrayList<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(Constants.FRIENDS_DB);

        myRef.child(Constants.SEND_REQUESTS_DB).child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot friendSnapshot: dataSnapshot.getChildren()) {
                    String friendKey = friendSnapshot.getKey();
                    allFriendsKeys.add(friendKey);
                }

                getFriendsData(allFriendsKeys);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                MySignal.getInstance().toast("Failed to read the friends request data");
            }
        });
    }

    private void getFriendsData(ArrayList<String> allFriendsKeys) {
        ArrayList<User> allFriends = new ArrayList<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(Constants.USERS_DB);

        for (int i = 0; i < allFriendsKeys.size(); i++) {
            myRef.child(allFriendsKeys.get(i)).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User friend = (User) dataSnapshot.getValue(User.class);
                    allFriends.add(friend);

                    showAllFriends(allFriends);
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    MySignal.getInstance().toast("Failed to read the friends request data");
                }
            });
        }
    }

    private void showAllFriends(ArrayList<User> allFriends) {
        friends_LST_myFriendsRequest.setLayoutManager(new LinearLayoutManager(getContext()));
        Adapter_Friend adapter_friend = new Adapter_Friend(getContext(), allFriends, Adapter_Friend.FRIENDS_STATE.MY_FRIENDS_REQUEST);
        adapter_friend.setClickListener(new Adapter_Friend.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                openFriendFragment(allFriends.get(position));
            }

            @Override
            public void onCompeteClick(int position) {

            }

            @Override
            public void onCancelRequest(int position) {
                cancelFriendRequest(allFriends.get(position));
            }

            @Override
            public void onAcceptRequest(int position) {

            }

            @Override
            public void onRejectRequest(int position) {

            }

            @Override
            public void onSendRequest(int position) {

            }
        });
        friends_LST_myFriendsRequest.setAdapter(adapter_friend);
    }

    private void openFriendFragment(User theUser) {
    }

    private void cancelFriendRequest(User theUser) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(Constants.FRIENDS_DB);

        myRef.child(Constants.SEND_REQUESTS_DB).child(user.getUid()).child(theUser.getUid()).removeValue();
        myRef.child(Constants.GET_REQUESTS_DB).child(theUser.getUid()).child(user.getUid()).removeValue();
    }
}