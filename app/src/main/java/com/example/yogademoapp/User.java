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

    public User(String name, String lastMessage, String lastMsgTime, String phoneNo, String gymNumber, String experience, String fees, int imageId) {
        this.name = name;
        this.lastMessage = lastMessage;
        this.lastMsgTime = lastMsgTime;
        this.phoneNo = phoneNo;
        this.gymNumber = gymNumber;
        this.experience = experience;
        this.fees = fees;
        this.imageId = imageId;
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

    public int getImageId() {
        return imageId;
    }

    //yes

    public int getEmail() {
        return 0;
    }
}
