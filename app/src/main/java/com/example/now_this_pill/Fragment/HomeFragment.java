package com.example.now_this_pill.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.now_this_pill.R;
import com.google.firebase.auth.FirebaseAuth;

public class HomeFragment extends Fragment {

    private TextView textEmail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // TextView 초기화
        textEmail = view.findViewById(R.id.text_email);

        // FirebaseAuth 인스턴스를 사용하여 현재 로그인한 사용자의 이메일을 가져옵니다.
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            String email = mAuth.getCurrentUser().getEmail();

            // 이메일을 TextView에 설정합니다.
            textEmail.setText(email);
        }

        return view;
    }
}