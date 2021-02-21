package com.example.final_project.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;

import com.example.final_project.R;
import com.example.final_project.callbacks.CallBack_UserPicture;
import com.example.final_project.objects.User;
import com.example.final_project.utils.Constants;
import com.example.final_project.utils.MyDB;
import com.example.final_project.utils.MySP;
import com.example.final_project.utils.MySignal;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import java.util.Calendar;

public class Activity_Edit_Profile extends AppCompatActivity {
    private ShapeableImageView edit_profile_IMG_picture;
    private TextInputLayout edit_profile_EDT_firstName;
    private TextInputLayout edit_profile_EDT_lastName;
    private TextInputLayout edit_profile_EDT_height;
    private TextInputLayout edit_profile_EDT_weight;
    private MaterialButton edit_profile_BTN_birthDate;
    private MaterialButton edit_profile_BTN_saveChanges;
    private Toolbar edit_profile_TLB_toolbar;

    private User user;
    private Uri imageUri;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        findViews();
        initViews();
        initUser();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        openMenuActivity();
    }

    private void findViews() {
        edit_profile_IMG_picture = findViewById(R.id.edit_profile_IMG_picture);
        edit_profile_EDT_firstName = findViewById(R.id.edit_profile_EDT_firstName);
        edit_profile_EDT_lastName = findViewById(R.id.edit_profile_EDT_lastName);
        edit_profile_EDT_height = findViewById(R.id.edit_profile_EDT_height);
        edit_profile_EDT_weight = findViewById(R.id.edit_profile_EDT_weight);
        edit_profile_BTN_birthDate = findViewById(R.id.edit_profile_BTN_birthDate);
        edit_profile_BTN_saveChanges = findViewById(R.id.edit_profile_BTN_saveChanges);
        edit_profile_TLB_toolbar = findViewById(R.id.edit_profile_TLB_toolbar);
    }

    private void initViews() {
        setSupportActionBar(edit_profile_TLB_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        edit_profile_EDT_firstName.getEditText().addTextChangedListener(textWatcher);
        edit_profile_EDT_lastName.getEditText().addTextChangedListener(textWatcher);
        edit_profile_EDT_height.getEditText().addTextChangedListener(textWatcher);
        edit_profile_EDT_weight.getEditText().addTextChangedListener(textWatcher);

        edit_profile_IMG_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePicture();
            }
        });

        edit_profile_BTN_birthDate.setOnClickListener(new View.OnClickListener() {
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

        edit_profile_BTN_saveChanges.setOnClickListener(new View.OnClickListener() {
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
        String firstName = edit_profile_EDT_firstName.getEditText().getText().toString().trim();
        String lastName = edit_profile_EDT_lastName.getEditText().getText().toString().trim();
        String heightString = edit_profile_EDT_height.getEditText().getText().toString().trim();
        String weightString = edit_profile_EDT_weight.getEditText().getText().toString().trim();
        double height, weight;
        boolean valid = true;

        edit_profile_EDT_firstName.setErrorEnabled(false);
        edit_profile_EDT_lastName.setErrorEnabled(false);
        edit_profile_EDT_height.setErrorEnabled(false);
        edit_profile_EDT_weight.setErrorEnabled(false);

        if (firstName.isEmpty() || firstName.length() > 12) {
            edit_profile_EDT_firstName.setError(getResources().getString(R.string.name_error));
            valid = false;
        }

        if (lastName.isEmpty() || lastName.length() > 12) {
            edit_profile_EDT_lastName.setError(getResources().getString(R.string.name_error));
            valid = false;
        }

        if (!heightString.isEmpty()) {
            height = Double.parseDouble(heightString);

            if (height < 100 || height > 250) {
                edit_profile_EDT_height.setError(getResources().getString(R.string.height_error));
                valid = false;
            }
        } else {
            valid = false;
        }

        if (!weightString.isEmpty()) {
            weight = Double.parseDouble(weightString);

            if (weight < 30 || weight > 200) {
                edit_profile_EDT_weight.setError(getResources().getString(R.string.weight_error));
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
            edit_profile_BTN_saveChanges.setEnabled(checkValidation());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private void updateSelectedBirthDate(int year, int month, int dayOfMonth) {
        user.setBirthYear(year);
        user.setBirthMonth(month + 1);
        user.setBirthDay(dayOfMonth);
        edit_profile_BTN_saveChanges.setEnabled(checkValidation());
    }

    private void openDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = user.getBirthYear() != 0 ? user.getBirthYear() : calendar.get(Calendar.YEAR);
        int month = user.getBirthMonth() - 1 != 0 ? user.getBirthMonth() - 1 : calendar.get(Calendar.MONTH);
        int day = user.getBirthDay() != 0 ? user.getBirthDay() : calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(
                this,
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
            MySignal.getInstance().loadPicture(imageUri.toString(), edit_profile_IMG_picture);
        }
    }

    private void updateImage() {
        if (user == null) {
            return;
        }

        MyDB.readMyUserPicture(new CallBack_UserPicture() {
            @Override
            public void onPictureReady(String urlString) {
                MySignal.getInstance().loadPicture(urlString, edit_profile_IMG_picture);
            }
        });
    }

    void setDetails() {
        updateImage();
        edit_profile_EDT_firstName.getEditText().setText(user.getFirstName());
        edit_profile_EDT_lastName.getEditText().setText(user.getLastName());
        edit_profile_EDT_height.getEditText().setText("" + user.getHeight());
        edit_profile_EDT_weight.getEditText().setText("" + user.getWeight());
    }

    private void updateUserData() {
        user.setUid(MyDB.getUid());
        user.setPhone(MyDB.getPhoneNumber());
        user.setFirstName(edit_profile_EDT_firstName.getEditText().getText().toString());
        user.setLastName(edit_profile_EDT_lastName.getEditText().getText().toString());
        user.setHeight(Double.parseDouble(edit_profile_EDT_height.getEditText().getText().toString()));
        user.setWeight(Double.parseDouble(edit_profile_EDT_weight.getEditText().getText().toString()));

        MyDB.updateUser(user);
        MySP.getInstance().putString(MySP.KEYS.USER_DATA, new Gson().toJson(user));
        uploadPictureToDB();
    }

    private void openMenuActivity() {
        Intent myIntent = new Intent(this, Activity_Menu.class);
        startActivity(myIntent);
        finish();
    }

    private void uploadPictureToDB() {
        if (imageUri == null)
            return;

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();
        StorageReference ref = storageReference.child(Constants.IMAGES_DB + user.getUid());

        ref.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                openMenuActivity();
            }
        });
    }
}