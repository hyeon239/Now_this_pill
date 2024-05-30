package com.example.now_this_pill.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import com.example.now_this_pill.PillScheduleAdapter;
import com.example.now_this_pill.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ScheduleFragment extends Fragment {

    private TextView todayTextView;
    private RecyclerView pillScheduleRecyclerView;
    private PillScheduleAdapter adapter;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseRef;
    private TextView noPillsTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);

        todayTextView = view.findViewById(R.id.today);
        noPillsTextView = view.findViewById(R.id.noPillsTextView);
        pillScheduleRecyclerView = view.findViewById(R.id.pill_schedule_recycler_view);
        pillScheduleRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mAuth = FirebaseAuth.getInstance();
        databaseRef = FirebaseDatabase.getInstance().getReference();

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy년 MM월 dd일 (E요일)", Locale.getDefault());
        String todayDate = dateFormat.format(calendar.getTime());
        SimpleDateFormat dayFormat = new SimpleDateFormat("E", Locale.getDefault());
        String todayDay = dayFormat.format(calendar.getTime());

        String[] englishDays = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        String[] koreanDays = {"일", "월", "화", "수", "목", "금", "토"};
        for (int i = 0; i < englishDays.length; i++) {
            if (todayDay.equals(englishDays[i])) {
                todayDay = koreanDays[i];
                break;
            }
        }

        todayTextView.setText(todayDate);

        loadPillSchedules(todayDay, todayDate);

        return view;
    }

    private void loadPillSchedules(String todayDay, String todayDate) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            List<String> schedules = Arrays.asList("pill_schedule_1", "pill_schedule_2", "pill_schedule_3");

            List<String> timesAndNamesList = new ArrayList<>();
            final int[] completedCount = {0}; // 완료된 콜백 수를 추적

            for (String schedule : schedules) {
                databaseRef.child("FirebaseEmailAccount").child("userAccount").child(userId).child(schedule)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    String weekdays = dataSnapshot.child("weekdays").getValue(String.class);
                                    if (weekdays != null && Arrays.asList(weekdays.split(",")).contains(todayDay)) {
                                        int pillFrequency = dataSnapshot.child("pillFrequency").getValue(Integer.class);
                                        for (int i = 0; i < pillFrequency; i++) {
                                            String time = dataSnapshot.child("times").child("time" + (i + 1)).getValue(String.class);
                                            String name = dataSnapshot.child("pillName").getValue(String.class);
                                            if (time != null && name != null) {
                                                timesAndNamesList.add(time + " " + name);
                                            }
                                        }
                                    }
                                }

                                completedCount[0]++;
                                if (completedCount[0] == schedules.size()) {
                                    // 모든 스케줄을 다 조회한 후 시간과 알약 이름을 정렬하여 표시
                                    Collections.sort(timesAndNamesList, new Comparator<String>() {
                                        SimpleDateFormat timeFormat = new SimpleDateFormat("a hh:mm", Locale.getDefault());

                                        @Override
                                        public int compare(String t1, String t2) {
                                            try {
                                                return timeFormat.parse(t1.split(" ")[0] + " " + t1.split(" ")[1]).compareTo(timeFormat.parse(t2.split(" ")[0] + " " + t2.split(" ")[1]));
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }
                                            return 0;
                                        }
                                    });

                                    if (!timesAndNamesList.isEmpty()) {
                                        adapter = new PillScheduleAdapter(timesAndNamesList);
                                        pillScheduleRecyclerView.setAdapter(adapter);
                                        saveScheduleToFirebase(timesAndNamesList, todayDate);
                                        noPillsTextView.setVisibility(View.GONE);
                                    } else {
                                        noPillsTextView.setVisibility(View.VISIBLE);
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Toast.makeText(requireContext(), "데이터를 불러오는 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        } else {
            Toast.makeText(requireContext(), "로그인된 사용자를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveScheduleToFirebase(List<String> scheduleList, String todayDate) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference scheduleRef = databaseRef.child("FirebaseEmailAccount").child("userAccount").child(userId).child("schedule").child(todayDate);

            for (int i = 0; i < scheduleList.size(); i++) {
                scheduleRef.child("스케줄" + (i + 1)).setValue(scheduleList.get(i));
            }
        }
    }
}