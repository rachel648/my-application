package com.example.yogademoapp;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class GreenCard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_green_card); // Make sure this matches your actual layout file name

        // Find the views by their IDs
        EditText passwordEditText = findViewById(R.id.passwordEditText);
        LinearLayout passwordChangeSection = findViewById(R.id.passwordChangeSection);

        // Set up the click listener to toggle visibility
        passwordEditText.setOnClickListener(v -> {
            if (passwordChangeSection.getVisibility() == View.GONE) {
                passwordChangeSection.setVisibility(View.VISIBLE);
            } else {
                passwordChangeSection.setVisibility(View.GONE);
            }
        });
    }
}
