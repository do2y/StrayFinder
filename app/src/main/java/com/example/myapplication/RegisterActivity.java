package com.example.myapplication;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mFirebaseAuth;  // 파이어베이스 인증처리
    private DatabaseReference mDatabaseRef;  // 실시간 데이터베이스
    private EditText etEmail, etPw, etName, etPwCheck, etNickname;  // 회원가입 입력필드
    private Button btnRegister;  // 회원가입 버튼

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        FirebaseApp.initializeApp(this);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("StrayFinder");

        etEmail = findViewById(R.id.et_email);
        etPw = findViewById(R.id.et_pw);
        etPwCheck = findViewById(R.id.et_pwCheck);
        etName = findViewById(R.id.et_name);
        etNickname = findViewById(R.id.et_nickname);
        btnRegister = findViewById(R.id.btn_register);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strEmail = etEmail.getText().toString().trim();
                String strPw = etPw.getText().toString().trim();
                String strPwCheck = etPwCheck.getText().toString().trim();
                String strName = etName.getText().toString().trim();
                String strNickname = etNickname.getText().toString().trim();

                // 이메일 유효성 검사
                if (!Patterns.EMAIL_ADDRESS.matcher(strEmail).matches()) {
                    Toast.makeText(RegisterActivity.this, "이메일 양식이 잘못되었습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 비밀번호 길이 검사
                if (strPw.length() < 6) {
                    Toast.makeText(RegisterActivity.this, "비밀번호 6자리를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (strPw.equals(strPwCheck)) {
                    mFirebaseAuth.createUserWithEmailAndPassword(strEmail, strPw).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            // 회원가입이 성공했을 때
                            if (task.isSuccessful()) {
                                FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
                                UserAccount account = new UserAccount();
                                account.setIdToken(firebaseUser.getUid());
                                account.setEmailId(firebaseUser.getEmail());
                                account.setPassword(strPw);
                                account.setName(strName);
                                account.setNickname(strNickname);
                                account.setAdmin(false);

                                // Firebase Realtime Database에 저장
                                mDatabaseRef.child("UserAccount").child(firebaseUser.getUid()).setValue(account);

                                Toast.makeText(RegisterActivity.this, "회원가입에 성공하셨습니다.", Toast.LENGTH_SHORT).show();
                                finish(); // 회원가입 완료 후 현재 액티비티 종료
                            } else {
                                Toast.makeText(RegisterActivity.this, "회원가입에 실패하셨습니다.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(RegisterActivity.this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
