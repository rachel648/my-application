package com.example.yogademoapp;

public class UserOne {
    private String email;
    private int imageResId;

    public UserOne(String email, int imageResId) {
        this.email = email;
        this.imageResId = imageResId;
    }

    public String getEmail() {
        return email;
    }

    public int getImageResId() {
        return imageResId;
    }
}
