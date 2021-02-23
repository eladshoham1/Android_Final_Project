package com.example.final_project.callbacks;

import java.util.HashMap;

public interface CallBack_Friends {
    void onFriendsReady(HashMap<String, String> friendsStatus);
    void onFriendsFailure(String msg);
}
