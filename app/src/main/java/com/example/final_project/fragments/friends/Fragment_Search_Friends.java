package com.example.final_project.fragments.friends;

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
import com.example.final_project.adapters.Adapter_Friend;
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

public class Fragment_Search_Friends extends Fragment {
    private TextInputEditText friends_EDT_searchFriends;
    private RecyclerView friends_LST_searchFriends;

    private User user;
    private ArrayList<User> allUsers;
    private Adapter_Friend adapter_friend;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_friends, container, false);
        findViews(view);
        initViews();
        initUser();

        return view;
    }

    private void findViews(View view) {
        friends_EDT_searchFriends = view.findViewById(R.id.friends_EDT_searchFriends);
        friends_LST_searchFriends = view.findViewById(R.id.friends_LST_searchFriends);
    }

    private void initViews() {
        friends_EDT_searchFriends.addTextChangedListener(new TextWatcher() {
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

    private void initUser() {
        String userString = MySP.getInstance().getString(MySP.KEYS.USER_DATA, "");
        user = new Gson().fromJson(userString, User.class);

        allUsers = new ArrayList<>();
        setUsersRecyclerView();
        getAllUsersList();
    }

    private void filter(String text) {
        String fullName;
        ArrayList<User> filteredUsers = new ArrayList<>();

        for (User user : allUsers) {
            fullName = user.getFirstName() + " " + user.getLastName();

            if (fullName.toLowerCase().contains(text.toLowerCase()))
                filteredUsers.add(user);
        }

        adapter_friend.filterList(filteredUsers);
    }

    private void getAllUsersList() { //TODO remove exist friends and requests
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(Constants.USERS_DB);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //allUsers.clear();
                for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                    User userFromDB = userSnapshot.getValue(User.class);

                    if (!user.getUid().equals(userFromDB.getUid()))
                        allUsers.add(userFromDB);
                }

                adapter_friend.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                MySignal.getInstance().toast("Failed to read the all users data");
            }
        });
    }

    private void setUsersRecyclerView() {
        friends_LST_searchFriends.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter_friend = new Adapter_Friend(getContext(), allUsers, Adapter_Friend.FRIENDS_STATE.SEARCH_FRIENDS);
        friends_LST_searchFriends.setAdapter(adapter_friend);

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
    }

    private void openFriendFragment(User theUser) {
        //TODO
    }

    private void sendFriendRequest(User theUser) {
        long currentDate = System.currentTimeMillis();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(Constants.FRIENDS_DB);

        myRef.child(Constants.FRIENDS_REQUESTS_DB).child(user.getUid()).child(Constants.SEND_REQUESTS_DB).child(theUser.getUid()).setValue(currentDate);
        myRef.child(Constants.FRIENDS_REQUESTS_DB).child(theUser.getUid()).child(Constants.GET_REQUESTS_DB).child(user.getUid()).setValue(currentDate);
    }
}