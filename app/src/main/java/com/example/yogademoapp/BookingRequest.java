package com.example.yogademoapp;

public class BookingRequest {
    private String clientEmail;
    private String consultantName;
    private String scheduledTime;
    private String dayOfWeek;
    private boolean isAccepted; // To track if the request is accepted or rejected

    // Default constructor required for Firestore
    public BookingRequest() {}

    // Parameterized constructor
    public BookingRequest(String clientEmail, String consultantName, String scheduledTime, String dayOfWeek) {
        this.clientEmail = clientEmail;
        this.consultantName = consultantName;
        this.scheduledTime = scheduledTime;
        this.dayOfWeek = dayOfWeek;
        this.isAccepted = false; // Default to false (pending)
    }

    // Getters and setters
    public String getClientEmail() { return clientEmail; }
    public void setClientEmail(String clientEmail) { this.clientEmail = clientEmail; }

    public String getConsultantName() { return consultantName; }
    public void setConsultantName(String consultantName) { this.consultantName = consultantName; }

    public String getScheduledTime() { return scheduledTime; }
    public void setScheduledTime(String scheduledTime) { this.scheduledTime = scheduledTime; }

    public String getDayOfWeek() { return dayOfWeek; }
    public void setDayOfWeek(String dayOfWeek) { this.dayOfWeek = dayOfWeek; }

    public boolean isAccepted() { return isAccepted; }
    public void setAccepted(boolean accepted) { isAccepted = accepted; }
}