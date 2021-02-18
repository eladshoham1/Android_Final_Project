package com.example.final_project.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.final_project.R;
import com.example.final_project.objects.Run;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Adapter_History extends RecyclerView.Adapter<Adapter_History.MyViewHolder> {

    private ArrayList<Run> allRuns;
    private Context context;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    public Adapter_History(Context context, ArrayList<Run> allRuns) {
        if (context != null) {
            this.context = context;
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

        holder.history_LBL_date.setText(new SimpleDateFormat("dd/MM/yyyy").format(new Date(run.getStartTime())));
        holder.history_LBL_duration.setText(getStringDuration(run.getDuration()));
        holder.history_LBL_distance.setText(new DecimalFormat("##.##").format(run.getDistance()) + " " + context.getResources().getString(R.string.kilometer));
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return allRuns.size();
    }

    public String getStringDuration(long duration) {
        long second, minutes, hours;
        StringBuilder strDuration = new StringBuilder();

        second = duration / 1000;
        minutes = second / 60;
        hours = minutes / 60;
        second %= 60;
        minutes %= 60;
        hours %= 24;

        strDuration.append(hours < 10 ? "0" : "").append(hours).append(":");
        strDuration.append(minutes < 10 ? "0" : "").append(minutes).append(":");
        strDuration.append(second < 10 ? "0" : "").append(second);

        return strDuration.toString();
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

        MyViewHolder(View itemView) {
            super(itemView);
            history_LBL_date = itemView.findViewById(R.id.history_LBL_date);
            history_LBL_duration = itemView.findViewById(R.id.history_LBL_duration);
            history_LBL_distance = itemView.findViewById(R.id.history_LBL_distance);

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
