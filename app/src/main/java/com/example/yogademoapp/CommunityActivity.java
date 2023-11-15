package com.example.yogademoapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class CommunityActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);

        ImageView imageView1 = findViewById(R.id.imageView1);
        ImageView imageView2 = findViewById(R.id.imageView2);
        ImageView imageView3 = findViewById(R.id.imageView3);
        ImageView imageView4 = findViewById(R.id.imageView4);

        imageView1.setOnClickListener(v -> {
            // Handle the click on imageView1
            Intent intent = new Intent(CommunityActivity.this, Types.class);
            startActivity(intent);
        });

        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click on imageView2
                Intent intent = new Intent(CommunityActivity.this, Teens.class);
                startActivity(intent);
            }
        });

        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click on imageView3
                Intent intent = new Intent(CommunityActivity.this, EarlyAdulthood.class);
                startActivity(intent);
            }
        });

        imageView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click on imageView4
                Intent intent = new Intent(CommunityActivity.this, Elderly.class);
                startActivity(intent);
            }
        });

        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CommunityActivity.this, ChooseActivity.class);
                startActivity(intent);
            }
        });
    }
}

