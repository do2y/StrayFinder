package com.example.myapplication;

public class Pet {
    private String postId;
    private String UserId;
    private String title;
    private String animalType;
    private String name;
    private String gender;
    private String age;
    private String feature;
    private String reportType;
    private String imageUrl;

    private double latitude;
    private  double longitude;

    public Pet() {}

    // 생성자
    public Pet(String title, String animalType, String name, String gender, String age, String feature, String reportType, String imageUrl) {
        this.title = title;
        this.animalType = animalType;
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.feature = feature;
        this.reportType = reportType;
        this.imageUrl = imageUrl;  // 이미지 URL 초기화
    }
    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAnimalType() {
        return animalType;
    }

    public void setAnimalType(String animalType) {
        this.animalType = animalType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public void setUserId(String UserId){
        this.UserId = UserId;
    }

    public String getUserId(){
        return UserId;
    }


    public String getImageUrl() {  // 이미지 URL getter 추가
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {  // 이미지 URL setter 추가
        this.imageUrl = imageUrl;
    }

    public double getLatitude() {
        return latitude;
    }
    public double getLongitude() {
        return longitude;
    }
}

