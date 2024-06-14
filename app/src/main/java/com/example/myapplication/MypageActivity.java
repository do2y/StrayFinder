package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MypageActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseRef;

    private TextView tvUserId, tvUserName, tvUserNickname;
    private Button buttonMyPosts;
    private Button buttonLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("StrayFinder");

        tvUserId = findViewById(R.id.tv_id);
        tvUserName = findViewById(R.id.tv_name);
        tvUserNickname = findViewById(R.id.tv_nickname);
        buttonMyPosts = findViewById(R.id.btn_goToMyPost);
        buttonLogout = findViewById(R.id.btn_logout);

        FirebaseUser user = mFirebaseAuth.getCurrentUser();
        if (user != null) {
            loadUserProfile(user.getUid());
        } else {
            Toast.makeText(MypageActivity.this, "로그인이 필요합니다.", Toast.LENGTH_SHORT).show();
            finish();
        }

        buttonMyPosts.setOnClickListener(v -> {
            // 내가 작성한 글 보러가기 버튼 클릭
            Toast.makeText(MypageActivity.this, "내가 작성한 글 보러가기 버튼 클릭", Toast.LENGTH_SHORT).show();
        });

        buttonMyPosts.setOnClickListener(v -> {
            // 내가 작성한 글 보러가기 버튼 클릭
            startActivity(new Intent(MypageActivity.this, MypostActivity.class));
        });

        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //로그아웃
                mFirebaseAuth.signOut();
                Intent intent = new Intent(MypageActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void loadUserProfile(String userId) {
        mDatabaseRef.child("UserAccount").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserAccount userAccount = snapshot.getValue(UserAccount.class);
                if (userAccount != null) {
                    tvUserId.setText("아이디: " + userAccount.getEmailId());
                    tvUserName.setText("이름: " + userAccount.getName());
                    tvUserNickname.setText("닉네임: " + userAccount.getNickname());
                } else {
                    Toast.makeText(MypageActivity.this, "사용자 정보를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MypageActivity.this, "데이터베이스 오류: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



    }
}
