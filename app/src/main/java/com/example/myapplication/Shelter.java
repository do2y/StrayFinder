package com.example.myapplication;

public class Shelter {
    private String name;
    private String phone;
    private String address;

    public Shelter(String name, String phone, String address) {
        this.name = name;
        this.phone = phone;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }
}
