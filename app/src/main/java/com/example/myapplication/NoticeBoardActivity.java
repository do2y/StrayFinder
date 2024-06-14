package com.example.myapplication;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NoticeBoardActivity extends AppCompatActivity {
    private DatabaseReference mDatabaseRef;
    private TextView tvNoticeTitle, tvNoticeContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_board);

        // Firebase Database 레퍼런스 초기화
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("notices");

        // TextView 요소 초기화
        tvNoticeTitle = findViewById(R.id.tv_notice_title);
        tvNoticeContent = findViewById(R.id.tv_notice_content);

        // Firebase Realtime Database에서 공지 데이터 읽기
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String title = snapshot.child("title").getValue(String.class);
                    String content = snapshot.child("content").getValue(String.class);

                    // 가져온 데이터를 TextView 요소에 업데이트
                    tvNoticeTitle.setText(title);
                    tvNoticeContent.setText(content);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 에러 처리
                Toast.makeText(NoticeBoardActivity.this, "공지사항을 불러오는 데 실패했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
