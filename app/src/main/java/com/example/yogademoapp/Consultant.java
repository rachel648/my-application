package com.example.yogademoapp;

public class Consultant {
    public String name;
    public String phoneNo;
    public String experience;
    public String fees;
    public String gymNumber;
    public int imageId;
    public int rating;
    public String imageUrl; // Added field for Firebase Storage URL

    // Default constructor required for Firebase
    public Consultant() {
    }

    // Constructor with all fields including imageUrl
    public Consultant(String name, String phoneNo, String experience, String fees,
                      String gymNumber, int imageId, int rating, String imageUrl) {
        this.name = name;
        this.phoneNo = phoneNo;
        this.experience = experience;
        this.fees = fees;
        this.gymNumber = gymNumber;
        this.imageId = imageId;
        this.rating = rating;
        this.imageUrl = imageUrl;
    }

    // Constructor without imageUrl (for backward compatibility)
    public Consultant(String name, String phoneNo, String experience, String fees,
                      String gymNumber, int imageId, int rating) {
        this(name, phoneNo, experience, fees, gymNumber, imageId, rating, "");
    }

    // Getters and setters for all fields
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getFees() {
        return fees;
    }

    public void setFees(String fees) {
        this.fees = fees;
    }

    public String getGymNumber() {
        return gymNumber;
    }

    public void setGymNumber(String gymNumber) {
        this.gymNumber = gymNumber;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}