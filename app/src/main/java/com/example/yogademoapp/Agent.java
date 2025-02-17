package com.example.yogademoapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.AdapterView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class Agent extends AppCompatActivity {

    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent);

        // Get the username and email from the intent
        String username = getIntent().getStringExtra("username");
        String userEmail = getIntent().getStringExtra("userEmail");

        List<UserOne> users = new ArrayList<>();
        // Add the consultant's details if available
        if (username != null && userEmail != null) {
            users.add(new UserOne(username, userEmail, R.drawable.man1));  // Example user image
        }

        ListView listView = findViewById(R.id.listView);
        CustomAdapter adapter = new CustomAdapter(this, users);
        listView.setAdapter(adapter);

        // Set item click listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parentView, android.view.View view, int position, long id) {
                // Get the selected user
                UserOne selectedUser = users.get(position);

                // Create an intent to navigate to Accept.java
                Intent intent = new Intent(Agent.this, accept.class);

                // Pass only the upper layout details (name, email, and image)
                intent.putExtra("consultantName", selectedUser.getUsername());
               intent.putExtra("consultantEmail", selectedUser.getUserEmail());
               intent.putExtra("consultantImage", selectedUser.getImageResourceId());

                // Start the Accept activity
                startActivity(intent);
            }
        });
    }
}
