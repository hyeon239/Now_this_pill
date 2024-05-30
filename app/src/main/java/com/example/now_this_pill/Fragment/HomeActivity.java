package com.example.now_this_pill.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.view.View;
import android.widget.Button;

import com.example.now_this_pill.R;
import com.example.now_this_pill.home.Num_inputActivity;
import com.example.now_this_pill.home.PillEatFragment;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // 버튼 찾기
        Button btnLogin = findViewById(R.id.btn_login);

        // 버튼에 클릭 리스너 설정
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Num_inputActivity로 이동
                Intent intent = new Intent(HomeActivity.this, Num_inputActivity.class);
                startActivity(intent);
            }
        });
    }
}