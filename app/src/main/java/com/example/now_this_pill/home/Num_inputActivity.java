package com.example.now_this_pill.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentTransaction;

import com.example.now_this_pill.MainActivity;
import com.example.now_this_pill.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Num_inputActivity extends AppCompatActivity {

    private static final String TAG = "Num_inputActivity";
    private TextView idTokenTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_num_input);

        idTokenTextView = findViewById(R.id.idTokenTextView); // 텍스트가 표시될 TextView의 ID로 교체해야 함

        // Firebase 사용자 가져오기
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();

            // Firebase 데이터베이스 참조 설정
            DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference()
                    .child("FirebaseEmailAccount")
                    .child("userAccount")
                    .child(userId);

            // 데이터베이스에서 ID 토큰 및 "connect" 값을 지속적으로 가져오기
            databaseRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String idToken = dataSnapshot.child("idToken").getValue(String.class);

                        if (idToken != null) {
                            idTokenTextView.setText(idToken); // ID 토큰 설정
                        } else {
                            idTokenTextView.setText("ID 토큰이 없습니다.");
                        }

                        boolean hasConnect = dataSnapshot.hasChild("connect");
                        Log.d(TAG, "Has connect node: " + hasConnect);
                        if (hasConnect) {
                            // "connect" 자식이 존재하면 MainActivity로 이동
                            Log.d(TAG, "Navigating to MainActivity");
                            Intent intent = new Intent(Num_inputActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish(); // 현재 액티비티 종료
                        }

                    } else {
                        // 데이터가 없는 경우 처리
                        Toast.makeText(Num_inputActivity.this, "사용자 정보를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // 오류 처리
                    Toast.makeText(Num_inputActivity.this, "데이터베이스 오류: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // 사용자가 로그인되어 있지 않은 경우 처리
            idTokenTextView.setText("사용자가 로그인되어 있지 않습니다.");
        }
    }
}