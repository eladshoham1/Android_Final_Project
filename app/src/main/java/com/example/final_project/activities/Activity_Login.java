package com.example.final_project.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.final_project.R;
import com.example.final_project.callbacks.CallBack_User;
import com.example.final_project.objects.User;
import com.example.final_project.utils.Constants;
import com.example.final_project.utils.MyDB;
import com.example.final_project.utils.MySP;
import com.example.final_project.utils.MySignal;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

public class Activity_Login extends AppCompatActivity {

    private enum LOGIN_STATE {
        ENTERING_NUMBER,
        ENTERING_CODE,
    }

    private TextInputLayout login_EDT_phone;
    private MaterialButton login_BTN_continue;

    private LOGIN_STATE login_state = LOGIN_STATE.ENTERING_NUMBER;
    private String verificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViews();
        initViews();
        updateUI();
    }

    private void findViews() {
        login_EDT_phone = findViewById(R.id.login_EDT_phone);
        login_BTN_continue = findViewById(R.id.login_BTN_continue);
    }

    private void initViews() {
        login_BTN_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                continueClicked();
            }
        });
    }

    private void continueClicked() {
        if (login_state == LOGIN_STATE.ENTERING_NUMBER) {
            startLoginProcess();
        } else if (login_state == LOGIN_STATE.ENTERING_CODE) {
            codeEntered();
        }
    }

    private void startLoginProcess() {
        String phoneNumber = login_EDT_phone.getEditText().getText().toString().trim();

        if (phoneNumber.isEmpty() || phoneNumber.length() < 10) {
            login_EDT_phone.setError("Valid number is required");
            login_EDT_phone.requestFocus();
            return;
        }

        sendVerificationCode(phoneNumber);
    }

    private void codeEntered() {
        String code = login_EDT_phone.getEditText().getText().toString().trim();

        if (code.isEmpty() || code.length() < 6) {
            login_EDT_phone.setError("Wrong code");
            login_EDT_phone.requestFocus();
            return;
        }

        verifyCode(code);
    }

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            readUserData();
                        } else {
                            MySignal.getInstance().toast("Wrong Code");
                            updateUI();
                        }
                    }
                });
    }

    private void readUserData() {
        MyDB.readUserData(new CallBack_User() {
            @Override
            public void onUserReady(User user) {
                startApp(user);
            }

            @Override
            public void onUserFailure(String msg) {
                startApp(null);
            }
        });
    }

    private void startApp(User user) {
        Intent myIntent;

        if (user != null) {
            myIntent = new Intent(this, Activity_Menu.class);
            MySP.getInstance().putString(MySP.KEYS.USER_DATA, new Gson().toJson(user));
        } else {
            myIntent = new Intent(this, Activity_Create_User.class);
        }

        startActivity(myIntent);
        finish();
    }

    private void sendVerificationCode(String phoneNumber) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(firebaseAuth)
                .setPhoneNumber(phoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(onVerificationStateChangedCallbacks)
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks onVerificationStateChangedCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            verificationId = s;
            login_state = LOGIN_STATE.ENTERING_CODE;
            updateUI();
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();

            if (code != null) {
                login_EDT_phone.getEditText().setText(code);
                verifyCode(code);
            }
        }

        @Override
        public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
            super.onCodeAutoRetrievalTimeOut(s);

            login_EDT_phone.setError("Time out, please try again");
            login_EDT_phone.requestFocus();
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            MySignal.getInstance().toast(e.getMessage());
            login_EDT_phone.setError("Wrong number");
            login_state = LOGIN_STATE.ENTERING_NUMBER;
            updateUI();
        }
    };

    private void updateUI() {
        if (login_state == LOGIN_STATE.ENTERING_NUMBER) {
            login_EDT_phone.getEditText().setText("+972501111111");
            login_EDT_phone.setHint(getString(R.string.phone_number));
            login_EDT_phone.setPlaceholderText("+972 50 1111111");
            login_BTN_continue.setText(getString(R.string.continue_));
        } else if (login_state == LOGIN_STATE.ENTERING_CODE) {
            login_EDT_phone.getEditText().setText("");
            login_EDT_phone.setHint(getString(R.string.enter_code));
            login_EDT_phone.setPlaceholderText("******");
            login_EDT_phone.setErrorEnabled(false);
            login_BTN_continue.setText(getString(R.string.login));
        }
    }

}