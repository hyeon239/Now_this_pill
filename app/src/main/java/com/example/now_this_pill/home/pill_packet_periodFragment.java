package com.example.now_this_pill.home;

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
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.now_this_pill.R;
import com.example.now_this_pill.UserAccount;

import java.util.ArrayList;
import java.util.List;

public class pill_packet_periodFragment extends Fragment {

    private Spinner pillFrequencySpinner;
    private LinearLayout layout1, layout2, layout3, layout4, layout5;
    private Button[] weekdayButtons;
    private Button btn_store_2; // 추가된 버튼

    // UserAccount 인스턴스
    private UserAccount userAccount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pill_packet_period, container, false);

        // Initialize views
        pillFrequencySpinner = view.findViewById(R.id.pillFrequencySpinner);
        layout1 = view.findViewById(R.id.layout1);
        layout2 = view.findViewById(R.id.layout2);
        layout3 = view.findViewById(R.id.layout3);
        layout4 = view.findViewById(R.id.layout4);
        layout5 = view.findViewById(R.id.layout5);
        btn_store_2 = view.findViewById(R.id.btn_store_2); // 추가된 버튼 초기화

        // Set up spinners
        ArrayAdapter<CharSequence> frequencyAdapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.pill_frequencies, android.R.layout.simple_spinner_item);
        frequencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pillFrequencySpinner.setAdapter(frequencyAdapter);

        // Set up spinner item selection listeners
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
                EditText editText_1 = view.findViewById(R.id.editText_1); // editText_1 초기화
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

        // Show the layout based on the selected position
        switch (position) {
            case 0:
                layout1.setVisibility(View.VISIBLE);
                break;
            case 1:
                layout1.setVisibility(View.VISIBLE);
                layout2.setVisibility(View.VISIBLE);
                break;
            case 2:
                layout1.setVisibility(View.VISIBLE);
                layout2.setVisibility(View.VISIBLE);
                layout3.setVisibility(View.VISIBLE);
                break;
            case 3:
                layout1.setVisibility(View.VISIBLE);
                layout2.setVisibility(View.VISIBLE);
                layout3.setVisibility(View.VISIBLE);
                layout4.setVisibility(View.VISIBLE);
                break;
            case 4:
                layout1.setVisibility(View.VISIBLE);
                layout2.setVisibility(View.VISIBLE);
                layout3.setVisibility(View.VISIBLE);
                layout4.setVisibility(View.VISIBLE);
                layout5.setVisibility(View.VISIBLE);
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
}