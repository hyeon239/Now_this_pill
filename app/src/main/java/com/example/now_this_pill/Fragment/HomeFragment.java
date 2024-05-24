package com.example.now_this_pill.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.now_this_pill.R;
import com.example.now_this_pill.home.PillEatFragment;
import com.google.firebase.auth.FirebaseAuth;

public class HomeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // 버튼 찾기
        Button btnLogin = view.findViewById(R.id.btn_login);

        // 버튼에 클릭 리스너 설정
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // PillEatFragment로 이동
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.main_container, new PillEatFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return view;
    }
}