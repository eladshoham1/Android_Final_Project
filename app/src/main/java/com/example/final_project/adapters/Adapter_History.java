package com.example.final_project.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.final_project.R;
import com.example.final_project.objects.Run;
import com.example.final_project.utils.MyStrings;

import java.util.ArrayList;

public class Adapter_History extends RecyclerView.Adapter<Adapter_History.MyViewHolder> {

    private ArrayList<Run> allRuns;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    public Adapter_History(Context context, ArrayList<Run> allRuns) {
        if (context != null) {
            this.mInflater = LayoutInflater.from(context);
            this.allRuns = allRuns;
        }
    }

    // inflates the row layout from xml when needed
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_running, parent, false);
        return new MyViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Run run = allRuns.get(position);
        String kmString = holder.context.getString(R.string.kilometer);

        holder.history_LBL_date.setText(MyStrings.makeDateString(run.getStartTime()));
        holder.history_LBL_duration.setText(MyStrings.makeDurationString(run.getDuration()));
        holder.history_LBL_distance.setText(MyStrings.twoDigitsAfterPoint(run.getDistance()) + " " + kmString);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return allRuns.size();
    }

    // convenience method for getting data at click position
    public Run getItem(int id) {
        return allRuns.get(id);
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
        TextView history_LBL_date;
        TextView history_LBL_duration;
        TextView history_LBL_distance;
        Context context;

        MyViewHolder(View itemView) {
            super(itemView);
            history_LBL_date = itemView.findViewById(R.id.history_LBL_date);
            history_LBL_duration = itemView.findViewById(R.id.history_LBL_duration);
            history_LBL_distance = itemView.findViewById(R.id.history_LBL_distance);
            context = itemView.getContext();

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
