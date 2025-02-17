package com.example.yogademoapp;

public class UserOne {
    private String username;
    private String email;
    private int imageId;

    public UserOne(String username, String email, int imageId) {
        this.username = username;
        this.email = email;
        this.imageId = imageId;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public int getImageId() {
        return imageId;
    }
}
