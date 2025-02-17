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

        // Enabling Edge-to-Edge layout for full-screen view
        EdgeToEdge.enable(this);

        // Setting the content view with the layout resource
        setContentView(R.layout.activity_accept);

        // Adjust the layout to handle system insets like status bar and navigation bar
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Receive the data passed from the previous activity (e.g., from Agent Activity)
        String consultantName = getIntent().getStringExtra("consultantName");
        String consultantEmail = getIntent().getStringExtra("consultantEmail");
        int consultantImage = getIntent().getIntExtra("consultantImage", 0);

        // Get the TextView for displaying the email
        TextView emailTextView = findViewById(R.id.textviewemail);

        // Get the ImageView for displaying the profile image
        ImageView profileImageView = findViewById(R.id.imageView);

        // If no data is passed, use default values (you can remove this if you always expect data)
        if (consultantName == null) consultantName = "John Doe";
        if (consultantEmail == null) consultantEmail = "johndoe@gmail.com";
        if (consultantImage == 0) consultantImage = R.drawable.profile; // default image resource

        // Set the email text (for the TextView)
        emailTextView.setText(consultantEmail);

        // Set the image source (for the ImageView)
        profileImageView.setImageResource(consultantImage);
    }
}
