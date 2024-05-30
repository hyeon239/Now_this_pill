package com.example.now_this_pill.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;

import com.example.now_this_pill.PillScheduleAdapter;
import com.example.now_this_pill.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CalendarFragment extends Fragment {

    private TextView todayTextView;
    private TextView scheduleTextView;
    private CalendarView calendarView;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseRef;
    private static final String TAG = "CalendarFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        databaseRef = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        // TextView 찾기
        todayTextView = view.findViewById(R.id.today);
        scheduleTextView = view.findViewById(R.id.pill_eat);
        calendarView = view.findViewById(R.id.calendar_view);

        // 현재 날짜 가져오기
        Calendar currentCalendar = Calendar.getInstance();
        int year = currentCalendar.get(Calendar.YEAR);
        int month = currentCalendar.get(Calendar.MONTH);
        int dayOfMonth = currentCalendar.get(Calendar.DAY_OF_MONTH);

        // 현재 날짜의 요일 계산
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy년 MM월 dd일 (E요일)", Locale.getDefault());
        String currentDate = dateFormat.format(currentCalendar.getTime());

        // 현재 요일 가져오기 (영어)
        SimpleDateFormat dayFormat = new SimpleDateFormat("E", Locale.getDefault());
        String todayDayEnglish = dayFormat.format(currentCalendar.getTime());

// 영어 요일을 한글 요일로 변환
        String[] englishDays = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        String[] koreanDays = {"일", "월", "화", "수", "목", "금", "토"};

        for (int i = 0; i < englishDays.length; i++) {
            if (todayDayEnglish.equals(englishDays[i])) {
                String todayDay = koreanDays[i];
                currentDate = currentDate.replace(todayDayEnglish, todayDay);
                break;
            }
        }

        // 현재 날짜와 요일을 텍스트뷰에 표시
        todayTextView.setText(currentDate);

        // 초기 데이터 로드
        loadScheduleForDate(currentDate);

        // 캘린더뷰에서 날짜 선택 시 이벤트 처리
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int year, int month, int dayOfMonth) {
                // 선택된 날짜의 요일 계산
                Calendar selectedCalendar = Calendar.getInstance();
                selectedCalendar.set(year, month, dayOfMonth);
                String selectedDate = dateFormat.format(selectedCalendar.getTime());

                // 선택된 날짜와 요일을 텍스트뷰에 표시
                todayTextView.setText(selectedDate);

                // 선택된 날짜에 해당하는 스케줄 로드
                loadScheduleForDate(selectedDate);
            }
        });

        return view;
    }

    private void loadScheduleForDate(String date) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference scheduleRef = databaseRef.child("FirebaseEmailAccount").child("userAccount").child(userId).child("schedule").child(date);

            // 경로 확인을 위한 로그 추가
            Log.d(TAG, "Database path: " + scheduleRef.toString());

            scheduleRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    List<String> scheduleList = new ArrayList<>();
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String schedule = snapshot.getValue(String.class);
                            scheduleList.add(schedule);
                        }
                    }
                    displaySchedule(scheduleList);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // 오류 처리 로그 추가
                    Log.e(TAG, "Database error: " + databaseError.getMessage());
                    scheduleTextView.setText("데이터를 불러오는 중 오류가 발생했습니다.");
                }
            });
        } else {
            scheduleTextView.setText("로그인 상태를 확인해 주세요.");
        }
    }

    private void displaySchedule(List<String> scheduleList) {
        if (scheduleList.isEmpty()) {
            scheduleTextView.setText("복용약 기록이 없습니다.");
        } else {
            StringBuilder schedules = new StringBuilder();
            for (String schedule : scheduleList) {
                schedules.append(schedule).append("\n");
            }
            scheduleTextView.setText(schedules.toString());
        }
    }
}