package com.example.now_this_pill.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.now_this_pill.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SettingFragment extends Fragment {

    private TextView textEmail, textName;
    private static final String PREF_NAME = "user_pref"; // 캐싱을 위한 SharedPreferences 이름
    private static final String KEY_USER_NAME = "user_name"; // 사용자 이름을 저장할 SharedPreferences 키

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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

            // SharedPreferences에서 사용자 이름을 가져옵니다.
            String cachedName = getUserNameFromSharedPreferences();
            if (!cachedName.isEmpty()) {
                // 캐시된 이름을 사용하여 UI 업데이트
                textName.setText("이름: " + cachedName);
            } else {
                // Firebase Realtime Database에서 사용자의 이름을 가져오고 설정합니다.
                loadUserName(userId);
            }
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
                // SharedPreferences에 사용자 이름을 저장
                saveUserNameToSharedPreferences(name);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // 데이터베이스에서 이름을 가져오는 데 실패한 경우 처리
            }
        });
    }

    // SharedPreferences에 사용자 이름을 저장하는 메서드
    private void saveUserNameToSharedPreferences(String name) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USER_NAME, name);
        editor.apply();
    }

    // SharedPreferences에서 사용자 이름을 가져오는 메서드
    private String getUserNameFromSharedPreferences() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USER_NAME, "");
    }
}
