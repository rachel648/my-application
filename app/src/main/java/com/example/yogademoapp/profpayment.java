package com.example.yogademoapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class profpayment extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private TextView textViewSessionOne;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profpayment);

        // Initialize views
        textViewSessionOne = findViewById(R.id.textViewSessionFee);
        CardView notification = findViewById(R.id.notifications);
        CardView settingsprof = findViewById(R.id.setting);

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("fees");

        // Retrieve the latest fee value from Firebase
        databaseReference.child("latest_fee").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.getValue() != null) {
                    String latestFee = snapshot.getValue(String.class);
                    Log.d("Firebase", "Retrieved Fee: " + latestFee);
                    textViewSessionOne.setText("Session Fee: " + latestFee);
                } else {
                    Log.e("Firebase", "Fee not found or null");
                    textViewSessionOne.setText("Session Fee: Not available");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Failed to read fee", error.toException());
                textViewSessionOne.setText("Failed to load session fee.");
            }
        });

        // Notification Card click listener
        notification.setOnClickListener(v -> {
            Intent intent = new Intent(profpayment.this, Not.class);
            startActivity(intent);
        });

        // Settings Card click listener
        settingsprof.setOnClickListener(v -> {
            Intent intent = new Intent(profpayment.this, GreenCard.class);
            startActivity(intent);
        });
    }
}
