package com.example.final_project.callbacks;

import com.example.final_project.objects.User;

public interface Callback_UserData {
    void onUserReady(User theUser);
    void onUserFailure(String msg);
}
