package com.example.final_project.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.final_project.R;
import com.example.final_project.callbacks.CallBack_UserPicture;
import com.example.final_project.objects.User;
import com.example.final_project.utils.MyDB;
import com.example.final_project.utils.MySignal;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;

public class Adapter_Friend_Request extends RecyclerView.Adapter<Adapter_Friend_Request.MyViewHolder> {

    private ArrayList<User> friendsRequests;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    public Adapter_Friend_Request(Context context, ArrayList<User> friendsRequests) {
        if (context != null) {
            this.mInflater = LayoutInflater.from(context);
            this.friendsRequests = friendsRequests;
        }
    }

    // inflates the row layout from xml when needed
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_friends_requests, parent, false);
        return new MyViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        User friend = friendsRequests.get(position);

        updateFriendPicture(friend, holder);
        holder.friends_requests_LBL_userName.setText(friend.getFirstName() + " " + friend.getLastName());
    }

    public void updateFriendPicture(User friend, MyViewHolder holder) {
        MyDB.readUserPicture(friend.getUid(), new CallBack_UserPicture() {
            @Override
            public void onPictureReady(String urlString) {
                MySignal.getInstance().loadPicture(urlString, holder.friends_requests_IMG_picture);
            }
        });
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return friendsRequests.size();
    }

    // convenience method for getting data at click position
    public User getItem(int id) {
        return friendsRequests.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
        void onAcceptRequest(int position);
        void onRejectRequest(int position);
    }

    // stores and recycles views as they are scrolled off screen
    public class MyViewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView friends_requests_IMG_picture;
        TextView friends_requests_LBL_userName;
        MaterialButton friends_requests_BTN_acceptRequest;
        MaterialButton friends_requests_BTN_rejectRequest;

        MyViewHolder(View itemView) {
            super(itemView);
            friends_requests_IMG_picture = itemView.findViewById(R.id.friends_requests_IMG_picture);
            friends_requests_LBL_userName = itemView.findViewById(R.id.friends_requests_LBL_userName);
            friends_requests_BTN_acceptRequest = itemView.findViewById(R.id.friends_requests_BTN_acceptRequest);
            friends_requests_BTN_rejectRequest = itemView.findViewById(R.id.friends_requests_BTN_rejectRequest);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mClickListener != null) {
                        mClickListener.onItemClick(v, getAdapterPosition());
                    }
                }
            });

            friends_requests_BTN_acceptRequest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mClickListener != null) {
                        mClickListener.onAcceptRequest(getAdapterPosition());
                    }
                }
            });

            friends_requests_BTN_rejectRequest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mClickListener != null) {
                        mClickListener.onRejectRequest(getAdapterPosition());
                    }
                }
            });
        }
    }
}