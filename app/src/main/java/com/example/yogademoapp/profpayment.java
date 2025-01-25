package com.example.yogademoapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class profpayment extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profpayment);

        CardView notification = findViewById(R.id.notifications);
        notification.setOnClickListener(v -> {
            Intent intent = new Intent(profpayment.this, Not.class);
            startActivity(intent);
        });

        CardView settingsprof = findViewById(R.id.setting);
        settingsprof.setOnClickListener(v -> {
            Intent intent = new Intent(profpayment.this, GreenCard.class);
            startActivity(intent);
        });
    }
}