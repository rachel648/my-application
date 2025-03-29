package com.example.yogademoapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class screening extends AppCompatActivity {
    private RadioGroup question1, question2, question3;
    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screening);

        // Initialize UI elements
        question1 = findViewById(R.id.question1);
        question2 = findViewById(R.id.question2);
        question3 = findViewById(R.id.question3);
        btnSubmit = findViewById(R.id.btnSubmit);

        // Set up button click listener
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                analyzeResults();
            }
        });
    }

    // Analyze results based on the selected options
    private void analyzeResults() {
        int depressionScore = getSelectedScore(question1);  // Question 1 focuses on sadness
        int anxietyScore = getSelectedScore(question2);     // Question 2 focuses on sleep/concentration
        int generalWellnessScore = getSelectedScore(question3); // Question 3 focuses on interest loss

        // Check if any mental health concern is detected
        if (depressionScore >= 2 || anxietyScore >= 2 || generalWellnessScore >= 2) {
            showToastAndNavigate("Signs of mental health concerns detected. Redirecting...", mentaldashboard.class);
        } else {
            showToastAndNavigate("No major concerns detected. Proceeding...", GreenCard.class);
        }
    }

    // Helper method to get the selected score from a RadioGroup
    private int getSelectedScore(RadioGroup radioGroup) {
        int selectedId = radioGroup.getCheckedRadioButtonId();
        if (selectedId == -1) {
            return 0; // Default score if no selection
        }
        RadioButton selectedButton = findViewById(selectedId);
        Object tagObject = selectedButton.getTag();

        if (tagObject != null) {
            try {
                return Integer.parseInt(tagObject.toString());
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    // Helper method to show a toast and navigate to an activity
    private void showToastAndNavigate(String message, Class<?> destination) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, destination));
        finish();
    }
}
