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

        CardView exit = findViewById(R.id.Boxback); //writing functinality of back(boxback)
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        CardView cycle = findViewById(R.id.Instructors);
        cycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(BoxActivity.this, GymDetailsActivity.class);
                it.putExtra("title", "Get_An_Instructor");
                startActivity(it);
            }
        });
        CardView Box = findViewById(R.id.Box);
        Box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(BoxActivity.this, GymDetailsActivity.class);
                it.putExtra("title", "Box_Training_Available");
                startActivity(it);
            }
        });
        CardView Weight = findViewById(R.id.Weight);
        Weight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(BoxActivity.this, GymDetailsActivity.class);
                it.putExtra("title", "WeightLift_With_Us");
                startActivity(it);
            }
        });
        CardView Aerobics = findViewById(R.id.Aerobics);
        Aerobics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(BoxActivity.this, GymDetailsActivity.class);
                it.putExtra("title", "Aerobics");
                startActivity(it);
            }
        });
        CardView Explore = findViewById(R.id.Explore);
        Explore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(BoxActivity.this, GymDetailsActivity.class);
                it.putExtra("title", "Explore_Outside");
                startActivity(it);
            }
        });

    }
}