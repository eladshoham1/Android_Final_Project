package com.example.final_project.fragments.user;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.example.final_project.R;
import com.example.final_project.activities.Activity_Menu;
import com.example.final_project.callbacks.CallBack_UserPicture;
import com.example.final_project.objects.User;
import com.example.final_project.utils.Constants;
import com.example.final_project.utils.MyDB;
import com.example.final_project.utils.MySP;
import com.example.final_project.utils.MySignal;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import java.util.Calendar;

import static android.app.Activity.RESULT_OK;

public class Fragment_Edit_Profile extends Fragment {
    private ShapeableImageView editProfile_IMG_picture;
    private TextInputLayout editProfile_EDT_firstName;
    private TextInputLayout editProfile_EDT_lastName;
    private TextInputLayout editProfile_EDT_height;
    private TextInputLayout editProfile_EDT_weight;
    private MaterialButton editProfile_BTN_birthDate;
    private MaterialButton editProfile_BTN_saveChanges;

    private User user;
    private Uri imageUri;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private boolean checkValid = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        findViews(view);
        initViews();
        initUser();

        return view;
    }

    private void findViews(View view) {
        editProfile_IMG_picture = view.findViewById(R.id.editProfile_IMG_picture);
        editProfile_EDT_firstName = view.findViewById(R.id.editProfile_EDT_firstName);
        editProfile_EDT_lastName = view.findViewById(R.id.editProfile_EDT_lastName);
        editProfile_EDT_height = view.findViewById(R.id.editProfile_EDT_height);
        editProfile_EDT_weight = view.findViewById(R.id.editProfile_EDT_weight);
        editProfile_BTN_birthDate = view.findViewById(R.id.editProfile_BTN_birthDate);
        editProfile_BTN_saveChanges = view.findViewById(R.id.editProfile_BTN_saveChanges);
    }

    private void initViews() {
        editProfile_EDT_firstName.getEditText().addTextChangedListener(textWatcher);
        editProfile_EDT_lastName.getEditText().addTextChangedListener(textWatcher);
        editProfile_EDT_height.getEditText().addTextChangedListener(textWatcher);
        editProfile_EDT_weight.getEditText().addTextChangedListener(textWatcher);

        editProfile_IMG_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePicture();
            }
        });

        editProfile_BTN_birthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePicker();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                view.updateDate(year, month, dayOfMonth);
                updateSelectedBirthDate(year, month, dayOfMonth);
            }
        };

        editProfile_BTN_saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserData();
            }
        });
    }

    private void initUser() {
        String userString = MySP.getInstance().getString(MySP.KEYS.USER_DATA, "");

        if (!userString.isEmpty()) {
            user = new Gson().fromJson(userString, User.class);
        } else {
            user = new User();
        }

        setDetails();
    }

    private boolean checkValidation() {
        String firstName = editProfile_EDT_firstName.getEditText().getText().toString().trim();
        String lastName = editProfile_EDT_lastName.getEditText().getText().toString().trim();
        String heightString = editProfile_EDT_height.getEditText().getText().toString().trim();
        String weightString = editProfile_EDT_weight.getEditText().getText().toString().trim();
        double height, weight;
        boolean valid = true;

        editProfile_EDT_firstName.setErrorEnabled(false);
        editProfile_EDT_lastName.setErrorEnabled(false);
        editProfile_EDT_height.setErrorEnabled(false);
        editProfile_EDT_weight.setErrorEnabled(false);

        if (firstName.isEmpty() || firstName.length() > 12) {
            editProfile_EDT_firstName.setError(getResources().getString(R.string.name_error));
            editProfile_EDT_firstName.requestFocus();
            valid = false;
        }

        if (lastName.isEmpty() || lastName.length() > 12) {
            editProfile_EDT_lastName.setError(getResources().getString(R.string.name_error));
            editProfile_EDT_lastName.requestFocus();
            valid = false;
        }

        if (!heightString.isEmpty()) {
            height = Double.parseDouble(heightString);

            if (height < 100 || height > 250) {
                editProfile_EDT_height.setError(getResources().getString(R.string.height_error));
                editProfile_EDT_height.requestFocus();
                valid = false;
            }
        } else {
            valid = false;
        }

        if (!weightString.isEmpty()) {
            weight = Double.parseDouble(weightString);

            if (weight < 30 || weight > 200) {
                editProfile_EDT_weight.setError(getResources().getString(R.string.weight_error));
                editProfile_EDT_weight.requestFocus();
                valid = false;
            }
        } else {
            valid = false;
        }

        return valid;
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            editProfile_BTN_saveChanges.setEnabled(checkValidation());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private void updateSelectedBirthDate(int year, int month, int dayOfMonth) {
        user.setBirthYear(year);
        user.setBirthMonth(month + 1);
        user.setBirthDay(dayOfMonth);
        editProfile_BTN_saveChanges.setEnabled(checkValidation());
    }

    private void openDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = user.getBirthYear() != 0 ? user.getBirthYear() : calendar.get(Calendar.YEAR);
        int month = user.getBirthMonth() - 1 != 0 ? user.getBirthMonth() - 1 : calendar.get(Calendar.MONTH);
        int day = user.getBirthDay() != 0 ? user.getBirthDay() : calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(
                getContext(),
                mDateSetListener,
                year, month, day);
        dialog.show();
    }

    private void updatePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(intent, "Select picture"), Constants.PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            MySignal.getInstance().loadPicture(imageUri.toString(), editProfile_IMG_picture);
        }
    }

    private void updateImage() {
        if (user == null) {
            return;
        }

        MyDB.readUserPicture(new CallBack_UserPicture() {
            @Override
            public void onPictureReady(String urlString) {
                MySignal.getInstance().loadPicture(urlString, editProfile_IMG_picture);
            }
        });
    }

    void setDetails() {
        updateImage();
        editProfile_EDT_firstName.setPlaceholderText(user.getFirstName());
        editProfile_EDT_lastName.setPlaceholderText(user.getLastName());
        editProfile_EDT_height.setPlaceholderText("" + user.getHeight());
        editProfile_EDT_weight.setPlaceholderText("" + user.getWeight());
    }

    private void updateUserData() {
        uploadPictureToDB();

        user.setUid(MyDB.getUid());
        user.setPhone(MyDB.getPhoneNumber());
        user.setFirstName(editProfile_EDT_firstName.getEditText().getText().toString());
        user.setLastName(editProfile_EDT_lastName.getEditText().getText().toString());
        user.setHeight(Double.parseDouble(editProfile_EDT_height.getEditText().getText().toString()));
        user.setWeight(Double.parseDouble(editProfile_EDT_weight.getEditText().getText().toString()));

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(Constants.USERS_DB);
        myRef.child(user.getUid()).setValue(user);

        MySP.getInstance().putString(MySP.KEYS.USER_DATA, new Gson().toJson(user));

        openMenuActivity();
    }

    private void openMenuActivity() {
        Intent myIntent = new Intent(getContext(), Activity_Menu.class);
        startActivity(myIntent);
        getActivity().finish();
    }

    private void uploadPictureToDB() {
        if (imageUri == null)
            return;

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();
        StorageReference ref = storageReference.child(Constants.IMAGES_DB + user.getUid());

        ref.putFile(imageUri);
    }

}