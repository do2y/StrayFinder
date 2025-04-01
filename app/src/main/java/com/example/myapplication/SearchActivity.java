// 패키지 및 import 문 추가
package com.example.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.util.FusedLocationSource;
import com.naver.maps.map.overlay.OverlayImage;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SearchActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mapView;
    private NaverMap naverMap;
    private FusedLocationSource locationSource;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        locationSource = new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);

        Button btn_search = findViewById(R.id.btn_search);
        EditText etSearchAddress = findViewById(R.id.et_SearchAddress);

        btn_search.setOnClickListener(v -> {
            String address = etSearchAddress.getText().toString();
            if (!address.isEmpty()) {
                geocodeAddress(address);
            }
        });

        Button btnGoToList = findViewById(R.id.btn_goToList);
        btnGoToList.setOnClickListener(v -> {
            Intent intent = new Intent(SearchActivity.this, PostListActivity.class);
            startActivity(intent);
        });
    }

    private void geocodeAddress(String address) {
        new Thread(() -> {
            try {
                String encodedAddress = java.net.URLEncoder.encode(address, "UTF-8");
                String urlStr = "https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode?query=" + encodedAddress;
                URL url = new URL(urlStr);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("X-NCP-APIGW-API-KEY-ID", "YOUR_API_KEY_ID");
                conn.setRequestProperty("X-NCP-APIGW-API-KEY", "YOUR_API_KEY");

                int responseCode = conn.getResponseCode();
                if (responseCode == 200) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                    }
                    br.close();
                    parseGeocodeResult(sb.toString());
                } else {
                    Log.e("Geocode", "Response code: " + responseCode);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void parseGeocodeResult(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray addresses = jsonObject.getJSONArray("addresses");
            if (addresses.length() > 0) {
                JSONObject address = addresses.getJSONObject(0);
                double lat = address.getDouble("y");
                double lng = address.getDouble("x");
                runOnUiThread(() -> {
                    LatLng latLng = new LatLng(lat, lng);
                    naverMap.moveCamera(CameraUpdate.scrollTo(latLng));
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        this.naverMap = naverMap;
        naverMap.setLocationSource(locationSource);
        UiSettings uiSettings = naverMap.getUiSettings();
        uiSettings.setLocationButtonEnabled(true);

        //디폴트 위치 영남대역
        LatLng initialPosition = new LatLng(35.8365, 128.7549);
        CameraUpdate cameraUpdate = CameraUpdate.scrollTo(initialPosition);
        naverMap.moveCamera(cameraUpdate);
        naverMap.moveCamera(CameraUpdate.zoomTo(14));

        addPostMarkers();
    }

    private void addPostMarkers() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("pets")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            double latitude = document.getDouble("latitude");
                            double longitude = document.getDouble("longitude");
                            String title = document.getString("title");
                            String imageUrl = document.getString("imageUrl");
                            String animalType = document.getString("animalType");
                            String name = document.getString("name");
                            String gender = document.getString("gender");
                            String age = document.getString("age");
                            String feature = document.getString("feature");

                            LatLng position = new LatLng(latitude, longitude);
                            Marker marker = new Marker();
                            marker.setPosition(position);

                            if (imageUrl != null && !imageUrl.isEmpty()) {
                                Glide.with(this)
                                        .asBitmap()
                                        .load(imageUrl)
                                        .into(new CustomTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                int width = 100;
                                                int height = 100;
                                                Bitmap resizedBitmap = Bitmap.createScaledBitmap(resource, width, height, false);

                                                OverlayImage overlayImage = OverlayImage.fromBitmap(resizedBitmap);
                                                marker.setIcon(overlayImage);
                                                marker.setMap(naverMap);
                                            }

                                            @Override
                                            public void onLoadCleared(@Nullable Drawable placeholder) {

                                            }
                                        });
                            } else {
                                Glide.with(this)
                                        .asBitmap()
                                        .load(R.drawable.default_marker)
                                        .into(new CustomTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                int width = 100; //마커 크기
                                                int height = 100;
                                                Bitmap resizedBitmap = Bitmap.createScaledBitmap(resource, width, height, false);

                                                OverlayImage overlayImage = OverlayImage.fromBitmap(resizedBitmap);
                                                marker.setIcon(overlayImage);
                                                marker.setMap(naverMap);
                                            }

                                            @Override
                                            public void onLoadCleared(@Nullable Drawable placeholder) {
                                                // Handle the case where the image load is cleared (optional)
                                            }
                                        });
                            }

                            marker.setOnClickListener(overlay -> {
                                Intent intent = new Intent(SearchActivity.this, PostDetailActivity.class);
                                intent.putExtra("title", title);
                                intent.putExtra("animalType", animalType);
                                intent.putExtra("name", name);
                                intent.putExtra("gender", gender);
                                intent.putExtra("age", age);
                                intent.putExtra("feature", feature);
                                intent.putExtra("imageUrl", imageUrl);
                                startActivity(intent);
                                return true;
                            });
                        }
                    } else {
                        Log.w("SearchActivity", "Error getting documents.", task.getException());
                    }
                });

}


    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
}
