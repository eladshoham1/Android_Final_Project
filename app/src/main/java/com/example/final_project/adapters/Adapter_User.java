package com.example.final_project.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.final_project.R;
import com.example.final_project.objects.User;
import com.example.final_project.utils.MySignal;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;

public class Adapter_User extends RecyclerView.Adapter<Adapter_User.MyViewHolder> {

    private ArrayList<User> allUsers;
    private ArrayList<String> friendsRequestsKeys;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    public Adapter_User(Context context, ArrayList<User> allUsers, ArrayList<String> friendsRequestsKeys) {
        if (context != null) {
            this.allUsers = allUsers;
            this.friendsRequestsKeys = friendsRequestsKeys;
            this.mInflater = LayoutInflater.from(context);
        }
    }

    // inflates the row layout from xml when needed
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_users, parent, false);
        return new MyViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        User user = allUsers.get(position);
        String pictureUrl = user.getPictureUrl();

        if (user.getSettings() != null && !user.getSettings().isPicture()) {
            MySignal.getInstance().loadPicture(null, holder.users_IMG_picture);
        } else {
            MySignal.getInstance().loadPicture(pictureUrl, holder.users_IMG_picture);
        }

        holder.users_LBL_userName.setText(user.getFirstName() + " " + user.getLastName());

        if (friendsRequestsKeys.contains(user.getUid())) {
            holder.users_BTN_sendRequest.setVisibility(View.GONE);
            holder.users_BTN_cancelRequest.setVisibility(View.VISIBLE);
        } else {
            holder.users_BTN_sendRequest.setVisibility(View.VISIBLE);
            holder.users_BTN_cancelRequest.setVisibility(View.GONE);
        }
    }

    public void filterList(ArrayList<User> filteredList) {
        allUsers = filteredList;
        notifyDataSetChanged();
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return allUsers.size();
    }

    // convenience method for getting data at click position
    public User getItem(int id) {
        return allUsers.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
        void onSendRequest(int position);
        void onCancelRequest(int position);
    }

    // stores and recycles views as they are scrolled off screen
    public class MyViewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView users_IMG_picture;
        TextView users_LBL_userName;
        MaterialButton users_BTN_sendRequest;
        MaterialButton users_BTN_cancelRequest;

        MyViewHolder(View itemView) {
            super(itemView);
            users_IMG_picture = itemView.findViewById(R.id.users_IMG_picture);
            users_LBL_userName = itemView.findViewById(R.id.users_LBL_userName);
            users_BTN_sendRequest = itemView.findViewById(R.id.users_BTN_sendRequest);
            users_BTN_cancelRequest = itemView.findViewById(R.id.users_BTN_cancelRequest);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mClickListener != null) {
                        mClickListener.onItemClick(v, getAdapterPosition());
                    }
                }
            });

            users_BTN_cancelRequest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mClickListener != null) {
                        mClickListener.onCancelRequest(getAdapterPosition());
                    }
                }
            });

            users_BTN_sendRequest.setOnClickListener(new View.OnClickListener() {
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