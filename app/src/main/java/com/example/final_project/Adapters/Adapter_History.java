/*package com.example.final_project.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.final_project.R;
import com.example.final_project.objects.History;
import com.example.final_project.objects.Run;

public class Adapter_History extends RecyclerView.Adapter<Adapter_History.MyViewHolder> {

    private History history;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    public Adapter_History(Context context, History history) {
        this.mInflater = LayoutInflater.from(context);
        this.history = history;
    }

    // inflates the row layout from xml when needed
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_record, parent, false);
        return new MyViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Run run = history.getAllRuns().get(position);
        //gliad for the map pic holder.history_IMG_runMap;
        holder.record_LBL_name.setText(record.getName());
        holder.record_LBL_date.setText(record.getDateByFormat());
        holder.record_LBL_score.setText("" + record.getScore());
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return history.getAllRuns().size();
    }

    // convenience method for getting data at click position
    public Run getItem(int id) {
        return history.getAllRuns().get(id);
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
        TextView record_LBL_rank;
        TextView record_LBL_name;
        TextView record_LBL_date;
        TextView record_LBL_score;

        MyViewHolder(View itemView) {
            super(itemView);
            record_LBL_rank = itemView.findViewById(R.id.record_LBL_rank);
            record_LBL_name = itemView.findViewById(R.id.record_LBL_name);
            record_LBL_date = itemView.findViewById(R.id.record_LBL_date);
            record_LBL_score = itemView.findViewById(R.id.record_LBL_score);

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
}*/
