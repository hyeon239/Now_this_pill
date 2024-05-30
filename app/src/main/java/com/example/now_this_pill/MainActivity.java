package com.example.now_this_pill;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.now_this_pill.Fragment.CalendarFragment;
import com.example.now_this_pill.Fragment.HomeActivity;
import com.example.now_this_pill.Fragment.ScheduleFragment;
import com.example.now_this_pill.Fragment.SettingFragment;
import com.example.now_this_pill.home.PillEatFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setBottomNavigationView();

        // 앱 초기 실행 시 EatPillFragment로 설정
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_container, new PillEatFragment())
                    .commit();
        }

        // FCM 토큰 가져오기
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete( Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();
                        Log.d(TAG, "FCM Token: " + token);

                        // 서버로 토큰 전송
                        sendTokenToServer(token);
                    }
                });
    }
    // 하단 네비게이션뷰 설정
    private void setBottomNavigationView() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                // 모든 아이템의 아이콘을 원래 상태로 되돌리기
                resetIcons(bottomNavigationView);

                switch (item.getItemId()) {
                    case R.id.home:
                        item.setIcon(R.drawable.m1_home_1);
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new PillEatFragment()).commit();
                        ((TextView) findViewById(R.id.toolbar_title)).setText("지금이약!");
                        return true;
                    case R.id.schedule:
                        item.setIcon(R.drawable.m2_schedule_1);
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new ScheduleFragment()).commit();
                        ((TextView) findViewById(R.id.toolbar_title)).setText("일정");
                        return true;
                    case R.id.calendar:
                        item.setIcon(R.drawable.m3_calendar_1);
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new CalendarFragment()).commit();
                        ((TextView) findViewById(R.id.toolbar_title)).setText("달력");
                        return true;
                    case R.id.setting:
                        item.setIcon(R.drawable.m4_setting_1);
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new SettingFragment()).commit();
                        ((TextView) findViewById(R.id.toolbar_title)).setText("설정");
                        return true;
                    default:
                        return false;
                }
            }
        });

        // 앱 초기 실행 시 홈 아이콘을 활성화된 상태로 설정
        bottomNavigationView.setSelectedItemId(R.id.home);
    }

    // 모든 아이템의 아이콘을 원래 상태로 되돌리는 메서드
    private void resetIcons(BottomNavigationView bottomNavigationView) {
        bottomNavigationView.getMenu().findItem(R.id.home).setIcon(R.drawable.m1_home_0);
        bottomNavigationView.getMenu().findItem(R.id.schedule).setIcon(R.drawable.m2_schedule_0);
        bottomNavigationView.getMenu().findItem(R.id.calendar).setIcon(R.drawable.m3_calendar_0);
        bottomNavigationView.getMenu().findItem(R.id.setting).setIcon(R.drawable.m4_setting_0);
    }

    public void setToolbarTitle(String title) {
        TextView toolbarTitle = findViewById(R.id.toolbar_title);
        if (toolbarTitle != null) {
            toolbarTitle.setText(title);
        }
    }


    // 툴바에 뒤로가기 버튼을 추가하고 클릭 이벤트 처리
    public void addBackButtonToToolbar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.btn_back); // 여기서 btn_back은 여러분의 뒤로가기 버튼 이미지입니다.
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    // 하단 메뉴바를 보이게 하는 메서드
    public void showBottomNavigationBar() {
        // 하단 메뉴바를 가져오고 보이게 설정
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setVisibility(View.VISIBLE);
    }

    public void hideBottomNavigationBar() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setVisibility(View.GONE);
    }

    // 서버에 FCM 토큰을 전송하는 메서드 (실제 서버 URL 및 로직에 맞게 구현)
    private void sendTokenToServer(String token) {
        // 서버에 FCM 토큰을 전송하는 로직을 구현합니다.
        // 예: HttpURLConnection을 사용하여 서버에 POST 요청 보내기
        // 여기서는 예시로 간단하게 로그 출력만 합니다.
        Log.d(TAG, "FCM 토큰을 서버에 전송: " + token);
    }
}