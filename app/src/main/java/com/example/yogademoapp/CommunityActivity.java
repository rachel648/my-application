package com.example.yogademoapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

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
            Toast.makeText(CommunityActivity.this, "Image 1 clicked", Toast.LENGTH_SHORT).show();
        });

        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click on imageView2
                Toast.makeText(CommunityActivity.this, "Image 2 clicked", Toast.LENGTH_SHORT).show();
            }
        });

        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click on imageView3
                Toast.makeText(CommunityActivity.this, "Image 3 clicked", Toast.LENGTH_SHORT).show();
            }
        });

        imageView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click on imageView4
                Toast.makeText(CommunityActivity.this, "Image 4 clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

