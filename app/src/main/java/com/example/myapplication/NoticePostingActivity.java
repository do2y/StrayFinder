package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NoticePostingActivity extends AppCompatActivity {

    private EditText etTitle, etPost;
    private Button btnPost;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_posting);

        // Firebase 데이터베이스 인스턴스 가져오기
        mDatabase = FirebaseDatabase.getInstance().getReference();

        etTitle = findViewById(R.id.et_title);
        etPost = findViewById(R.id.et_post);
        btnPost = findViewById(R.id.btn_post);

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // EditText에서 제목과 내용 가져오기
                String title = etTitle.getText().toString().trim();
                String post = etPost.getText().toString().trim();

                // 제목과 내용이 모두 입력되었는지 확인
                if (!title.isEmpty() && !post.isEmpty()) {
                    // 파이어베이스에 공지글 저장
                    saveNoticeToFirebase(title, post);
                } else {
                    Toast.makeText(NoticePostingActivity.this, "제목과 내용을 모두 입력하세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // 파이어베이스에 공지글 저장
    private void saveNoticeToFirebase(String title, String post) {
        // Firebase Realtime Database에...
        DatabaseReference noticesRef = mDatabase.child("notices");

        //키 생성
        String noticeId = noticesRef.push().getKey();

        Notice notice = new Notice(noticeId, title, post);

        noticesRef.child(noticeId).setValue(notice).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // 사용자에게 저장 완료 메시지 표시
                Toast.makeText(this, "공지가 성공적으로 작성되었습니다.", Toast.LENGTH_SHORT).show();

                // 작성 완료 후 EditText 초기화
                etTitle.setText("");
                etPost.setText("");

                // Home 액티비티로 이동
                Intent intent = new Intent(NoticePostingActivity.this, AdministerActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "공지 작성에 실패했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
