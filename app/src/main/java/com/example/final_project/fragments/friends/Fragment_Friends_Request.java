package com.example.final_project.fragments.friends;

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
import com.example.final_project.utils.MyDB;
import com.example.final_project.utils.MySP;
import com.example.final_project.utils.MySignal;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;

public class Fragment_Friends_Request extends Fragment {
    private RecyclerView friends_LST_friendsRequest;
    private User user;
    private ArrayList<String> friendsRequests;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends_request, container, false);
        findViews(view);
        initUser();

        return view;
    }

    private void findViews(View view) {
        friends_LST_friendsRequest = view.findViewById(R.id.friends_LST_friendsRequest);
    }

    private void initUser() {
        String userString = MySP.getInstance().getString(MySP.KEYS.USER_DATA, "");
        user = new Gson().fromJson(userString, User.class);

        friendsRequests = new ArrayList<>();
        getAllFriendsKeys();
    }

    private void getAllFriendsKeys() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(Constants.FRIENDS_DB);

        myRef.child(Constants.FRIENDS_REQUESTS_DB).child(user.getUid()).child(Constants.GET_REQUESTS_DB).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //friendsRequests.clear();
                for (DataSnapshot requestSnapshot : dataSnapshot.getChildren()) {
                    String userID = requestSnapshot.getKey();
                    friendsRequests.add(userID);
                }

                getFriendsData();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                MySignal.getInstance().toast("Failed to read the friends request data");
            }
        });
    }

    private void getFriendsData() {
        ArrayList<User> allFriends = new ArrayList<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(Constants.USERS_DB);

        for (int i = 0; i < friendsRequests.size(); i++) {
            myRef.child(friendsRequests.get(i)).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User friend = dataSnapshot.getValue(User.class);
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
        friends_LST_friendsRequest.setLayoutManager(new LinearLayoutManager(getContext()));
        Adapter_Friend adapter_friend = new Adapter_Friend(getContext(), allFriends, Adapter_Friend.FRIENDS_STATE.FRIENDS_REQUEST);
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

            }

            @Override
            public void onAcceptRequest(int position) {
                acceptFriendRequest(allFriends.get(position));
            }

            @Override
            public void onRejectRequest(int position) {
                deleteFriendRequest(allFriends.get(position));
            }

            @Override
            public void onSendRequest(int position) {

            }
        });
        friends_LST_friendsRequest.setAdapter(adapter_friend);
    }

    private void openFriendFragment(User theUser) {
        //TODO
    }

    private void acceptFriendRequest(User theUser) {
        long date = System.currentTimeMillis();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(Constants.FRIENDS_DB);

        myRef.child(Constants.CURRENT_FRIENDS_DB).child(user.getUid()).child(theUser.getUid()).setValue(date);
        myRef.child(Constants.CURRENT_FRIENDS_DB).child(theUser.getUid()).child(user.getUid()).setValue(date);
        deleteFriendRequest(theUser);
    }

    private void deleteFriendRequest(User theUser) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(Constants.FRIENDS_DB);

        myRef.child(Constants.FRIENDS_REQUESTS_DB).child(user.getUid()).child(Constants.GET_REQUESTS_DB).child(theUser.getUid()).removeValue();
        myRef.child(Constants.FRIENDS_REQUESTS_DB).child(theUser.getUid()).child(Constants.SEND_REQUESTS_DB).child(user.getUid()).removeValue();
        friendsRequests.remove(theUser.getUid());
    }

}