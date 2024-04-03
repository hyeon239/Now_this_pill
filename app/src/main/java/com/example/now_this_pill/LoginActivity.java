package com.example.now_this_pill;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

//임시 주석처리 테스팅할 시 로그인 시간 소모 방지

    private TextView btn_register_1;


    private FirebaseAuth mFirebaseAuth; //파이어베이스 인증 관련
    private EditText et_email, et_pwd;
    private Button btn_login;
/*
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

        btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(v -> {
            String strEmail = et_email.getText().toString();
            String strPwd = et_pwd.getText().toString();

            // 입력 필드가 비어 있는지 확인
            if (strEmail.isEmpty() || strPwd.isEmpty()) {
                // 안내 메시지 표시
                Toast.makeText(LoginActivity.this, "이메일과 비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();
            } else {
                // 비어 있지 않으면 로그인 시도
                mFirebaseAuth.signInWithEmailAndPassword(strEmail, strPwd).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "로그인 실패", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
*/
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

    // 하드코딩된 이메일과 비밀번호 설정
    et_email.setText("111111@naver.com");
    et_pwd.setText("111111");

    btn_login = findViewById(R.id.btn_login);
    btn_login.setOnClickListener(v -> {
        String strEmail = et_email.getText().toString();
        String strPwd = et_pwd.getText().toString();

        // 입력 필드가 비어 있는지 확인
        if (strEmail.isEmpty() || strPwd.isEmpty()) {
            // 안내 메시지 표시
            Toast.makeText(LoginActivity.this, "이메일과 비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();
        } else {
            // 비어 있지 않으면 로그인 시도
            mFirebaseAuth.signInWithEmailAndPassword(strEmail, strPwd).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(Task<AuthResult> task) {
                    if(task.isSuccessful()) {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "로그인 실패", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    });
}


}