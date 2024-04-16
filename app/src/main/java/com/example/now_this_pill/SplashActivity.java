package com.example.now_this_pill;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SplashActivity extends AppCompatActivity {

    // 스플래시 화면에 보여줄 시간 (밀리초)
    private static final int SPLASH_TIME_OUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 스플래시 화면의 레이아웃 설정
        setContentView(R.layout.activity_splash);

        // 메인 액티비티로 이동하기 위한 인텐트 설정
        final Intent homeIntent = new Intent(this, LoginActivity.class);

        // 지정된 시간(SPLASH_TIME_OUT) 이후에 메인 액티비티로 전환
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(homeIntent);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}