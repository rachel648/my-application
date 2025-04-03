package com.example.yogademoapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

                SharedPreferences sharedPreferences = getSharedPreferences("UserProfile", MODE_PRIVATE);
                int sessionIndex = sharedPreferences.getInt("currentSessionIndex", -1);

                if (sessionIndex == -1) {
                    sessionIndex = findAvailableSession(sharedPreferences);
                    if (sessionIndex == -1) {
                        showMaxSessionsDialog();
                        return;
                    }
                }

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("updatedFee", user.getFees());
                editor.putString("selectedConsultantName", user.getName());
                editor.putInt("updatedSessionIndex", sessionIndex);
                editor.putInt("lastBookedSession", sessionIndex);
                editor.apply();

                Intent i = new Intent(ConsultantActivity.this, UserActivity.class);
                i.putExtra("name", user.getName());
                i.putExtra("phone", user.getPhoneNo());
                i.putExtra("Experience", user.getExperience());
                i.putExtra("imageid", user.getImageId());
                i.putExtra("imageUrl", user.getImageUrl());
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
                finish();
            }
        });

        if (userArrayList.isEmpty()) {
            loadHardcodedData();
        }
    }

    private void fetchConsultantDetails() {
        FirebaseDatabase.getInstance().getReference("Consultants")
                .orderByChild("timestamp")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        userArrayList.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Consultant consultant = dataSnapshot.getValue(Consultant.class);
                            if (consultant != null) {
                                // Generate random experience between 1 and 15 years
                                int experienceYears = 1 + (int)(Math.random() * 15);
                                String experience = experienceYears + "yrs";

                                // Calculate fee based on experience (minimum 2000)
                                int baseFee = 2000 + (experienceYears * 300);
                                int feeVariation = (int)(baseFee * 0.2 * (Math.random() > 0.5 ? 1 : -1));
                                int fee = Math.max(baseFee + feeVariation, 2000);
                                String fees = String.valueOf(fee);

                                // Generate random phone number (Kenyan format)
                                String phoneNo = "07" + (10000000 + (int)(Math.random() * 90000000));

                                // Generate random gym number between 1-20
                                String gymNumber = "ConsultantNo: " + (1 + (int)(Math.random() * 20));

                                int rating = consultant.getRating();
                                String ratingString = generateStars(rating);

                                User user = new User(
                                        consultant.name,
                                        ratingString,
                                        generateRandomAvailability(),
                                        phoneNo,
                                        gymNumber,
                                        experience,
                                        fees,
                                        consultant.imageId,
                                        rating,
                                        consultant.getImageUrl()
                                );

                                userArrayList.add(0, user);
                            }
                        }
                        listAdapter.notifyDataSetChanged();

                        if (userArrayList.isEmpty()) {
                            loadHardcodedData();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(ConsultantActivity.this,
                                "Failed to load consultants: " + error.getMessage(),
                                Toast.LENGTH_SHORT).show();
                        loadHardcodedData();
                    }
                });
    }

    private String generateRandomAvailability() {
        int hour = 8 + (int)(Math.random() * 10); // 8am-6pm
        int minute = (int)(Math.random() * 12) * 5; // 0,5,10...55
        String period = hour < 12 ? "am" : "pm";
        return "\n"+"\n" +"\n"+ hour + ":" + (minute < 10 ? "0" + minute : minute) + " " + period;

    }

    private int findAvailableSession(SharedPreferences sharedPreferences) {
        for (int i = 0; i < 5; i++) {
            String fee = sharedPreferences.getString("session" + i + "Fee", i == 0 ? "5000" : "0");
            if (fee.equals("0")) {
                return i;
            }
        }
        return -1;
    }

    private void showMaxSessionsDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Maximum Sessions Reached")
                .setMessage("You've already booked all 5 available sessions.")
                .setPositiveButton("OK", null)
                .show();
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
        int[] imageId = {R.drawable.man1, R.drawable.man2, R.drawable.man3, R.drawable.lady2,
                R.drawable.lady3, R.drawable.lady4, R.drawable.babe3, R.drawable.man4, R.drawable.lady1};
        String[] name = {"Chris\nBones", "Craig\nOmolo", "Mike\nKimathi", "Ray\nMellissa",
                "Shelmith Nelina", "Zaga llo", "Caroline Odinga", "Dennis chipchip", "Agnes\nBenson"};

        // Generate random data for hardcoded consultants
        for (int i = 0; i < imageId.length; i++) {
            int experienceYears = 1 + (int)(Math.random() * 15);
            String experience = experienceYears + "yrs";

            int baseFee = 2000 + (experienceYears * 300);
            int feeVariation = (int)(baseFee * 0.2 * (Math.random() > 0.5 ? 1 : -1));
            int fee = Math.max(baseFee + feeVariation, 2000);
            String fees = String.valueOf(fee);

            String phoneNo = "07" + (10000000 + (int)(Math.random() * 90000000));
            String gymNumber = "ConsultantNo: " + (1 + (int)(Math.random() * 20));
            int rating = 1 + (int)(Math.random() * 5);

            User user = new User(
                    name[i],
                    generateStars(rating),
                    generateRandomAvailability(),
                    phoneNo,
                    gymNumber,
                    experience,
                    fees,
                    imageId[i],
                    rating,
                    "" // Empty image URL for hardcoded data
            );
            userArrayList.add(user);
        }

        listAdapter.notifyDataSetChanged();
    }
}