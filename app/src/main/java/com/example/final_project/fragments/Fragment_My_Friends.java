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
import com.example.final_project.utils.MySignal;
import com.example.final_project.utils.database.MyDB;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Fragment_My_Friends extends Fragment {
    private RecyclerView friends_LST_allFriends;
    private User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_friends, container, false);
        findViews(view);
        initViews();

        return view;
    }

    private void findViews(View view) {
        friends_LST_allFriends = view.findViewById(R.id.friends_LST_allFriends);
    }

    private void initViews() {
        user = MyDB.getInstance().getUserData();
        getAllFriendsKeys();
    }

    private void getAllFriendsKeys() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRefFriends = database.getReference("friends");
        DatabaseReference myRefUsers = database.getReference("users");

        myRefFriends.child("current friends").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<User> allFriends = new ArrayList<>();
                for (DataSnapshot friendSnapshot: dataSnapshot.getChildren()) {
                    String friendKey = friendSnapshot.getValue(String.class);

                    myRefUsers.child(friendKey).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            User friend = (User)dataSnapshot.getValue(User.class);
                            allFriends.add(friend);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            MySignal.getInstance().toast("Failed to read the friends data");
                        }
                    });
                }

                showAllFriends(allFriends);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                MySignal.getInstance().toast("Failed to read the friends data");
            }
        });
    }

    private void showAllFriends(ArrayList<User> allFriends) {
        friends_LST_allFriends.setLayoutManager(new LinearLayoutManager(getContext()));
        Adapter_Friend adapter_friend = new Adapter_Friend(getContext(), allFriends, Adapter_Friend.FRIENDS_STATE.MY_FRIENDS);
        adapter_friend.setClickListener(new Adapter_Friend.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                openFriendFragment(allFriends.get(position));
            }

            @Override
            public void onCompeteClick(int position) {
                friendsCompetition(allFriends.get(position));
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

            }
        });
        friends_LST_allFriends.setAdapter(adapter_friend);
    }

    private void openFriendFragment(User theUser) {
        //TODO
    }

    private void friendsCompetition(User theUser) {
        MySignal.getInstance().toast("You win"); //TODO
    }

}