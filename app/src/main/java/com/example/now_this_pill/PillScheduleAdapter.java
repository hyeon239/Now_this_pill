package com.example.now_this_pill;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PillScheduleAdapter extends RecyclerView.Adapter<PillScheduleAdapter.PillScheduleViewHolder> {

    private List<String> pillSchedules;
    private Handler handler = new Handler(Looper.getMainLooper());
    private SharedPreferences sharedPreferences;

    public PillScheduleAdapter(Context context, List<String> pillSchedules) {
        this.pillSchedules = pillSchedules;
        this.sharedPreferences = context.getSharedPreferences("PillSchedulePrefs", Context.MODE_PRIVATE);
    }

    @Override
    public PillScheduleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pill_schedule, parent, false);
        return new PillScheduleViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PillScheduleViewHolder holder, int position) {
        String pillSchedule = pillSchedules.get(position);
        holder.timeNameTextView.setText(pillSchedule);
        holder.actionButton.setText("외출하기"); // 필요에 따라 버튼 텍스트 변경

        // SharedPreferences에서 버튼의 가시성 상태 불러오기
        boolean isButtonVisible = sharedPreferences.getBoolean(pillSchedule, true);
        holder.actionButton.setVisibility(isButtonVisible ? View.VISIBLE : View.GONE);

        // 버튼 클릭 리스너 설정
        holder.actionButton.setOnClickListener(v -> {
            holder.actionButton.setVisibility(View.GONE);
            // 버튼의 가시성 상태를 SharedPreferences에 저장
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(pillSchedule, false);
            editor.apply();
        });

        // 현재 시간이 지난 일정인지 확인
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
            Date scheduleTime = sdf.parse(pillSchedule);
            if (scheduleTime != null && scheduleTime.before(new Date())) {
                holder.actionButton.setVisibility(View.GONE);
                // 버튼의 가시성 상태를 SharedPreferences에 저장
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(pillSchedule, false);
                editor.apply();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return pillSchedules.size();
    }

    public static class PillScheduleViewHolder extends RecyclerView.ViewHolder {
        public TextView timeNameTextView;
        public Button actionButton;

        public PillScheduleViewHolder(View itemView) {
            super(itemView);
            timeNameTextView = itemView.findViewById(R.id.time_name);
            actionButton = itemView.findViewById(R.id.button);
        }
    }
}
