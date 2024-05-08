package com.example.now_this_pill;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.CalendarView;
import android.widget.TextView;

import com.example.now_this_pill.Fragment.CalendarFragment;
import com.example.now_this_pill.Fragment.HomeFragment;
import com.example.now_this_pill.Fragment.ScheduleFragment;
import com.example.now_this_pill.Fragment.SettingFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.text.DateFormat;

import javax.annotation.Nonnull;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        setBottomNavigationView();

        // 앱 초기 실행 시 홈화면으로 설정
        if (savedInstanceState == null) {
            // 홈 화면 프래그먼트로 전환
            getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new HomeFragment()).commit();
        }
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
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new HomeFragment()).commit();
                        return true;
                    case R.id.schedule:
                        item.setIcon(R.drawable.m2_schedule_1);
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new ScheduleFragment()).commit();
                        return true;
                    case R.id.calendar:
                        item.setIcon(R.drawable.m3_calendar_1);
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new CalendarFragment()).commit();
                        return true;
                    case R.id.setting:
                        item.setIcon(R.drawable.m4_setting_1);
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new SettingFragment()).commit();
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


}