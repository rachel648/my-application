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
        // Add the user's email to the list
        if (userEmail != null) {
            users.add(new UserOne(userEmail, R.drawable.man1));
        }
        // Add more dummy data if necessary
        users.add(new UserOne("user2@example.com", R.drawable.man1));
        users.add(new UserOne("user3@example.com", R.drawable.man1));

        ListView listView = findViewById(R.id.listView);
        CustomAdapter adapter = new CustomAdapter(this, users);
        listView.setAdapter(adapter);



    }
}
