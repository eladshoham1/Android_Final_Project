package com.example.final_project.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.final_project.R;
import com.example.final_project.callbacks.CallBack_User;
import com.example.final_project.callbacks.CallBack_UserPicture;
import com.example.final_project.objects.BirthDate;
import com.example.final_project.objects.User;
import com.example.final_project.utils.Constants;
import com.example.final_project.utils.MyDB;
import com.example.final_project.utils.MySignal;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;

public class Activity_Edit_Profile extends Activity_Base {
    private ShapeableImageView edit_profile_IMG_picture;
    private ImageView edit_profile_IMG_uploadPicture;
    private ImageView edit_profile_IMG_deletePicture;
    private TextInputLayout edit_profile_EDT_firstName;
    private TextInputLayout edit_profile_EDT_lastName;
    private TextInputLayout edit_profile_EDT_height;
    private TextInputLayout edit_profile_EDT_weight;
    private MaterialButton edit_profile_BTN_birthDate;
    private MaterialButton edit_profile_BTN_saveChanges;
    private ProgressBar edit_profile_PRB_loading;
    private Toolbar edit_profile_TLB_toolbar;

    private boolean userComplete = true;
    private User theUser;
    private Uri pictureUri;
    private boolean pictureExist;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    private boolean firstNameValid;
    private boolean lastNameValid;
    private boolean heightValid;
    private boolean weightValid;
    private boolean birthDateValid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        findViews();
        initUser();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (userComplete) {
            openMenuActivity();
        } else {
            finish();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            pictureUri = data.getData();
            pictureExist = true;
            MySignal.getInstance().loadPicture(pictureUri.toString(), edit_profile_IMG_picture);
            updatePictureOptionView();
        }
    }

    private void findViews() {
        edit_profile_IMG_picture = findViewById(R.id.edit_profile_IMG_picture);
        edit_profile_IMG_uploadPicture = findViewById(R.id.edit_profile_IMG_uploadPicture);
        edit_profile_IMG_deletePicture = findViewById(R.id.edit_profile_IMG_deletePicture);
        edit_profile_EDT_firstName = findViewById(R.id.edit_profile_EDT_firstName);
        edit_profile_EDT_lastName = findViewById(R.id.edit_profile_EDT_lastName);
        edit_profile_EDT_height = findViewById(R.id.edit_profile_EDT_height);
        edit_profile_EDT_weight = findViewById(R.id.edit_profile_EDT_weight);
        edit_profile_BTN_birthDate = findViewById(R.id.edit_profile_BTN_birthDate);
        edit_profile_BTN_saveChanges = findViewById(R.id.edit_profile_BTN_saveChanges);
        edit_profile_PRB_loading = findViewById(R.id.edit_profile_PRB_loading);
        edit_profile_TLB_toolbar = findViewById(R.id.edit_profile_TLB_toolbar);
    }

    private void initUser() {
        MyDB.readMyUserData(new CallBack_User() {
            @Override
            public void onUserReady(User user) {
                if (user != null) {
                    if (user.getUid().equals(MyDB.getUid())) {
                        theUser = user;
                    } else {
                        openMenuActivity();
                    }
                } else {
                    theUser = new User();
                    userComplete = false;
                }

                initViews();
                setDetails();
            }

            @Override
            public void onUserFailure(String msg) {
                MySignal.getInstance().toast(msg);
            }
        });
    }

    private void initViews() {
        setSupportActionBar(edit_profile_TLB_toolbar);
        if (theUser != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        edit_profile_EDT_firstName.getEditText().addTextChangedListener(firstNameWatcher);
        edit_profile_EDT_lastName.getEditText().addTextChangedListener(lastNameWatcher);
        edit_profile_EDT_height.getEditText().addTextChangedListener(heightWatcher);
        edit_profile_EDT_weight.getEditText().addTextChangedListener(weightWatcher);

        edit_profile_IMG_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePicture();
            }
        });

        edit_profile_IMG_deletePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletePicture();
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

    void setDetails() {
        updateUserPicture();

        if (!theUser.getFirstName().isEmpty()) {
            edit_profile_EDT_firstName.getEditText().setText(theUser.getFirstName());
        } else {
            edit_profile_EDT_firstName.setErrorEnabled(false);
        }

        if (!theUser.getLastName().isEmpty()) {
            edit_profile_EDT_lastName.getEditText().setText(theUser.getLastName());
        } else {
            edit_profile_EDT_lastName.setErrorEnabled(false);
        }

        if (theUser.getHeight() != 0) {
            edit_profile_EDT_height.getEditText().setText("" + theUser.getHeight());
        } else {
            edit_profile_EDT_height.setErrorEnabled(false);
        }

        if (theUser.getWeight() != 0) {
            edit_profile_EDT_weight.getEditText().setText("" + theUser.getWeight());
        } else {
            edit_profile_EDT_weight.setErrorEnabled(false);
        }

        if (theUser.getBirthDate() != null) {
            birthDateValid = true;
            setSaveChangesButton();
        }
    }

    private void updatePicture() {
        Intent intent = new Intent();
        intent.setType(Constants.IMAGES_FILES);
        intent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(intent, Constants.SELECT_PICTURE), Constants.PICK_IMAGE);
    }

    private void deletePicture() {
        pictureUri = null;
        pictureExist = false;
        MySignal.getInstance().loadPicture(null, edit_profile_IMG_picture);
        updatePictureOptionView();
    }

    private void updateUserPicture() {
        if (!theUser.getPictureUrl().isEmpty()) {
            pictureExist = true;
            MySignal.getInstance().loadPicture(theUser.getPictureUrl(), edit_profile_IMG_picture);
        } else {
            pictureExist = false;
        }

        updatePictureOptionView();
    }

    private void openDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = theUser.getBirthDate() != null ? theUser.getBirthDate().getYear() : calendar.get(Calendar.YEAR);
        int month = theUser.getBirthDate() != null ? theUser.getBirthDate().getMonth() - 1 : calendar.get(Calendar.MONTH);
        int day = theUser.getBirthDate() != null ? theUser.getBirthDate().getDay() : calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(
                this,
                mDateSetListener,
                year, month, day);
        dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        dialog.show();
    }

    private void updateSelectedBirthDate(int year, int month, int dayOfMonth) {
        theUser.setBirthDate(new BirthDate(dayOfMonth, month + 1, year));
        birthDateValid = true;
        setSaveChangesButton();
    }

    private void updateUserData() {
        edit_profile_PRB_loading.setVisibility(View.VISIBLE);
        theUser.setUid(MyDB.getUid());
        theUser.setPhone(MyDB.getPhoneNumber());
        theUser.setFirstName(edit_profile_EDT_firstName.getEditText().getText().toString());
        theUser.setLastName(edit_profile_EDT_lastName.getEditText().getText().toString());
        theUser.setHeight(Double.parseDouble(edit_profile_EDT_height.getEditText().getText().toString()));
        theUser.setWeight(Double.parseDouble(edit_profile_EDT_weight.getEditText().getText().toString()));
        uploadPictureToDB();
    }

    private void uploadPictureToDB() {
        if (pictureExist && pictureUri != null) {
            MyDB.updateMyUserPicture(pictureUri, new CallBack_UserPicture() {
                @Override
                public void onPictureReady(String urlString) {
                    theUser.setPictureUrl(urlString);
                    uploadUserToDB();
                }
            });
        } else if (!pictureExist && pictureUri == null){
            MyDB.deleteMyUserPicture();
            theUser.setPictureUrl("");
            uploadUserToDB();
        } else {
            uploadUserToDB();
        }
    }

    private void uploadUserToDB() {
        MyDB.updateUser(theUser, new CallBack_User() {
            @Override
            public void onUserReady(User user) {
                openMenuActivity();
            }

            @Override
            public void onUserFailure(String msg) {
                MySignal.getInstance().toast(msg);
            }
        });
    }

    private void updatePictureOptionView() {
        if (pictureExist) {
            edit_profile_IMG_uploadPicture.setVisibility(View.GONE);
            edit_profile_IMG_deletePicture.setVisibility(View.VISIBLE);
        } else {
            edit_profile_IMG_uploadPicture.setVisibility(View.VISIBLE);
            edit_profile_IMG_deletePicture.setVisibility(View.GONE);
        }
    }

    private boolean checkName(TextInputLayout textInputLayout) {
        String name = textInputLayout.getEditText().getText().toString().trim();
        if (name.isEmpty() || name.length() > Constants.MAX_NAME_CHARACTERS) {
            textInputLayout.setError(getResources().getString(R.string.name_error));
            return false;
        }

        return true;
    }

    private boolean checkMeasure(TextInputLayout textInputLayout, int errorMessage, int lowValue, int highValue) {
        String measureString = textInputLayout.getEditText().getText().toString().trim();
        double measure = 0.0;

        if (!measureString.isEmpty()) {
            try {
                measure = Double.parseDouble(measureString);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            if (measure < lowValue || measure > highValue) {
                textInputLayout.setError(getResources().getString(errorMessage));
                return false;
            }
        } else {
            textInputLayout.setError(getResources().getString(R.string.empty_error));
            return false;
        }

        return true;
    }

    private void setSaveChangesButton() {
        edit_profile_BTN_saveChanges.setEnabled(firstNameValid && lastNameValid && heightValid && weightValid && birthDateValid);
    }

    private TextWatcher firstNameWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            edit_profile_EDT_firstName.setErrorEnabled(false);
            firstNameValid = checkName(edit_profile_EDT_firstName);
            setSaveChangesButton();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private TextWatcher lastNameWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            edit_profile_EDT_lastName.setErrorEnabled(false);
            lastNameValid = checkName(edit_profile_EDT_lastName);
            setSaveChangesButton();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private TextWatcher heightWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            edit_profile_EDT_height.setErrorEnabled(false);
            heightValid = checkMeasure(edit_profile_EDT_height, R.string.height_error, Constants.MIN_HEIGHT, Constants.MAX_HEIGHT);
            setSaveChangesButton();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private TextWatcher weightWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            edit_profile_EDT_weight.setErrorEnabled(false);
            weightValid = checkMeasure(edit_profile_EDT_weight, R.string.weight_error, Constants.MIN_WEIGHT, Constants.MAX_WEIGHT);
            setSaveChangesButton();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
}