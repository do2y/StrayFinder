package com.example.myapplication;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ShelterInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shelter_info);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Shelter> shelterList = new ArrayList<>();
        shelterList.add(new Shelter("대구유기동물보호센터", "053-964-6258", "대구광역시 동구 금강로 151-13 (금강동)"));
        shelterList.add(new Shelter("동인동물병원", "053-424-4258", "대구광역시 중구 동인동4가 국채보상로 724 (동인동4가) 가정만 관할"));
        shelterList.add(new Shelter("희망반려동물병원", "053-634-7975", "대구광역시 달성군 다사읍 사문진로 447 (달성읍) 희망관 관할"));
        shelterList.add(new Shelter("금강동물병원", "053-751-4211", "대구광역시 수성구 달구벌대로 150 (만촌동) 금강관 관할"));
        shelterList.add(new Shelter("다사랑동물병원", "053-614-3775", "대구광역시 달성군 논공읍 논공로 64 (논공읍) 논공관 관할"));
        shelterList.add(new Shelter("포산동물병원", "053-616-2478", "대구광역시 달성군 현풍면 비슬로 614-1 (현풍면) 논공읍 관할"));
        shelterList.add(new Shelter("다사랑동물병원", "053-614-3775", "대구광역시 달성군 논공읍 논공로 64 (논공읍) 논공관 관할"));
        shelterList.add(new Shelter("희망반려동물병원", "053-634-7975", "대구광역시 달성군 다사읍 사문진로 447 (달성읍) 희망관 관할"));
        shelterList.add(new Shelter("신세계동물병원", "053-751-6693", "대구광역시 수성구 동대구로 103 (두산동) 신세계관 관할"));
        shelterList.add(new Shelter("예스동물병원", "053-768-7543", "대구광역시 수성구 동대구로 207 (황금동) 예스동물병원"));

        ShelterAdapter adapter = new ShelterAdapter(shelterList);
        recyclerView.setAdapter(adapter);
    }
}
