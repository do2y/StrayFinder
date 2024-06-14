package com.example.myapplication;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class PostingActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int LOCATION_REQUEST_CODE = 2;

    private EditText etTitle, etAnimalType, etName, etGender, etAge, etFeature;
    private Button buttonAttachPhoto, buttonPost, buttonSetLocation;
    private Uri imageUri;
    private double latitude, longitude;
    private StorageReference mStorageRef;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private String userId, reportType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posting);

        mStorageRef = FirebaseStorage.getInstance().getReference();
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        userId = currentUser.getUid();

        Intent intent = getIntent();
        reportType = intent.getStringExtra("reportType");

        etTitle = findViewById(R.id.et_title);
        etAnimalType = findViewById(R.id.et_animalType);
        etName = findViewById(R.id.et_name);
        etGender = findViewById(R.id.et_gender);
        etAge = findViewById(R.id.et_age);
        etFeature = findViewById(R.id.et_feature);
        buttonAttachPhoto = findViewById(R.id.button_attach_photo);
        buttonPost = findViewById(R.id.button_post);
        buttonSetLocation = findViewById(R.id.button_set_location);

        buttonAttachPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        buttonSetLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PostingActivity.this, LocationSettingActivity.class);
                startActivityForResult(intent, LOCATION_REQUEST_CODE);
            }
        });

        buttonPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = etTitle.getText().toString();
                String animalType = etAnimalType.getText().toString();
                String name = etName.getText().toString();
                String gender = etGender.getText().toString();
                String age = etAge.getText().toString();
                String feature = etFeature.getText().toString();

                if (title.isEmpty() || animalType.isEmpty() || name.isEmpty() || gender.isEmpty() || age.isEmpty() || feature.isEmpty()) {
                    Toast.makeText(PostingActivity.this, "모든 필드를 입력하세요.", Toast.LENGTH_SHORT).show();
                } else {
                    // Check if imageUri is null
                    if (imageUri != null) {
                        uploadImageAndSavePetData(title, animalType, name, gender, age, feature, reportType, userId, latitude, longitude);
                    } else {
                        //사진없어도 업로드 가능
                        savePetData(title, animalType, name, gender, age, feature, reportType, userId, latitude, longitude, "");
                    }
                }
            }
        });

    }

    private void uploadImageAndSavePetData(String title, String animalType, String name, String gender, String age, String feature, String reportType, String userId, double latitude, double longitude) {
        if (imageUri != null) {
            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
            UploadTask uploadTask = fileReference.putFile(imageUri);

            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        Map<String, Object> pet = new HashMap<>();
                        pet.put("title", title);
                        pet.put("animalType", animalType);
                        pet.put("name", name);
                        pet.put("gender", gender);
                        pet.put("age", age);
                        pet.put("feature", feature);
                        pet.put("reportType", reportType);
                        pet.put("userId", userId);
                        pet.put("latitude", latitude);
                        pet.put("longitude", longitude);
                        pet.put("imageUrl", downloadUri.toString());

                        db.collection("pets")
                                .add(pet)
                                .addOnSuccessListener(documentReference -> {
                                    Toast.makeText(PostingActivity.this, "작성 완료!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(PostingActivity.this, NewPostActivity.class);
                                    intent.putExtra("title", title);
                                    intent.putExtra("animalType", animalType);
                                    intent.putExtra("name", name);
                                    intent.putExtra("gender", gender);
                                    intent.putExtra("age", age);
                                    intent.putExtra("feature", feature);
                                    startActivity(intent);
                                    finish();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(PostingActivity.this, "작성 실패: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                    } else {
                        Toast.makeText(PostingActivity.this, "이미지 업로드 실패: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(PostingActivity.this, "이미지를 선택하세요.", Toast.LENGTH_SHORT).show();
        }
    }
    private void savePetData(String title, String animalType, String name, String gender, String age, String feature, String reportType, String userId, double latitude, double longitude, String imageUrl) {
        Map<String, Object> pet = new HashMap<>();
        pet.put("title", title);
        pet.put("animalType", animalType);
        pet.put("name", name);
        pet.put("gender", gender);
        pet.put("age", age);
        pet.put("feature", feature);
        pet.put("reportType", reportType);
        pet.put("userId", userId);
        pet.put("latitude", latitude);
        pet.put("longitude", longitude);
        pet.put("imageUrl", imageUrl);

        db.collection("pets")
                .add(pet)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(PostingActivity.this, "작성 완료!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(PostingActivity.this, NewPostActivity.class);
                    intent.putExtra("title", title);
                    intent.putExtra("animalType", animalType);
                    intent.putExtra("name", name);
                    intent.putExtra("gender", gender);
                    intent.putExtra("age", age);
                    intent.putExtra("feature", feature);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(PostingActivity.this, "작성 실패: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
        } else if (requestCode == LOCATION_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            latitude = data.getDoubleExtra("latitude", 0);
            longitude = data.getDoubleExtra("longitude", 0);
            Toast.makeText(this, "위치 설정 완료: (" + latitude + ", " + longitude + ")", Toast.LENGTH_SHORT).show();
        }
    }
}