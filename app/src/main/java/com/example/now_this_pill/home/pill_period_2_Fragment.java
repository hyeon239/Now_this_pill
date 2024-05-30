package com.example.now_this_pill.home;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.collection.CircularArray;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.now_this_pill.R;
import com.example.now_this_pill.UserAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class pill_period_2_Fragment extends Fragment {

    private Spinner pillQuantitySpinner;
    private Spinner pillFrequencySpinner;
    private LinearLayout layout1, layout2, layout3, layout4, layout5;
    private Button[] weekdayButtons;
    private Button btn_store_2; // 추가된 버튼
    private EditText editText_1; // 약 이름 입력을 위한 EditText
    private TimePicker timePicker; // 시간 선택을 위한 TimePicker 추가

    private TimePicker[] timePickers; // 시간 선택을 위한 TimePicker 배열
    private List<Integer> hours;
    private List<Integer> minutes;
    private EditText memoEditText; // 추가된 부분


    // Firebase 인증 객체
    private FirebaseAuth mAuth;
    // Firebase 실시간 데이터베이스 참조
    private DatabaseReference databaseRef;
    private CircularArray<Object> times;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pill_period, container, false);

        // Firebase 인증 객체 초기화
        mAuth = FirebaseAuth.getInstance();
        // Firebase 실시간 데이터베이스 참조 초기화
        databaseRef = FirebaseDatabase.getInstance().getReference();

        memoEditText = view.findViewById(R.id.memo_border);

        // Initialize views
        pillQuantitySpinner = view.findViewById(R.id.pillQuantitySpinner);
        pillFrequencySpinner = view.findViewById(R.id.pillFrequencySpinner);
        layout1 = view.findViewById(R.id.layout1);
        layout2 = view.findViewById(R.id.layout2);
        layout3 = view.findViewById(R.id.layout3);
        layout4 = view.findViewById(R.id.layout4);
        layout5 = view.findViewById(R.id.layout5);
        btn_store_2 = view.findViewById(R.id.btn_store_2); // 추가된 버튼 초기화
        editText_1 = view.findViewById(R.id.editText_1); // editText_1 초기화
        // TimePicker 배열 초기화
        timePickers = new TimePicker[]{
                view.findViewById(R.id.timePicker),
                view.findViewById(R.id.timePicker_2),
                view.findViewById(R.id.timePicker_3),
                view.findViewById(R.id.timePicker_4),
                view.findViewById(R.id.timePicker_5)
        };

        // 시간 저장 리스트 초기화
        hours = new ArrayList<>();
        minutes = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            hours.add(0);
            minutes.add(0);
        }
        // Set up spinners
        ArrayAdapter<CharSequence> quantityAdapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.pill_quantities, android.R.layout.simple_spinner_item);
        quantityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pillQuantitySpinner.setAdapter(quantityAdapter);

        ArrayAdapter<CharSequence> frequencyAdapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.pill_frequencies, android.R.layout.simple_spinner_item);
        frequencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pillFrequencySpinner.setAdapter(frequencyAdapter);

        // Set up spinner item selection listeners
        pillQuantitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        pillFrequencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateLayoutVisibility(position);
                checkFields(); // 스피너 선택 시 필드 확인
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        // Set up weekday buttons
        setupWeekdayButtons(view);

        // Set up button click listener
        btn_store_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if at least one of the required fields is filled
                if (editText_1.getText().toString().isEmpty()) {
                    // Display a message indicating missing fields
                    Toast.makeText(requireContext(), "약 이름을 입력해주세요", Toast.LENGTH_SHORT).show();
                    return; // 약 이름이 입력되지 않았으면 더 이상 진행하지 않음
                }
                if (!isAnyWeekdaySelected()) {
                    Toast.makeText(requireContext(), "요일을 선택해주세요", Toast.LENGTH_SHORT).show();
                    return; // 요일이 선택되지 않았으면 더 이상 진행하지 않음
                }
                // Save pill schedule
                savePillSchedule();
                // Navigate to the next screen
                PillEatFragment fragment = new PillEatFragment();
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.main_container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
// Show a message indicating that the pill schedule has been set
                Toast.makeText(requireContext(), "복용 주기가 설정되었습니다", Toast.LENGTH_SHORT).show();
            }
        });

        //저장된 DB 불러오는 함수
        loadPillName();
        loadPillSchedule();
        loadPillQuantity();
        loadPillFrequency();
        loadMemo();
        return view;
    }

    private void savePillSchedule() {
        // Retrieve pill schedule data
        String pillName = editText_1.getText().toString();
        int pillCount = Integer.parseInt(pillQuantitySpinner.getSelectedItem().toString());
        int pillFrequency = pillFrequencySpinner.getSelectedItemPosition() + 1;
        String memo = ((EditText) getView().findViewById(R.id.memo_border)).getText().toString();

        UserAccount.PillSchedule2 pillSchedule2 = new UserAccount.PillSchedule2(
                pillName, "", pillCount, pillFrequency,
                getTimeFromTimePicker(timePickers[0]), getTimeFromTimePicker(timePickers[1]), getTimeFromTimePicker(timePickers[2]), getTimeFromTimePicker(timePickers[3]), getTimeFromTimePicker(timePickers[4]),
                memo
        );

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference userRef = databaseRef.child("FirebaseEmailAccount").child("userAccount").child(userId).child("pill_schedule_2");

            userRef.child("times").removeValue();

            for (int i = 0; i < pillFrequency; i++) {
                String time = getTimeFromTimePicker(timePickers[i]);
                userRef.child("times").child("time" + (i + 1)).setValue(time);
            }

            userRef.child("pillName").setValue(pillName);
            userRef.child("pillCount").setValue(pillCount);
            userRef.child("pillFrequency").setValue(pillFrequency);
            userRef.child("memo").setValue(memo);
            String pillDay = getSelectedWeekdays();
            userRef.child("weekdays").setValue(pillDay);
        } else {
            Toast.makeText(requireContext(), "로그인된 사용자를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
        }
    }




    // Get selected weekdays
    private String getSelectedWeekdays() {
        List<String> selectedWeekdays = new ArrayList<>();
        for (Button button : weekdayButtons) {
            if (button.isSelected()) {
                selectedWeekdays.add(button.getText().toString());
            }
        }
        return TextUtils.join(",", selectedWeekdays); // Join selected weekdays with comma separator
    }

    // Check if any weekday button is selected
    private boolean isAnyWeekdaySelected() {
        for (Button button : weekdayButtons) {
            if (button.isSelected()) {
                return true;
            }
        }
        return false;
    }

    private void updateLayoutVisibility(int position) {
        // Hide all layouts
        layout1.setVisibility(View.GONE);
        layout2.setVisibility(View.GONE);
        layout3.setVisibility(View.GONE);
        layout4.setVisibility(View.GONE);
        layout5.setVisibility(View.GONE);

        // Hide all TimePickers
        for (TimePicker timePicker : timePickers) {
            timePicker.setVisibility(View.GONE);
        }

        // Show the layout based on the selected position
        switch (position) {
            case 0:
                layout1.setVisibility(View.VISIBLE);
                timePickers[0].setVisibility(View.VISIBLE);
                break;
            case 1:
                layout1.setVisibility(View.VISIBLE);
                layout2.setVisibility(View.VISIBLE);
                timePickers[0].setVisibility(View.VISIBLE);
                timePickers[1].setVisibility(View.VISIBLE);
                break;
            case 2:
                layout1.setVisibility(View.VISIBLE);
                layout2.setVisibility(View.VISIBLE);
                layout3.setVisibility(View.VISIBLE);
                timePickers[0].setVisibility(View.VISIBLE);
                timePickers[1].setVisibility(View.VISIBLE);
                timePickers[2].setVisibility(View.VISIBLE);
                break;
            case 3:
                layout1.setVisibility(View.VISIBLE);
                layout2.setVisibility(View.VISIBLE);
                layout3.setVisibility(View.VISIBLE);
                layout4.setVisibility(View.VISIBLE);
                timePickers[0].setVisibility(View.VISIBLE);
                timePickers[1].setVisibility(View.VISIBLE);
                timePickers[2].setVisibility(View.VISIBLE);
                timePickers[3].setVisibility(View.VISIBLE);
                break;
            case 4:
                layout1.setVisibility(View.VISIBLE);
                layout2.setVisibility(View.VISIBLE);
                layout3.setVisibility(View.VISIBLE);
                layout4.setVisibility(View.VISIBLE);
                layout5.setVisibility(View.VISIBLE);
                timePickers[0].setVisibility(View.VISIBLE);
                timePickers[1].setVisibility(View.VISIBLE);
                timePickers[2].setVisibility(View.VISIBLE);
                timePickers[3].setVisibility(View.VISIBLE);
                timePickers[4].setVisibility(View.VISIBLE);
                break;
        }
    }

    private void setupWeekdayButtons(View view) {
        weekdayButtons = new Button[]{
                view.findViewById(R.id.sundayButton),
                view.findViewById(R.id.mondayButton),
                view.findViewById(R.id.tuesdayButton),
                view.findViewById(R.id.wednesdayButton),
                view.findViewById(R.id.thursdayButton),
                view.findViewById(R.id.fridayButton),
                view.findViewById(R.id.saturdayButton)
        };

        for (Button button : weekdayButtons) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toggleButtonSelection(button);
                }
            });
        }
    }

    private void toggleButtonSelection(Button button) {
        if (button.isSelected()) {
            button.setSelected(false);
            button.setBackgroundResource(R.drawable.btn_circle);
            button.setTextColor(getResources().getColor(R.color.main_color)); // 선택되지 않은 경우의 텍스트 색상
        } else {
            button.setSelected(true);
            button.setBackgroundResource(R.drawable.btn_circle_2);
            button.setTextColor(getResources().getColor(android.R.color.white)); // 선택된 경우의 텍스트 색상
        }
    }

    private boolean checkFields() {
        // Check if all required fields are filled
        boolean fieldsFilled = true;
        if (pillFrequencySpinner.getSelectedItemPosition() == 0) {
            fieldsFilled = false;
        }
        // Add checks for other fields if needed
        return fieldsFilled;
    }

    private String getTimeFromTimePicker(TimePicker timePicker) {
        int hour;
        int minute;
        if (Build.VERSION.SDK_INT >= 23) {
            hour = timePicker.getHour();
            minute = timePicker.getMinute();
        } else {
            hour = timePicker.getCurrentHour();
            minute = timePicker.getCurrentMinute();
        }

        boolean isKorean = getResources().getConfiguration().locale.getLanguage().equals("ko");
        String timeFormat;
        if (hour < 12) {
            timeFormat = isKorean ? "오전" : "AM";
        } else {
            timeFormat = isKorean ? "오후" : "PM";
            if (hour > 12) {
                hour -= 12;
            }
        }

        if (hour == 0) {
            hour = 12;
        }

        String timeString;
        if (isKorean) {
            timeString = String.format(Locale.getDefault(), "%s %02d:%02d", timeFormat, hour, minute);
        } else {
            timeString = String.format(Locale.getDefault(), "%02d:%02d %s", hour, minute, timeFormat);
        }

        return timeString;
    }


    //알약 이름 DB 불러오기
    private void loadPillName() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            databaseRef.child("FirebaseEmailAccount").child("userAccount").child(userId).child("pill_schedule_2")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                String pillName = dataSnapshot.child("pillName").getValue(String.class);
                                if (pillName != null && !pillName.isEmpty()) {
                                    editText_1.setText(pillName);
                                    btn_store_2.setText("수정하기"); // 약 이름이 있으면 버튼 텍스트를 수정하기로 변경
                                } else {
                                    editText_1.setHint("약 이름을 입력하세요");
                                }
                            } else {
                                editText_1.setHint("약 이름을 입력하세요");
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(requireContext(), "데이터를 불러오는 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(requireContext(), "로그인된 사용자를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
        }
    }
//복용 요일 DB 불러오기
// loadPillSchedule() 함수 내부에서 loadPillScheduleTimes() 함수 호출 시 데이터 스냅샷 전달
private void loadPillSchedule() {
    FirebaseUser currentUser = mAuth.getCurrentUser();
    if (currentUser != null) {
        String userId = currentUser.getUid();
        databaseRef.child("FirebaseEmailAccount").child("userAccount").child(userId).child("pill_schedule_2")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // Load pill name
                            String pillName = dataSnapshot.child("pillName").getValue(String.class);
                            if (pillName != null && !pillName.isEmpty()) {
                                editText_1.setText(pillName);
                                btn_store_2.setText("수정하기");
                            } else {
                                editText_1.setHint("약 이름을 입력하세요");
                            }

                            // Load pill days
                            String weekdays = dataSnapshot.child("weekdays").getValue(String.class);
                            if (weekdays != null && !weekdays.isEmpty()) {
                                setSelectedWeekdays(weekdays);
                            }

                            // Load pill frequency
                            int pillFrequency = dataSnapshot.child("pillFrequency").getValue(Integer.class);
                            if (pillFrequency != 0) {
                                // Find the position of pill frequency in the array
                                int position = pillFrequency - 1; // Adjusted position
                                pillFrequencySpinner.setSelection(position);
                                updateLayoutVisibility(position); // Update layout visibility based on pill frequency

                                // Call loadPillScheduleTimes() and pass the DataSnapshot and pillFrequency
                                loadPillScheduleTimes(dataSnapshot, pillFrequency);
                            }
                        } else {
                            editText_1.setHint("약 이름을 입력하세요");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(requireContext(), "데이터를 불러오는 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
    } else {
        Toast.makeText(requireContext(), "로그인된 사용자를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
    }
}


    private void setSelectedWeekdays(String weekdays) {
        String[] days = weekdays.split(",");
        for (String day : days) {
            for (Button button : weekdayButtons) {
                if (button.getText().toString().equals(day)) {
                    button.setSelected(true);
                    button.setBackgroundResource(R.drawable.btn_circle_2);
                    button.setTextColor(getResources().getColor(android.R.color.white)); // 선택된 경우의 텍스트 색상
                }
            }
        }
    }


    private void loadPillQuantity() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            databaseRef.child("FirebaseEmailAccount").child("userAccount").child(userId).child("pill_schedule_2")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                // Load pill quantity
                                int pillQuantity = dataSnapshot.child("pillCount").getValue(Integer.class);
                                if (pillQuantity != 0) {
                                    // Find the position of pill quantity in the array
                                    int position = Arrays.asList(getResources().getStringArray(R.array.pill_quantities)).indexOf(String.valueOf(pillQuantity));
                                    pillQuantitySpinner.setSelection(position);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(requireContext(), "데이터를 불러오는 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(requireContext(), "로그인된 사용자를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    //하루에 몇 번 복용하는지 DB 불러오기
    private void loadPillFrequency() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            databaseRef.child("FirebaseEmailAccount").child("userAccount").child(userId).child("pill_schedule_2")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                // Load pill frequency
                                int pillFrequency = dataSnapshot.child("pillFrequency").getValue(Integer.class);
                                if (pillFrequency != 0) {
                                    // Find the position of pill frequency in the array
                                    int position = pillFrequency - 1; // Adjusted position
                                    pillFrequencySpinner.setSelection(position);
                                    updateLayoutVisibility(position); // Update layout visibility based on pill frequency
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(requireContext(), "데이터를 불러오는 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(requireContext(), "로그인된 사용자를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    //시간, 분 가져오기


    // Load pill schedule times
    // Load pill schedule times
    // Load pill schedule times
    private void loadPillScheduleTimes(DataSnapshot dataSnapshot, int pillFrequency) {
        if (dataSnapshot == null) {
            return;
        }

        boolean isKorean = getResources().getConfiguration().locale.getLanguage().equals("ko");

        for (int i = 0; i < pillFrequency; i++) {
            String timeString = dataSnapshot.child("times").child("time" + (i + 1)).getValue(String.class);
            if (timeString != null) {
                String[] parts = timeString.split("[: ]");
                if (parts.length == 3) {
                    int hour = Integer.parseInt(parts[1]);
                    int minute = Integer.parseInt(parts[2]);

                    if (parts[0].equals("오후") || parts[0].equals("PM")) {
                        if (hour != 12) {
                            hour += 12;
                        }
                    } else if (parts[0].equals("오전") && hour == 12) {
                        hour = 0;
                    }

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        timePickers[i].setHour(hour);
                        timePickers[i].setMinute(minute);
                    } else {
                        timePickers[i].setCurrentHour(hour);
                        timePickers[i].setCurrentMinute(minute);
                    }
                }
            }
        }
    }




    //메모 dB
// 메모 DB 불러오기
private void loadMemo() {
    FirebaseUser currentUser = mAuth.getCurrentUser();
    if (currentUser != null) {
        String userId = currentUser.getUid();
        databaseRef.child("FirebaseEmailAccount").child("userAccount").child(userId).child("pill_schedule_2")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            String memo = dataSnapshot.child("memo").getValue(String.class);
                            if (memo != null && !memo.isEmpty()) {
                                // 데이터베이스에서 가져온 메모를 EditText에 설정
                                memoEditText.setText(memo);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(requireContext(), "데이터를 불러오는 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
    } else {
        Toast.makeText(requireContext(), "로그인된 사용자를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
    }
}


}