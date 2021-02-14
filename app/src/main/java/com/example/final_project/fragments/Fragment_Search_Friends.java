package com.example.final_project.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
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

public class Fragment_Search_Friends extends Fragment {
    private RecyclerView friends_LST_searchFriends;
    private User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_friends, container, false);
        findViews(view);
        initViews();

        return view;
    }

    private void findViews(View view) {
        friends_LST_searchFriends = view.findViewById(R.id.friends_LST_searchFriends);
    }

    private void initViews() {
        user = MyDB.getInstance().getUserData();
        getAllUsersList();
    }

    private void getAllUsersList() { //TODO remove exist friends and requests
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(Constants.USERS_DB);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<User> allUsers = new ArrayList<>();
                for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                    User userFromDB = userSnapshot.getValue(User.class);

                    if (!user.getUid().equals(userFromDB.getUid()))
                        allUsers.add(userFromDB);
                }

                showAllUsers(allUsers);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                MySignal.getInstance().toast("Failed to read the all users data");
            }
        });
    }

    private void showAllUsers(ArrayList<User> allUsers) {
        friends_LST_searchFriends.setLayoutManager(new LinearLayoutManager(getContext()));
        Adapter_Friend adapter_friend = new Adapter_Friend(getContext(), allUsers, Adapter_Friend.FRIENDS_STATE.SEARCH_FRIENDS);
        adapter_friend.setClickListener(new Adapter_Friend.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                openFriendFragment(allUsers.get(position));
            }

            @Override
            public void onCompeteClick(int position) {

            }

            @Override
            public void onCancelRequest(int position) {

            }

            @Override
            public void onAcceptRequest(int position) {

            }

            @Override
            public void onRejectRequest(int position) {

            }

            @Override
            public void onSendRequest(int position) {
                sendFriendRequest(allUsers.get(position));
            }
        });
        friends_LST_searchFriends.setAdapter(adapter_friend);
    }

    private void openFriendFragment(User theUser) {
        //TODO
    }

    private void sendFriendRequest(User theUser) {
        long currentDate = System.currentTimeMillis();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(Constants.FRIENDS_DB);

        myRef.child(Constants.SEND_REQUESTS_DB).child(user.getUid()).child(theUser.getUid()).setValue(currentDate);
        myRef.child(Constants.GET_REQUESTS_DB).child(theUser.getUid()).child(user.getUid()).setValue(currentDate);
    }
}