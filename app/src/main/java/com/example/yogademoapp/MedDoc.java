package com.example.yogademoapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

public class MedDoc extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_med_doc);

        // Delay for 2 seconds and then start the next activity
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                // Start the next activity here
                Intent intent = new Intent(MedDoc.this, screening.class);
                startActivity(intent);
                finish(); // Optional: Close this activity to prevent going back
            }
        }, 1000); // Delay in milliseconds (2 seconds)
    }
}
