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

        List<UserOne> users = new ArrayList<>();
        // Add some dummy data
        users.add(new UserOne("user1@example.com", R.drawable.man1));
        users.add(new UserOne("user2@example.com", R.drawable.man1));
        users.add(new UserOne("user3@example.com", R.drawable.man1));

        ListView listView = findViewById(R.id.listView);
        CustomAdapter adapter = new CustomAdapter(this, users);
        listView.setAdapter(adapter);
    }
}
