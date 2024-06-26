package com.example.now_this_pill;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth; //파이어베이스 인증 관련
    private DatabaseReference mDatabaseReference; // 데이터베이스 관련
    private RadioGroup rg_select;
    private EditText et_email, et_pwd, et_pwd_c, et_name;
    private Button btn_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("FirebaseEmailAccount");

        et_email = findViewById(R.id.et_email);   //이메일
        et_pwd = findViewById(R.id.et_pwd);     //비밀번호
        et_pwd_c = findViewById(R.id.et_pwd_c);     //비밀번호 확인
        et_name = findViewById(R.id.et_name);   //이름
        btn_register = findViewById(R.id.btn_register);     //회원가입 버튼

        btn_register.setOnClickListener(v -> {
            String strSelect = getSelect();
            String strEmail = et_email.getText().toString();
            String strPwd = et_pwd.getText().toString();
            String strPwdConfirm = et_pwd_c.getText().toString();
            String strName = et_name.getText().toString();

            // 이메일 형식이 올바른지 확인
            if (!isValidEmail(strEmail)) {
                Toast.makeText(RegisterActivity.this, "올바른 이메일을 입력해주세요.", Toast.LENGTH_SHORT).show();
                return; // 올바른 이메일이 아니면 회원가입을 중단
            }

            // 비밀번호 확인
            if (!strPwd.equals(strPwdConfirm)) {
                Toast.makeText(RegisterActivity.this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                return; // 일치하지 않으면 회원가입을 진행하지 않음
            }

            // 이름 필드가 비어 있는지 확인
            if (TextUtils.isEmpty(strName)) {
                Toast.makeText(RegisterActivity.this, "이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
                return; // 이름이 비어있으면 회원가입을 중단
            }

            // "회원가입 중..." 토스트 메시지 표시
            Toast.makeText(RegisterActivity.this, "회원가입 중...", Toast.LENGTH_SHORT).show();

            mFirebaseAuth.createUserWithEmailAndPassword(strEmail, strPwd).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(Task<AuthResult> task) { //유저가 다 만들어졌을 때
                    if(task.isSuccessful()) {
                        FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser(); //로그인을 성공해서 가능한 것
                        UserAccount account = new UserAccount();
                        //account.setUsertype(strSelect); // 사용자가 선택한 복용자 또는 보호자 설정
                        account.setEmail(strEmail);
                        account.setPassword(strPwd);
                        account.setName(strName);

                        //database에 저장
                        mDatabaseReference.child("userAccount").child(firebaseUser.getUid()).setValue(account);

                        Toast.makeText(RegisterActivity.this, "회원가입에 성공하셨습니다.", Toast.LENGTH_SHORT).show();
                        // 회원가입 성공 시 로그인 페이지로 이동
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish(); // 현재 액티비티 종료
                    } else {
                        Toast.makeText(RegisterActivity.this, "회원가입에 실패하셨습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        });
    }

    private String getSelect() {
        return ""; // 선택이 되지 않았을 경우 빈 문자열 반환
    }

    // 이메일 형식을 확인하는 메서드
    private boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
}
