package com.example.yogademoapp;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class accept extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_accept);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Receive the data passed from the Agent activity
        String consultantName = getIntent().getStringExtra("consultantName");
        String consultantEmail = getIntent().getStringExtra("consultantEmail");
        int consultantImage = getIntent().getIntExtra("consultantImage", 0);

        // Set the received data to the views in the top section of the Accept activity
        TextView nameTextView = findViewById(R.id.textviewemail); // The TextView where the email is displayed
        ImageView profileImageView = findViewById(R.id.imageView); // Profile image

        // Update the TextView and ImageView with the consultant's details
        nameTextView.setText(consultantName);
        profileImageView.setImageResource(consultantImage);
    }
}
