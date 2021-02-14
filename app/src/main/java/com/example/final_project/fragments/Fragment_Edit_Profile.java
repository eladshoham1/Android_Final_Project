package com.example.final_project.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.example.final_project.R;
import com.example.final_project.activities.Activity_Create_User;
import com.example.final_project.activities.Activity_Menu;
import com.example.final_project.callbacks.Callback_UserData;
import com.example.final_project.utils.MySP;
import com.example.final_project.utils.database.MyDB;
import com.example.final_project.objects.User;
import com.example.final_project.utils.MySignal;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.time.LocalDate;
import java.time.Period;

public class Fragment_Edit_Profile extends Fragment {
    private ShapeableImageView editProfile_IMG_picture;
    private TextInputLayout editProfile_EDT_firstName;
    private TextInputLayout editProfile_EDT_lastName;
    private TextInputLayout editProfile_EDT_height;
    private TextInputLayout editProfile_EDT_weight;
    private DatePicker editProfile_DAT_birthDate;
    private MaterialButton editProfile_BTN_saveChanges;
    private User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        findViews(view);
        initViews();

        return view;
    }

    private void findViews(View view) {
        user = MyDB.getInstance().getUserData();

        editProfile_IMG_picture = view.findViewById(R.id.editProfile_IMG_picture);
        editProfile_EDT_firstName = view.findViewById(R.id.editProfile_EDT_firstName);
        editProfile_EDT_lastName = view.findViewById(R.id.editProfile_EDT_lastName);
        editProfile_DAT_birthDate = view.findViewById(R.id.editProfile_DAT_birthDate);
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
        editProfile_EDT_height.setHint("" + user.getHeight());
        editProfile_EDT_weight.setHint("" + user.getWeight());
    }

    private boolean allValid() { //TODO update
        return true;
    }

    private void updateUser() {
        long day, month, year, millisecond;
        day = editProfile_DAT_birthDate.getDayOfMonth();
        month = editProfile_DAT_birthDate.getMonth();
        year = editProfile_DAT_birthDate.getYear();
        millisecond = day + month * 30 + year * 365;
        millisecond *= 24 * 60 * 60 * 1000;
        user.setFirstName(editProfile_EDT_firstName.getEditText().getText().toString());
        user.setLastName(editProfile_EDT_lastName.getEditText().getText().toString());
        Log.d("check2", "" + millisecond);
        user.setDateOfBirth(millisecond - System.currentTimeMillis());
        if (!editProfile_EDT_height.getEditText().getText().toString().isEmpty())
            user.setHeight(Double.parseDouble(editProfile_EDT_height.getEditText().getText().toString()));
        if (!editProfile_EDT_weight.getEditText().getText().toString().isEmpty())
            user.setWeight(Double.parseDouble(editProfile_EDT_weight.getEditText().getText().toString()));

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users");
        myRef.child(user.getUid()).setValue(user);
        MySP.getInstance().putString(MySP.KEYS.USER_DATA, new Gson().toJson(user));
        openProfile();
    }

    private void openProfile() {
        Intent myIntent = new Intent(getContext(), Activity_Menu.class);
        MySP.getInstance().putString(MySP.KEYS.USER_DATA, new Gson().toJson(user));
        startActivity(myIntent);
    }
}