package com.example.final_project.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.final_project.R;
import com.example.final_project.objects.User;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;

public class Adapter_Friend extends RecyclerView.Adapter<Adapter_Friend.MyViewHolder> {

    public enum FRIENDS_STATE {
        MY_FRIENDS,
        MY_FRIENDS_REQUEST,
        FRIENDS_REQUEST,
        SEARCH_FRIENDS
    }

    private ArrayList<User> friends;
    private LayoutInflater mInflater;
    private FRIENDS_STATE state;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    public Adapter_Friend(Context context, ArrayList<User> friends, FRIENDS_STATE state) {
        if (context != null) {
            this.mInflater = LayoutInflater.from(context);
            this.friends = friends;
            this.state = state;
        }
    }

    // inflates the row layout from xml when needed
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_friends, parent, false);
        return new MyViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        User friend = friends.get(position);
        /*holder.friends_IMG_picture = friend.getPicture();
        Glide
                .with(mInflater.getContext())
                .load(post.getUserImageUrl())
                .centerCrop()
                .into(holder.post_IMG_user);*/

        holder.friends_LBL_userName.setText(friend.getFirstName() + " " + friend.getLastName());
        holder.friends_LBL_date.setText("" + friend.getAge());

        switch (state) {
            case MY_FRIENDS:
                holder.friends_BTN_compete.setVisibility(View.VISIBLE);
                break;
            case MY_FRIENDS_REQUEST:
                holder.friends_BTN_cancelRequest.setVisibility(View.VISIBLE);
                break;
            case FRIENDS_REQUEST:
                holder.friends_BTN_acceptRequest.setVisibility(View.VISIBLE);
                holder.friends_BTN_rejectRequest.setVisibility(View.VISIBLE);
                break;
            case SEARCH_FRIENDS:
                holder.friends_BTN_sendRequest.setVisibility(View.VISIBLE);
                break;
        }
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return friends.size();
    }

    // convenience method for getting data at click position
    public User getItem(int id) {
        return friends.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
        void onCompeteClick(int position);
        void onCancelRequest(int position);
        void onAcceptRequest(int position);
        void onRejectRequest(int position);
        void onSendRequest(int position);
    }

    // stores and recycles views as they are scrolled off screen
    public class MyViewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView friends_IMG_picture;
        TextView friends_LBL_userName;
        TextView friends_LBL_date;
        MaterialButton friends_BTN_compete;
        MaterialButton friends_BTN_cancelRequest;
        MaterialButton friends_BTN_acceptRequest;
        MaterialButton friends_BTN_rejectRequest;
        MaterialButton friends_BTN_sendRequest;

        MyViewHolder(View itemView) {
            super(itemView);
            friends_IMG_picture = itemView.findViewById(R.id.friends_IMG_picture);
            friends_LBL_userName = itemView.findViewById(R.id.friends_LBL_userName);
            friends_LBL_date = itemView.findViewById(R.id.friends_LBL_date);
            friends_BTN_compete = itemView.findViewById(R.id.friends_BTN_compete);
            friends_BTN_cancelRequest = itemView.findViewById(R.id.friends_BTN_cancelRequest);
            friends_BTN_acceptRequest = itemView.findViewById(R.id.friends_BTN_acceptRequest);
            friends_BTN_rejectRequest = itemView.findViewById(R.id.friends_BTN_rejectRequest);
            friends_BTN_sendRequest = itemView.findViewById(R.id.friends_BTN_sendRequest);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mClickListener != null) {
                        mClickListener.onItemClick(v, getAdapterPosition());
                    }
                }
            });

            friends_BTN_compete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mClickListener != null) {
                        mClickListener.onCompeteClick(getAdapterPosition());
                    }
                }
            });

            friends_BTN_cancelRequest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mClickListener != null) {
                        mClickListener.onCancelRequest(getAdapterPosition());
                    }
                }
            });

            friends_BTN_acceptRequest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mClickListener != null) {
                        mClickListener.onAcceptRequest(getAdapterPosition());
                    }
                }
            });

            friends_BTN_rejectRequest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mClickListener != null) {
                        mClickListener.onRejectRequest(getAdapterPosition());
                    }
                }
            });

            friends_BTN_sendRequest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mClickListener != null) {
                        mClickListener.onSendRequest(getAdapterPosition());
                    }
                }
            });
        }
    }
}