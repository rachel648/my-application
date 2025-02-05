package com.example.yogademoapp;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class Agent extends AppCompatActivity {

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
            users.add(new UserOne(username, userEmail, R.drawable.man1));
        }

        ListView listView = findViewById(R.id.listView);
        CustomAdapter adapter = new CustomAdapter(this, users);
        listView.setAdapter(adapter);
    }
}
