package com.example.yogademoapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class BoxActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_box);

        CardView exit = findViewById(R.id.Boxback); //writinf functinally of back(boxback)
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        CardView Class_And_Schedule = findViewById(R.id.Class_And_Schedule);
        Class_And_Schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(BoxActivity.this, GymDetailsActivity.class);
                it.putExtra("title", "Class_And_Schedule");
                startActivity(it);
            }
        });
        CardView Membership = findViewById(R.id.Membership);
        Class_And_Schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(BoxActivity.this, GymDetailsActivity.class);
                it.putExtra("title", "Membership");
                startActivity(it);
            }
        });


    }
    }