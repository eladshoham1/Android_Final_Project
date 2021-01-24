package com.example.final_project.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.final_project.R;
import com.example.final_project.objects.User;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;

public class Fragment_Profile extends Fragment {
    //private RecyclerView profile_LST_friends;
    private ShapeableImageView profile_IMG_picture;
    private TextView profile_LBL_userName;
    private TextView profile_LBL_age;
    private TextView profile_LBL_bmi;
    private TextView profile_LBL_height;
    private TextView profile_LBL_weight;
    private User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        findViews(view);
        getUserData();

        return view;
    }

    private void findViews(View view) {
        //profile_LST_friends = view.findViewById(R.id.profile_LST_friends);
        profile_IMG_picture = view.findViewById(R.id.profile_IMG_picture);
        profile_LBL_userName = view.findViewById(R.id.profile_LBL_userName);
        profile_LBL_age = view.findViewById(R.id.profile_LBL_age);
        profile_LBL_bmi = view.findViewById(R.id.profile_LBL_bmi);
        profile_LBL_height = view.findViewById(R.id.profile_LBL_height);
        profile_LBL_weight = view.findViewById(R.id.profile_LBL_weight);
    }

    private void getUserData() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users");

        myRef.child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                initViews((User)dataSnapshot.getValue(User.class));
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.d("pttt", "Failed to read value.", error.toException());
            }
        });
    }

    private void initViews(User user) {
        //profile_IMG_picture.setImageResource(user.getPicture());
        profile_LBL_userName.setText(user.getFirstName() + " " + user.getLastName());
        profile_LBL_age.setText("" + user.getAge());
        profile_LBL_bmi.setText(new DecimalFormat("##.##").format(user.getBmi()));
        profile_LBL_height.setText("" + user.getHeight());
        profile_LBL_weight.setText("" + user.getWeight());

    }
}