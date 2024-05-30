package com.example.now_this_pill;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PillScheduleAdapter extends RecyclerView.Adapter<PillScheduleAdapter.PillScheduleViewHolder> {

    private List<String> pillSchedules;

    public PillScheduleAdapter(List<String> pillSchedules) {
        this.pillSchedules = pillSchedules;
    }


    @Override
    public PillScheduleViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pill_schedule, parent, false);
        return new PillScheduleViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder( PillScheduleViewHolder holder, int position) {
        String pillSchedule = pillSchedules.get(position);
        holder.timeNameTextView.setText(pillSchedule);
        holder.actionButton.setText("외출하기"); // 필요에 따라 버튼 텍스트 변경
    }

    @Override
    public int getItemCount() {
        return pillSchedules.size();
    }

    public static class PillScheduleViewHolder extends RecyclerView.ViewHolder {
        public TextView timeNameTextView;
        public Button actionButton;

        public PillScheduleViewHolder( View itemView) {
            super(itemView);
            timeNameTextView = itemView.findViewById(R.id.time_name);
            actionButton = itemView.findViewById(R.id.button);
        }
    }
}