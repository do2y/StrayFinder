package com.example.myapplication;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class PostDetailActivity extends AppCompatActivity {

    private TextView tvTitle, tvAnimalType, tvName, tvGender, tvAge, tvFeature;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        // Initialize views
        tvTitle = findViewById(R.id.tv_title);
        tvAnimalType = findViewById(R.id.tv_animalType);
        tvName = findViewById(R.id.tv_name);
        tvGender = findViewById(R.id.tv_gender);
        tvAge = findViewById(R.id.tv_age);
        tvFeature = findViewById(R.id.tv_feature);
        imageView = findViewById(R.id.imageView);

        if (getIntent() != null) {
            tvTitle.setText(getIntent().getStringExtra("title"));
            tvAnimalType.setText(getIntent().getStringExtra("animalType"));
            tvName.setText(getIntent().getStringExtra("name"));
            tvGender.setText(getIntent().getStringExtra("gender"));
            tvAge.setText(getIntent().getStringExtra("age"));
            tvFeature.setText(getIntent().getStringExtra("feature"));
            String imageUrl = getIntent().getStringExtra("imageUrl");

            Glide.with(this)
                    .load(imageUrl)
                    .into(imageView);
        }
    }
}
