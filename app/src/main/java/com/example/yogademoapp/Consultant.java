package com.example.yogademoapp;

public class Consultant {
    public String name;
    public String phoneNo;
    public String experience;
    public String fees;
    public String gymNumber;
    public int imageId;

    public Consultant() {
        // Default constructor required for calls to DataSnapshot.getValue(Consultant.class)
    }

    public Consultant(String name, String phoneNo, String experience, String fees, String gymNumber, int imageId) {
        this.name = name;
        this.phoneNo = phoneNo;
        this.experience = experience;
        this.fees = fees;
        this.gymNumber = gymNumber;
        this.imageId = imageId;
    }
}
