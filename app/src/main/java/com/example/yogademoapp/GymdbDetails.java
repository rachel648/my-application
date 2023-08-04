package com.example.yogademoapp;

public class GymdbDetails {
    private int id;
    private String instructorName;
    private String gymNumber;
    private String experience;
    private String mobileNo;
    private int fees;

    public GymdbDetails(int id, String instructorName, String gymNumber, String experience, String mobileNo, int fees) {
        this.id = id;
        this.instructorName = instructorName;
        this.gymNumber = gymNumber;
        this.experience = experience;
        this.mobileNo = mobileNo;
        this.fees = fees;
    }

    public int getId() {
        return id;
    }

    public String getInstructorName() {
        return instructorName;
    }

    public String getGymNumber() {
        return gymNumber;
    }

    public String getExperience() {
        return experience;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public int getFees() {
        return fees;
    }
}
