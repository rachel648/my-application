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

        CardView weight12 = findViewById(R.id.weight12);
        weight12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(BoxActivity.this, GymDetailsActivity.class);
                it.putExtra("title", "weight12");
                startActivity(it);
            }
        });
        CardView weight13 = findViewById(R.id.weight13);
        weight13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(BoxActivity.this, GymDetailsActivity.class);
                it.putExtra("title", "weight13");
                startActivity(it);
            }
        });
        CardView weight14 = findViewById(R.id.weight14);
        weight14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(BoxActivity.this, GymDetailsActivity.class);
                it.putExtra("title", "weight14");
                startActivity(it);
            }
        });
        CardView weight15 = findViewById(R.id.weight15);
        weight15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(BoxActivity.this, GymDetailsActivity.class);
                it.putExtra("title", "weight15");
                startActivity(it);
            }
        });
        CardView weight16 = findViewById(R.id.weight16);
        weight16.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(BoxActivity.this, GymDetailsActivity.class);
                it.putExtra("title", "weight16");
                startActivity(it);
            }
        });


    }
}