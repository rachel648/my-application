package com.example.yogademoapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Types extends AppCompatActivity {

    private RadioGroup ageGroupRadioGroup;
    private RadioGroup timeDateRadioGroup;
    private RadioGroup locationRadioGroup;
    private TextView groupLeaderText;
    private Button joinButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_types);

        ageGroupRadioGroup = findViewById(R.id.ageGroupRadioGroup);
        timeDateRadioGroup = findViewById(R.id.timeDateRadioGroup);
        locationRadioGroup = findViewById(R.id.locationRadioGroup);
        groupLeaderText = findViewById(R.id.groupLeaderText);
        joinButton = findViewById(R.id.joinButton);

        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedAgeId = ageGroupRadioGroup.getCheckedRadioButtonId();
                int selectedTimeId = timeDateRadioGroup.getCheckedRadioButtonId();
                int selectedLocationId = locationRadioGroup.getCheckedRadioButtonId();

                if (selectedAgeId == -1 || selectedTimeId == -1 || selectedLocationId == -1) {
                    groupLeaderText.setText("Please select an age group, a time/date, and a location.");
                } else {
                    RadioButton ageRadioButton = findViewById(selectedAgeId);
                    String selectedAgeGroup = ageRadioButton.getText().toString();

                    RadioButton timeRadioButton = findViewById(selectedTimeId);
                    String selectedTimeDate = timeRadioButton.getText().toString();

                    RadioButton locationRadioButton = findViewById(selectedLocationId);
                    String selectedLocation = locationRadioButton.getText().toString();

                    String groupLeader = getGroupLeader(selectedAgeGroup);
                    groupLeaderText.setText("Group Leader: " + groupLeader + "\nTime/Date: " + selectedTimeDate + "\nLocation: " + selectedLocation);

                    // Start the new activity
                    Intent intent = new Intent(Types.this, CartBook.class);
                    intent.putExtra("ageGroup", selectedAgeGroup);
                    intent.putExtra("timeDate", selectedTimeDate);
                    intent.putExtra("location", selectedLocation);
                    startActivity(intent);
                }
            }
        });

        ImageView backButton = findViewById(R.id.backButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Types.this, ChooseActivity.class);
                startActivity(intent);
            }
        });
    }

    private String getGroupLeader(String ageGroup) {
        // Implement logic to get the group leader for the selected age group
        // You can use a switch statement or other logic here
        if (ageGroup.equals("8-10")) {
            return "Group Leader: Alex";
        } else if (ageGroup.equals("12-15")) {
            return "Group Leader: Grace";
        } else if (ageGroup.equals("16-19")) {
            return "Group Leader: Ray";
        } else {
            return "Unknown Group Leader";
        }


    }
}
