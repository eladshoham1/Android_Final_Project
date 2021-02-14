package com.example.final_project.callbacks;

import java.util.ArrayList;

public interface Callback_FriendsData {
    void onFriendsReady(ArrayList<String> friends);
    void onFriendsFailure(String msg);
}
