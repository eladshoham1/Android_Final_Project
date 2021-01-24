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

    private ArrayList<User> friends;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    public Adapter_Friend(Context context, ArrayList<User> friends) {
        this.mInflater = LayoutInflater.from(context);
        this.friends = friends;
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
        //holder.friends_IMG_picture = friend.getPicture();
        holder.friends_LBL_userName.setText(friend.getFirstName() + " " + friend.getLastName());
        holder.friends_LBL_age.setText("" + friend.getAge());
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
    }

    // stores and recycles views as they are scrolled off screen
    public class MyViewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView friends_IMG_picture;
        TextView friends_LBL_userName;
        TextView friends_LBL_age;
        MaterialButton post_BTN_sendRequest;

        MyViewHolder(View itemView) {
            super(itemView);
            friends_IMG_picture = itemView.findViewById(R.id.friends_IMG_picture);
            friends_LBL_userName = itemView.findViewById(R.id.friends_LBL_userName);
            friends_LBL_age = itemView.findViewById(R.id.friends_LBL_age);
            post_BTN_sendRequest = itemView.findViewById(R.id.post_BTN_sendRequest);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mClickListener != null) {
                        mClickListener.onItemClick(v, getAdapterPosition());
                    }
                }
            });
        }
    }
}