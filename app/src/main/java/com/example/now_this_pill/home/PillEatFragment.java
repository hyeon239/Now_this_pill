package com.example.now_this_pill.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.now_this_pill.MainActivity;
import com.example.now_this_pill.R;
import com.example.now_this_pill.UserAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class PillEatFragment extends Fragment {


    private long backPressedTime; // 뒤로 가기 버튼이 눌린 시간을 저장하기 위한 변수
    private DatabaseReference databaseRef; // Firebase 실시간 데이터베이스 참조
    private FirebaseAuth mAuth; // Firebase 인증 객체

    private TextView textInto1;
    private TextView textInto2;
    private TextView textInto3;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pill_eat, container, false);

        // Firebase 인증 객체 초기화
        mAuth = FirebaseAuth.getInstance();
        // Firebase 실시간 데이터베이스 참조 초기화
        databaseRef = FirebaseDatabase.getInstance().getReference();

        // 클릭 이벤트 처리
        LinearLayout linearLayout = view.findViewById(R.id.linearLayout);
        LinearLayout linearLayout2 = view.findViewById(R.id.linearLayout_2);
        LinearLayout linearLayout3 = view.findViewById(R.id.linearLayout_3);

        // TextView 초기화
        textInto1 = view.findViewById(R.id.text_info);
        textInto2 = view.findViewById(R.id.text_info_2);
        textInto3 = view.findViewById(R.id.text_info_3);

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Fragment로 이동
                moveToFragment(new pill_periodFragment(), "1번통");
            }
        });

        linearLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Fragment로 이동
                moveToFragment(new pill_period_2_Fragment(), "2번통");
            }
        });

        linearLayout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Fragment로 이동
                moveToFragment(new pill_packet_periodFragment(), "봉지약");
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // 화면이 다시 보일 때 상단 메뉴바의 이름을 원래대로 변경
        restoreToolbarTitle();
        // 하단 메뉴바를 보이게 함
        showBottomNavigationBar();
        // Firebase 데이터 읽기
        loadPillData();
    }
    private void loadPillData() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            // 1번통 데이터 로드
            databaseRef.child("FirebaseEmailAccount").child("userAccount").child(userId).child("pill_schedule_1")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                String pillName = dataSnapshot.child("pillName").getValue(String.class);
                                if (pillName != null && !pillName.isEmpty()) {
                                    textInto1.setText(pillName);
                                } else {
                                    textInto1.setText("등록된 정보가 없습니다");
                                }
                            } else {
                                textInto1.setText("등록된 정보가 없습니다");
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(requireContext(), "데이터를 불러오는 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                        }
                    });

            // 2번통 데이터 로드
            databaseRef.child("FirebaseEmailAccount").child("userAccount").child(userId).child("pill_schedule_2")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                String pillName = dataSnapshot.child("pillName").getValue(String.class);
                                if (pillName != null && !pillName.isEmpty()) {
                                    textInto2.setText(pillName);
                                } else {
                                    textInto2.setText("등록된 정보가 없습니다");
                                }
                            } else {
                                textInto2.setText("등록된 정보가 없습니다");
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(requireContext(), "데이터를 불러오는 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                        }
                    });

            // 봉지약 데이터 로드
            databaseRef.child("FirebaseEmailAccount").child("userAccount").child(userId).child("pill_schedule_3")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                String pillName = dataSnapshot.child("pillName").getValue(String.class);
                                if (pillName != null && !pillName.isEmpty()) {
                                    textInto3.setText(pillName);
                                } else {
                                    textInto3.setText("등록된 정보가 없습니다");
                                }
                            } else {
                                textInto3.setText("등록된 정보가 없습니다");
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(requireContext(), "데이터를 불러오는 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            textInto1.setText("등록된 정보가 없습니다");
            textInto2.setText("등록된 정보가 없습니다");
            textInto3.setText("등록된 정보가 없습니다");
        }
    }

    @Override
    public void onViewCreated( View view,  Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    // 현재 시간
                    long currentTime = System.currentTimeMillis();
                    // 첫 번째로 뒤로 가기 버튼을 눌렀을 때
                    if (backPressedTime + 2000 > currentTime) {
                        // 앱 종료
                        requireActivity().finish();
                    } else {
                        // 두 번째로 뒤로 가기 버튼을 눌렀을 때
                        // 사용자에게 앱 종료 안내 메시지 표시
                        Toast.makeText(requireContext(), "한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
                    }
                    backPressedTime = currentTime;
                    return true;
                }
                return false;
            }
        });
    }

    // Fragment로 이동하는 메서드
    private void moveToFragment(Fragment fragment, String title) {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();

        // 액티비티의 메서드 호출하여 상단 메뉴바의 이름 변경
        ((MainActivity) requireActivity()).setToolbarTitle(title);

        // 하단 메뉴바를 숨기는 코드 추가
        hideBottomNavigationBar();
    }

    // 상단 메뉴바의 이름을 원래대로 복원하는 메서드
    private void restoreToolbarTitle() {
        // MainActivity에서 상단 툴바를 가져오고 이름을 변경
        MainActivity mainActivity = (MainActivity) requireActivity();
        mainActivity.setToolbarTitle("지금이약!");
    }

    // 하단 메뉴바를 숨기는 메서드
    private void hideBottomNavigationBar() {
        // MainActivity에서 하단 네비게이션 뷰를 가져오는 코드
        MainActivity mainActivity = (MainActivity) requireActivity();
        mainActivity.hideBottomNavigationBar();
    }

    // 하단 메뉴바를 보이는 메서드
    private void showBottomNavigationBar() {
        // MainActivity에서 하단 네비게이션 뷰를 가져오는 코드
        MainActivity mainActivity = (MainActivity) requireActivity();
        mainActivity.showBottomNavigationBar();
    }


}