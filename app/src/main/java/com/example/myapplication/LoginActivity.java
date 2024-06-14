package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mFirebaseAuth;  // 파이어베이스 인증처리
    private DatabaseReference mDatabaseRef;  // 실시간 데이터베이스
    private EditText etEmail, etPw;  // 로그인 입력필드

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("StrayFinder");

        etEmail = findViewById(R.id.et_email);
        etPw = findViewById(R.id.et_pw);

        Button btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 일반 로그인 요청
                String strEmail = etEmail.getText().toString().trim();
                String strPw = etPw.getText().toString().trim();

                mFirebaseAuth.signInWithEmailAndPassword(strEmail, strPw).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 로그인 성공
                            Intent intent = new Intent(LoginActivity.this, HomepageActivity.class);
                            startActivity(intent);
                            finish(); // 현재 액티비티 파괴
                        } else {
                            Toast.makeText(LoginActivity.this, "로그인 실패", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        Button btn_register = findViewById(R.id.btn_register);
        // 회원가입 버튼 누르면 회원가입 화면으로
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 회원가입 화면으로 이동
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        Button btn_admin_login = findViewById(R.id.btn_admin);
        btn_admin_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 관리자 로그인 요청
                String strEmail = etEmail.getText().toString().trim();
                String strPw = etPw.getText().toString().trim();

                mFirebaseAuth.signInWithEmailAndPassword(strEmail, strPw).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            checkIfAdminAndProceed();
                        } else {
                            Toast.makeText(LoginActivity.this, "관리자 로그인 실패", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private void checkIfAdminAndProceed() {
        FirebaseUser currentUser = mFirebaseAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            mDatabaseRef.child("UserAccount").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    UserAccount userAccount = snapshot.getValue(UserAccount.class);
                    if (userAccount != null) {
                        boolean admin = userAccount.isAdmin();
                        Log.d("Login", "admin: " + admin);
                        if (admin) {
                            // 관리자 확인 후 Administer 액티비티로 이동
                            Intent intent = new Intent(LoginActivity.this, AdministerActivity.class);
                            startActivity(intent);
                            finish(); // 현재 액티비티 파괴
                        } else {
                            Toast.makeText(LoginActivity.this, "관리자 권한이 없습니다.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "사용자 정보를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(LoginActivity.this, "데이터베이스 오류: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(LoginActivity.this, "로그인이 필요합니다.", Toast.LENGTH_SHORT).show();
        }
    }
}
