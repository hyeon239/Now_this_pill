package com.example.now_this_pill.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.now_this_pill.LoginActivity;
import com.example.now_this_pill.R;
import com.example.now_this_pill.Setting.InquiryFragment;
import com.example.now_this_pill.Setting.VersionFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import android.os.Handler;
import android.os.Looper;

public class SettingFragment extends Fragment {

    private TextView textEmail, textName;
    private int deleteButtonClickCount = 0;
    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable resetDeleteButtonClickCountRunnable = new Runnable() {

        public void run() {
            deleteButtonClickCount = 0;
        }
    };

    // Menu items
    private String[] menuItems = {"로그아웃", "문의하기", "버전정보", "계정삭제"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        // TextView 초기화
        textEmail = view.findViewById(R.id.text_email);
        textName = view.findViewById(R.id.text_name);

        // FirebaseAuth 인스턴스를 사용하여 현재 로그인한 사용자의 이메일을 가져옵니다.
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            String email = mAuth.getCurrentUser().getEmail();
            String userId = mAuth.getCurrentUser().getUid();

            // "이메일: " 텍스트와 사용자 이메일을 함께 설정합니다.
            textEmail.setText("이메일 : " + email);
            // 이름 동기화 중 메시지 표시
            textName.setText("이름 : 동기화 중...");
            // Firebase Realtime Database에서 사용자의 이름을 가져오고 설정합니다.
            loadUserName(userId);
        }

        // 설정 메뉴 생성
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) LinearLayout settingLayout = view.findViewById(R.id.setting_layout);
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        for (int i = 0; i < menuItems.length; i++) {
            View menuItemView = layoutInflater.inflate(R.layout.setting_menu_item, settingLayout, false);
            TextView menuItemTextView = menuItemView.findViewById(R.id.setting_menu_text);
            menuItemTextView.setText(menuItems[i]);
            final int position = i;
            menuItemTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleMenuItemClick(position);
                }
            });
            settingLayout.addView(menuItemView);
        }

        return view;
    }

    // Firebase Realtime Database에서 사용자의 이름을 가져오는 메서드
    private void loadUserName(String userId) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("FirebaseEmailAccount").child("userAccount");
        usersRef.child(userId).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.getValue(String.class);
                // 가져온 사용자 이름을 설정
                textName.setText("이름 : " + name);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // 데이터베이스에서 이름을 가져오는 데 실패한 경우 처리
            }
        });
    }

    // 가져온 사용자 이름을 표시하는 메서드
    private void displayUserName(String name) {
        textName.setText("이름: " + name);
    }

    // 메뉴 아이템 클릭 이벤트 처리
    private void handleMenuItemClick(int position) {
        switch (position) {
            case 0:
                // 로그아웃 처리
                FirebaseAuth.getInstance().signOut();
                // 로그인 화면으로 이동
                Intent intent = new Intent(getContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                getActivity().finish(); // 현재 액티비티 종료
                break;
            case 1:
                // 문의하기 처리
                replaceFragment(new InquiryFragment());
                break;
            case 2:
                // 버전 정보 화면으로 이동
                replaceFragment(new VersionFragment());
                break;
            case 3:
                // 계정삭제 처리
                handleDeleteAccount();
                break;
        }
    }

    // 프래그먼트를 교체하는 메서드
    private void replaceFragment(Fragment fragment) {
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    // 계정 삭제 버튼 클릭 핸들링
    private void handleDeleteAccount() {
        deleteButtonClickCount++;
        handler.removeCallbacks(resetDeleteButtonClickCountRunnable);
        handler.postDelayed(resetDeleteButtonClickCountRunnable, 5000); // 5초 후에 카운트 초기화

        if (deleteButtonClickCount < 5) {
            Toast.makeText(requireContext(), "정말 삭제하시겠습니까? 해당 버튼을 5회 누르시면 삭제됩니다 (" + deleteButtonClickCount + "/5)", Toast.LENGTH_SHORT).show();
        } else {
            deleteAccount();
        }
    }

    // 계정 삭제 처리
    private void deleteAccount() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            user.delete()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // 계정 삭제 성공
                            Toast.makeText(requireContext(), "계정이 성공적으로 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getContext(), LoginActivity.class);
                            startActivity(intent);

                            // 필요한 추가 작업 수행
                        } else {
                            // 계정 삭제 실패
                            Toast.makeText(requireContext(), "계정 삭제에 실패했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            // 사용자가 로그인되어 있지 않음을 사용자에게 알림
            Toast.makeText(requireContext(), "로그인이 필요한 기능입니다.", Toast.LENGTH_SHORT).show();
            // 로그인 화면으로 이동
            Intent loginIntent = new Intent(getContext(), LoginActivity.class);
            loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); // 기존의 스택 내용 제거 후 새로운 액티비티 시작
            startActivity(loginIntent);
            getActivity().finish(); // 현재 액티비티 종료
        }
    }
}