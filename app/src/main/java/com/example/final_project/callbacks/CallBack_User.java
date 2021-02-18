package com.example.final_project.callbacks;

import com.example.final_project.objects.User;

public interface CallBack_User {
    void onUserReady(User user);
    void onUserFailure(String msg);
}
