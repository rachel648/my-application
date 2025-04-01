package com.example.yogademoapp;

public class User {
    private final String name;
    private String lastMessage;
    private String lastMsgTime;
    private String phoneNo;
    private String gymNumber;
    private String experience;
    private String fees;
    private int imageId;
    private int rating;
    private String imageUrl; // Added field for Firebase Storage URL

    // Constructor with all fields including imageUrl
    public User(String name, String lastMessage, String lastMsgTime, String phoneNo,
                String gymNumber, String experience, String fees, int imageId,
                int rating, String imageUrl) {
        this.name = name;
        this.lastMessage = lastMessage;
        this.lastMsgTime = lastMsgTime;
        this.phoneNo = phoneNo;
        this.gymNumber = gymNumber;
        this.experience = experience;
        this.fees = fees;
        this.imageId = imageId;
        this.rating = rating;
        this.imageUrl = imageUrl;
    }

    // Constructor without imageUrl (for backward compatibility)
    public User(String name, String lastMessage, String lastMsgTime, String phoneNo,
                String gymNumber, String experience, String fees, int imageId,
                int rating) {
        this(name, lastMessage, lastMsgTime, phoneNo, gymNumber, experience,
                fees, imageId, rating, "");
    }

    public String getName() {
        return name;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public String getLastMsgTime() {
        return lastMsgTime;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public String getGymNumber() {
        return gymNumber;
    }

    public String getExperience() {
        return experience;
    }

    public String getFees() {
        return fees;
    }

    public int getRating() {
        return rating;
    }

    public int getImageId() {
        return imageId;
    }

    // New getter for imageUrl
    public String getImageUrl() {
        return imageUrl;
    }

    // Setter for imageUrl
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    // Remove or fix this method as it doesn't make sense for email to return int
    public String getEmail() {
        return ""; // Return appropriate email value if needed
    }
}