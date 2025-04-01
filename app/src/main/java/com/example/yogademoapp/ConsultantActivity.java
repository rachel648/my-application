package com.example.yogademoapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.yogademoapp.databinding.ActivityConsultantBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ConsultantActivity extends AppCompatActivity {
    ActivityConsultantBinding binding;
    Button btn;
    ArrayList<User> userArrayList;
    ListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConsultantBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        btn = findViewById(R.id.buttonConsultantDetailback);
        ImageView backButton = findViewById(R.id.backButton);

        userArrayList = new ArrayList<>();
        listAdapter = new ListAdapter(ConsultantActivity.this, userArrayList);
        binding.myListView.setAdapter(listAdapter);

        fetchConsultantDetails();

        binding.myListView.setClickable(true);
        binding.myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                User user = userArrayList.get(position);
                Intent i = new Intent(ConsultantActivity.this, UserActivity.class);
                i.putExtra("name", user.getName());
                i.putExtra("phone", user.getPhoneNo());
                i.putExtra("Experience", user.getExperience());
                i.putExtra("imageid", user.getImageId());
                i.putExtra("imageUrl", user.getImageUrl()); // Add this line
                i.putExtra("fees", user.getFees());
                i.putExtra("GymNumber", user.getGymNumber());
                startActivity(i);
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ConsultantActivity.this, mentaldashboard.class));
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConsultantActivity.this, ChooseActivity.class);
                startActivity(intent);
            }
        });

        // Load hardcoded data only if no data from Firebase
        if (userArrayList.isEmpty()) {
            loadHardcodedData();
        }
    }

    private void fetchConsultantDetails() {
        FirebaseDatabase.getInstance().getReference("Consultants").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Consultant consultant = dataSnapshot.getValue(Consultant.class);
                    if (consultant != null) {
                        int rating = consultant.getRating();
                        String ratingString = generateStars(rating);

                        User user = new User(
                                consultant.name,
                                ratingString,
                                "12:00",
                                consultant.phoneNo,
                                consultant.gymNumber,
                                consultant.experience,
                                consultant.fees,
                                consultant.imageId,
                                rating
                        );

                        // Set the image URL if available
                        if (consultant.getImageUrl() != null && !consultant.getImageUrl().isEmpty()) {
                            user.setImageUrl(consultant.getImageUrl());
                        }

                        userArrayList.add(user);
                    }
                }
                listAdapter.notifyDataSetChanged();

                // If no consultants in Firebase, load hardcoded data
                if (userArrayList.isEmpty()) {
                    loadHardcodedData();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ConsultantActivity.this, "Failed to load consultants: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                // Load hardcoded data if Firebase fails
                loadHardcodedData();
            }
        });
    }

    private String generateStars(int rating) {
        StringBuilder stars = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            if (i < rating) {
                stars.append("⭐");
            } else {
                stars.append("☆");
            }
        }
        return stars.toString();
    }

    private void loadHardcodedData() {
        int[] imageId = {R.drawable.man1, R.drawable.man2, R.drawable.man3, R.drawable.lady2, R.drawable.lady3, R.drawable.lady4, R.drawable.babe3, R.drawable.man4, R.drawable.lady1};

        String[] name = {"Chris\nBones", "Craig\nOmolo", "Mike\nKimathi", "Ray\nMellissa", "Shelmith Nelina", "Zaga llo", "Caroline Odinga", "Dennis chipchip", "Agnes\nBenson"};

        String[] lastMessage = {"Hi", "Let's talk", "How can I help you?", "Hey", "ssup", "Confidential", "Cool", "Need help?", "Friendly"};

        String[] lastMsgTime = {"5:00 pm", "3:00 pm", "7:00 am", "2:00 pm", "12:00 noon", "8:30 pm", "10:00 pm", "11:00 am", "8:00 am", "9:00 pm", "4:00 pm", "5:30 pm"};

        String[] phoneNo = {"0712671173", "0112671077", "0782641193", "0799671773", "0782677173", "0767671183", "0782671479", "0752671178", "0110677170"};

        String[] experience = {"10yrs", "7yrs", "7yrs", "6yrs", "5yrs", "3yrs", "2yrs", "1yrs", "3yrs"};

        String[] fees = {"7000", "6000", "6000", "5500", "5000", "4000", "4700", "3500", "2000"};

        String[] gymNumber = {"ConsultantNo: 07", "ConsultantNo: 03", "ConsultantNo: 10", "ConsultantNo: 06", "ConsultantNo: 05", "ConsultantNo: 16", "ConsultantNo: 3", "ConsultantNo: 14", "ConsultantNo: 14"};

        int[] rating = {5, 4, 3, 2, 1, 1, 3, 4, 5};

        for (int i = 0; i < imageId.length; i++) {
            User user = new User(name[i], lastMessage[i], lastMsgTime[i], phoneNo[i], gymNumber[i], experience[i], fees[i], imageId[i], rating[i]);
            userArrayList.add(user);
        }

        listAdapter.notifyDataSetChanged();
    }
}