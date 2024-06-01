package com.example.now_this_pill;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.now_this_pill.Fragment.HomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private TextView btn_register_1;
    private FirebaseAuth mFirebaseAuth;
    private EditText et_email, et_pwd;
    private Button btn_login;

    // 변수 추가
    private boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btn_register_1 = findViewById(R.id.btn_register_1);
        btn_register_1.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        mFirebaseAuth = FirebaseAuth.getInstance();

        et_email = findViewById(R.id.et_email);
        et_pwd = findViewById(R.id.et_pwd);

        // 임시로 아이디와 비밀번호 설정
        et_email.setText("al@naver.com");
        et_pwd.setText("al1234");


        btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(v -> {
            String strEmail = et_email.getText().toString();
            String strPwd = et_pwd.getText().toString();

            if (strEmail.isEmpty() || strPwd.isEmpty()) {
                Toast.makeText(LoginActivity.this, "이메일과 비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();
            } else {
                mFirebaseAuth.signInWithEmailAndPassword(strEmail, strPwd).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 로그인 성공 시 현재 유저 ID를 가져옴
                            String userId = mFirebaseAuth.getCurrentUser().getUid();
                            checkConnectNodeAndNavigate(userId);
                        } else {
                            Toast.makeText(LoginActivity.this, "로그인 실패", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private void checkConnectNodeAndNavigate(String userId) {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference()
                .child("FirebaseEmailAccount")
                .child("userAccount")
                .child(userId);

        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Intent intent;
                if (dataSnapshot.hasChild("connect")) {
                    // "connect" 자식이 존재하면 MainActivity로 이동
                    intent = new Intent(LoginActivity.this, MainActivity.class);
                } else {
                    // "connect" 자식이 존재하지 않으면 HomeActivity로 이동
                    intent = new Intent(LoginActivity.this, HomeActivity.class);
                }
                startActivity(intent);
                finish(); // 현재 액티비티 종료
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // 데이터베이스 에러 처리
                Toast.makeText(LoginActivity.this, "데이터베이스 에러: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 뒤로 가기 버튼 처리
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();

        // 두 번째 누름부터 종료
        new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
    }
}