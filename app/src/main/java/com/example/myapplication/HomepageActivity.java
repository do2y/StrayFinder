package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class HomepageActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        // 버튼 초기화
        Button btn_goToSearch = findViewById(R.id.btn_goToSearch);
        Button btn_goToPost = findViewById(R.id.btn_goToPost);
        Button btn_goToNotice = findViewById(R.id.btn_goToNotice);
        Button btn_goToShelterInfo = findViewById((R.id.btn_goToShelterInfo));

        // 버튼 클릭 이벤트 처리
        btn_goToSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //게시글 검색 지도 화면으로 이동
                Intent intent = new Intent(HomepageActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });

        btn_goToShelterInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomepageActivity.this, ShelterInfoActivity.class);
                startActivity(intent);
            }
        });

        btn_goToPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 게시글 작성 화면으로 이동
                Intent intent = new Intent(HomepageActivity.this, Posting_optionActivity.class);
                startActivity(intent);
            }
        });

        Button btnGoToMyPage = findViewById(R.id.btn_goToMypage);
        btnGoToMyPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 마이페이지 화면으로 이동
                startActivity(new Intent(HomepageActivity.this, MypageActivity.class));
            }
        });

        btn_goToNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomepageActivity.this, NoticeBoardActivity.class));
            }
        });

    }
}
