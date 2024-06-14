package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class NewPostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        String title = getIntent().getStringExtra("title");
        String animalType = getIntent().getStringExtra("animalType");
        String name = getIntent().getStringExtra("name");
        String gender = getIntent().getStringExtra("gender");
        String age = getIntent().getStringExtra("age");
        String feature = getIntent().getStringExtra("feature");
        String imageUri = getIntent().getStringExtra("imageUri");

        TextView tvTitle = findViewById(R.id.tv_title);
        TextView tvAnimalType = findViewById(R.id.tv_animalType);
        TextView tvName = findViewById(R.id.tv_name);
        TextView tvGender = findViewById(R.id.tv_gender);
        TextView tvAge = findViewById(R.id.tv_age);
        TextView tvFeature = findViewById(R.id.tv_feature);
        ImageView imageView = findViewById(R.id.imageView);

        tvTitle.setText(title);
        tvAnimalType.setText(animalType);
        tvName.setText(name);
        tvGender.setText(gender);
        tvAge.setText(age);
        tvFeature.setText(feature);

        if (imageUri != null && !imageUri.isEmpty()) {
            imageView.setImageURI(Uri.parse(imageUri));
        }

        Button goToHome = findViewById(R.id.btn_goToHome);
        goToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewPostActivity.this, GuideActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}
