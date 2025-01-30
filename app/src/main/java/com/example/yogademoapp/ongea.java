package com.example.yogademoapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ongea extends AppCompatActivity {

    private EditText groupNameEditText, groupDescriptionEditText, joinGroupIdEditText;
    private Button registerButton, joinButton, showGroupsButton;
    private ListView groupListView;
    private GroupAdapter groupAdapter;
    private ArrayList<Group> groupList;

    private DatabaseReference groupRef;
    private boolean isListVisible = false; // To track visibility

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ongea);

        // Initialize views
        groupNameEditText = findViewById(R.id.groupNameEditText);
        groupDescriptionEditText = findViewById(R.id.groupDescriptionEditText);
        joinGroupIdEditText = findViewById(R.id.joinGroupIdEditText);
        registerButton = findViewById(R.id.registerButton);
        joinButton = findViewById(R.id.joinButton);
        showGroupsButton = findViewById(R.id.showGroupsButton);
        groupListView = findViewById(R.id.groupListView);

        // Firebase reference
        groupRef = FirebaseDatabase.getInstance().getReference("Groups");

        // Initialize list and adapter
        groupList = new ArrayList<>();
        groupAdapter = new GroupAdapter(this, groupList);
        groupListView.setAdapter(groupAdapter);

        // Set click listeners
        registerButton.setOnClickListener(v -> registerGroup());
        joinButton.setOnClickListener(v -> joinGroup());
        showGroupsButton.setOnClickListener(v -> toggleGroupList());
    }

    private void registerGroup() {
        String groupName = groupNameEditText.getText().toString().trim();
        String groupDescription = groupDescriptionEditText.getText().toString().trim();

        if (groupName.isEmpty() || groupDescription.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
        } else {
            String groupId = groupRef.push().getKey();
            Group group = new Group(groupId, groupName, groupDescription);
            groupRef.child(groupId).setValue(group);
            Toast.makeText(this, "Group Registered: " + groupName, Toast.LENGTH_SHORT).show();
        }
    }

    private void joinGroup() {
        String groupId = joinGroupIdEditText.getText().toString().trim();

        if (groupId.isEmpty()) {
            Toast.makeText(this, "Please enter a Group ID", Toast.LENGTH_SHORT).show();
        } else {
            groupRef.child(groupId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        Toast.makeText(ongea.this, "Joined Group!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ongea.this, "Group ID not found!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(ongea.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void toggleGroupList() {
        if (isListVisible) {
            groupListView.setVisibility(View.GONE);
            showGroupsButton.setText("Show Groups");
        } else {
            loadGroups();
            groupListView.setVisibility(View.VISIBLE);
            showGroupsButton.setText("Hide Groups");
        }
        isListVisible = !isListVisible;
    }

    private void loadGroups() {
        groupRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                groupList.clear();
                for (DataSnapshot groupSnapshot : snapshot.getChildren()) {
                    Group group = groupSnapshot.getValue(Group.class);
                    groupList.add(group);
                }
                groupAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ongea.this, "Error loading groups", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
