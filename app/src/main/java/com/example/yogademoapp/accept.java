package com.example.yogademoapp;

import android.content.SharedPreferences;
import android.os.Bundle;
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
        TextView consultantEmailTextView = findViewById(R.id.textviewConsultantEmail);
        TextView patientEmailTextView = findViewById(R.id.textviewPatientEmail);
        TextView clientTextView = findViewById(R.id.textviewclient);
        TextView scheduleTextView = findViewById(R.id.textviewschedule);
        TextView dayTextView = findViewById(R.id.textviewday);
        TextView feesTextView = findViewById(R.id.textviewfees);
        ImageView profileImageView = findViewById(R.id.imageView);
        Button acceptButton = findViewById(R.id.buttonAccept);
        Button rejectButton = findViewById(R.id.buttonReject);

        // Retrieve consultant details
        String consultantName = getIntent().getStringExtra("consultantName");
        String consultantEmail = getIntent().getStringExtra("consultantEmail");
        int consultantImage = getIntent().getIntExtra("consultantImage", R.drawable.profile);

        if (consultantName == null) consultantName = "John Doe";
        if (consultantEmail == null) consultantEmail = "johndoe@gmail.com";

        // Retrieve patient’s email and booking details
        SharedPreferences sharedPreferences = getSharedPreferences("UserProfile", MODE_PRIVATE);
        String patientEmail = sharedPreferences.getString("patientEmail", "Not Available");

        // Extract name from email
        String patientName = sharedPreferences.getString("userName", "Unknown User");
        if (patientName.equals("Unknown User") && patientEmail.contains("@")) {
            patientName = patientEmail.split("@")[0]; // Extract username from email
        }

        // Retrieve other details
        String scheduledTime = sharedPreferences.getString("scheduledTime", "Not Scheduled");
        String dayOfWeek = sharedPreferences.getString("dayOfWeek", "Unknown");
        String trainFees = sharedPreferences.getString("trainFees", "0");

        // Debugging: Verify retrieved email
        Toast.makeText(this, patientEmail, Toast.LENGTH_LONG).show();
        Toast.makeText(this, consultantEmail, Toast.LENGTH_LONG).show();

        // Display correct emails
        consultantEmailTextView.setText(consultantEmail);
        patientEmailTextView.setText("Patient Email: " + patientEmail);

        // Display other patient details
        clientTextView.setText("Client: " + patientName);
        scheduleTextView.setText("Scheduled Time: " + scheduledTime);
        dayTextView.setText("Day: " + dayOfWeek);
        feesTextView.setText("Fees Paid: " + trainFees);

        // Set consultant image
        profileImageView.setImageResource(consultantImage);

        acceptButton.setOnClickListener(v ->
                Toast.makeText(accept.this, "Accepted", Toast.LENGTH_SHORT).show());

        rejectButton.setOnClickListener(v ->
                Toast.makeText(accept.this, "Reschedule message successfully sent to patient", Toast.LENGTH_SHORT).show());
    }
}
