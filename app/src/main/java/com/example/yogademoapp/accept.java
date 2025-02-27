package com.example.yogademoapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class accept extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept);

        // Retrieve UI elements
        TextView emailTextView = findViewById(R.id.textviewemail2);
        TextView clientTextView = findViewById(R.id.textviewclient);
        TextView scheduleTextView = findViewById(R.id.textviewschedule);
        TextView dayTextView = findViewById(R.id.textviewday);
        TextView feesTextView = findViewById(R.id.textviewfees);
        ImageView profileImageView = findViewById(R.id.imageView);

        // Retrieve consultant data passed from the previous activity
        String consultantName = getIntent().getStringExtra("consultantName");
        String consultantEmail = getIntent().getStringExtra("consultantEmail");
        int consultantImage = getIntent().getIntExtra("consultantImage", R.drawable.profile);

        // Use default values if no data is passed
        if (consultantName == null) consultantName = "John Doe";
        if (consultantEmail == null) consultantEmail = "johndoe@gmail.com";

        // Retrieve patient’s booking details from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("YogaDemoAppPrefs", MODE_PRIVATE);
        String patientEmail = sharedPreferences.getString("userEmail", "Not Available");
        String patientName = sharedPreferences.getString("userName", "Unknown User");
        String scheduledTime = sharedPreferences.getString("scheduledTime", "Not Scheduled");
        String dayOfWeek = sharedPreferences.getString("dayOfWeek", "Unknown");
        String trainFees = sharedPreferences.getString("trainFees", "0");

        // Display patient details
        emailTextView.setText("Email: " + patientEmail);
        clientTextView.setText("Client: " + patientName);
        scheduleTextView.setText("Scheduled Time: " + scheduledTime);
        dayTextView.setText("Day: " + dayOfWeek);
        feesTextView.setText("Fees Paid: " + trainFees);

        // Set consultant details
        profileImageView.setImageResource(consultantImage);
    }
}
