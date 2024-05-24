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


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.now_this_pill.PillSchedule;
import com.example.now_this_pill.R;
import com.example.now_this_pill.UserAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
public class pill_periodFragment extends Fragment {

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


    // Firebase 인증 객체
    private FirebaseAuth mAuth;
    // Firebase 실시간 데이터베이스 참조
    private DatabaseReference databaseRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pill_period, container, false);

        // Firebase 인증 객체 초기화
        mAuth = FirebaseAuth.getInstance();
        // Firebase 실시간 데이터베이스 참조 초기화
        databaseRef = FirebaseDatabase.getInstance().getReference();

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
            }
        });

        return view;
    }

    private void savePillSchedule() {
        // Retrieve pill schedule data
        String pillName = editText_1.getText().toString(); // 사용자가 입력한 약 이름
        String pillDay = getSelectedWeekdays();
        int pillCount = Integer.parseInt(pillQuantitySpinner.getSelectedItem().toString());
        int pillFrequency = pillFrequencySpinner.getSelectedItemPosition() + 1; // Adjusted frequency value
        String memo = ((EditText) getView().findViewById(R.id.memo_border)).getText().toString(); // 메모 입력

        // Retrieve selected times from TimePickers
        for (int i = 0; i < pillFrequency; i++) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                hours.set(i, timePickers[i].getHour());
                minutes.set(i, timePickers[i].getMinute());
            } else {
                hours.set(i, timePickers[i].getCurrentHour());
                minutes.set(i, timePickers[i].getCurrentMinute());
            }
        }

        // Create a new PillSchedule object
        UserAccount.PillSchedule1 pillSchedule1 = new UserAccount.PillSchedule1(
                pillName, pillDay, pillCount, pillFrequency,
                hours.size() > 0 ? hours.get(0) : 0, minutes.size() > 0 ? minutes.get(0) : 0,
                hours.size() > 1 ? hours.get(1) : 0, minutes.size() > 1 ? minutes.get(1) : 0,
                hours.size() > 2 ? hours.get(2) : 0, minutes.size() > 2 ? minutes.get(2) : 0,
                hours.size() > 3 ? hours.get(3) : 0, minutes.size() > 3 ? minutes.get(3) : 0,
                hours.size() > 4 ? hours.get(4) : 0, minutes.size() > 4 ? minutes.get(4) : 0,
                memo // 요일 정보 추가
        );

        // Write the new PillSchedule object to the database
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            databaseRef.child("FirebaseEmailAccount").child("userAccount").child(userId).child("pill_schedule_1").setValue(pillSchedule1);
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
        String timeFormat;
        if (hour < 12) {
            timeFormat = "AM";
        } else {
            timeFormat = "PM";
            hour -= 12; // 오후 시간을 12시간 형식으로 변환
        }
        return String.format("%02d:%02d %s", hour, minute, timeFormat);
    }

}