package com.example.yogademoapp;

public class User {
    String name,lastMessage,lastMsgTime,phoneNo,Experience,fees,GymNumber;
    int imageId;


    public User(String name, String lastMessage, String lastMsgTime, String phoneNo,String GymNumber, String Experience,String fees, int imageId) {
        this.name = name;
        this.lastMessage = lastMessage;
        this.lastMsgTime = lastMsgTime;
        this.phoneNo = phoneNo;
        this.Experience= Experience;
        this.imageId = imageId;
        this.fees = fees;
        this.GymNumber = GymNumber;

    }

}



