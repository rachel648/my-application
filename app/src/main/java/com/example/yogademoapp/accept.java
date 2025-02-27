package com.example.yogademoapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class accept extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept);

        // Retrieve UI elements
        TextView consultantEmailTextView = findViewById(R.id.textviewConsultantEmail); // Consultant's email
        TextView patientEmailTextView = findViewById(R.id.textviewPatientEmail); // Patient's email
        TextView clientTextView = findViewById(R.id.textviewclient);
        TextView scheduleTextView = findViewById(R.id.textviewschedule);
        TextView dayTextView = findViewById(R.id.textviewday);
        TextView feesTextView = findViewById(R.id.textviewfees);
        ImageView profileImageView = findViewById(R.id.imageView);
        Button acceptButton = findViewById(R.id.buttonAccept);
        Button rejectButton = findViewById(R.id.buttonReject);

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

        // Display consultant's email without label
        consultantEmailTextView.setText(consultantEmail);

        // Display patient's details
        patientEmailTextView.setText("Patient Email: " + patientEmail);
        clientTextView.setText("Client: " + patientName);
        scheduleTextView.setText("Scheduled Time: " + scheduledTime);
        dayTextView.setText("Day: " + dayOfWeek);
        feesTextView.setText("Fees Paid: " + trainFees);

        // Set consultant image
        profileImageView.setImageResource(consultantImage);

        // Accept button click event (Show toast message)
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(accept.this, "Accepted", Toast.LENGTH_SHORT).show();
            }
        });

        // Reject button click event (Redirect to UserActivity)
        rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(accept.this, "Reschedule message successfully sent to patient", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
