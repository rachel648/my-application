package com.example.yogademoapp;

public class GymdbDetails {
    private final int id;
    private final String instructorName;
    private final String gymNumber;
    private final String experience;
    private final String mobileNo;
    private final int fees;

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
