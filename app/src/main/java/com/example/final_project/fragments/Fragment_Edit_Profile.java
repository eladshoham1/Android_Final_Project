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
import com.example.final_project.callbacks.Callback_UserData;
import com.example.final_project.database.MyDB;
import com.example.final_project.objects.User;
import com.example.final_project.utils.MySignal;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Fragment_Edit_Profile extends Fragment {
    private ShapeableImageView editProfile_IMG_picture;
    private TextInputLayout editProfile_EDT_firstName;
    private TextInputLayout editProfile_EDT_lastName;
    private TextInputLayout editProfile_EDT_birthDate;
    private TextInputLayout editProfile_EDT_height;
    private TextInputLayout editProfile_EDT_weight;
    private MaterialButton editProfile_BTN_saveChanges;
    private User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        findViews(view);
        MyDB.readUserData(new Callback_UserData() {
            @Override
            public void onUserReady(User theUser) {
                user = theUser;
                initViews();
            }

            @Override
            public void onUserFailure(String msg) {
                MySignal.getInstance().toast(msg);
            }
        });

        return view;
    }

    private void findViews(View view) {
        editProfile_IMG_picture = view.findViewById(R.id.editProfile_IMG_picture);
        editProfile_EDT_firstName = view.findViewById(R.id.editProfile_EDT_firstName);
        editProfile_EDT_lastName = view.findViewById(R.id.editProfile_EDT_lastName);
        editProfile_EDT_birthDate = view.findViewById(R.id.editProfile_EDT_birthDate);
        editProfile_EDT_height = view.findViewById(R.id.editProfile_EDT_height);
        editProfile_EDT_weight = view.findViewById(R.id.editProfile_EDT_weight);
        editProfile_BTN_saveChanges = view.findViewById(R.id.editProfile_BTN_saveChanges);
    }

    private void initViews() {
        hintViews();

        editProfile_BTN_saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (allValid())
                    updateUser();
            }
        });
    }

    void hintViews() {
        editProfile_EDT_firstName.setHint(user.getFirstName());
        editProfile_EDT_lastName.setHint(user.getLastName());
        editProfile_EDT_birthDate.setHint("" + user.getDateOfBirth());
        editProfile_EDT_height.setHint("" + user.getHeight());
        editProfile_EDT_weight.setHint("" + user.getWeight());
    }

    private boolean allValid() { //TODO update
        return true;
    }

    private void updateUser() {
        user.setFirstName(editProfile_EDT_firstName.getEditText().getText().toString());
        user.setLastName(editProfile_EDT_lastName.getEditText().getText().toString());
        if (!editProfile_EDT_birthDate.getEditText().getText().toString().isEmpty())
            user.setDateOfBirth(Long.parseLong(editProfile_EDT_birthDate.getEditText().getText().toString()));
        if (!editProfile_EDT_height.getEditText().getText().toString().isEmpty())
            user.setHeight(Double.parseDouble(editProfile_EDT_height.getEditText().getText().toString()));
        if (!editProfile_EDT_weight.getEditText().getText().toString().isEmpty())
            user.setWeight(Double.parseDouble(editProfile_EDT_weight.getEditText().getText().toString()));

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users");
        myRef.child(user.getUid()).setValue(user);
    }
}