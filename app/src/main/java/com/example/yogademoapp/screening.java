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
    private RadioGroup question1, question2, question3, question4, question5, question6, question7, question8;
    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screening);

        // Initialize UI elements
        question1 = findViewById(R.id.question1); // Sadness
        question2 = findViewById(R.id.question2); // Anxiety
        question3 = findViewById(R.id.question3); // Self-Esteem
        question4 = findViewById(R.id.question4); // Eating Habits
        question5 = findViewById(R.id.question5); // Drug Use
        question6 = findViewById(R.id.question6); // Sleeping Patterns
        question7 = findViewById(R.id.question7); // Concentration Issues
        question8 = findViewById(R.id.question8); // Social Withdrawal

        btnSubmit = findViewById(R.id.btnSubmit);

        // Set up button click listener
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                analyzeResults();
            }
        });
    }

    // Analyze results based on scores
    private void analyzeResults() {
        int depressionScore = getSelectedScore(question1) + getSelectedScore(question6) + getSelectedScore(question8);
        int anxietyScore = getSelectedScore(question2) + getSelectedScore(question6) + getSelectedScore(question7);
        int selfEsteemScore = getSelectedScore(question3);
        int eatingDisorderScore = getSelectedScore(question4);
        int drugAbuseScore = getSelectedScore(question5);

        // Determine the highest scoring mental health condition
        if (depressionScore >= 3) {
            showToastAndNavigate("Signs of Depression detected.", mentaldashboard.class);
        } else if (anxietyScore >= 3) {
            showToastAndNavigate("Signs of Anxiety Disorder detected.", mentaldashboard.class);
        } else if (selfEsteemScore >= 3) {
            showToastAndNavigate("Potential signs of Low Self-Esteem detected.", mentaldashboard.class);
        } else if (eatingDisorderScore >= 3) {
            showToastAndNavigate("Potential signs of an Eating Disorder detected.", mentaldashboard.class);
        } else if (drugAbuseScore >= 3) {
            showToastAndNavigate("Signs of Substance Abuse detected.", mentaldashboard.class);
        } else {
            showToastAndNavigate("No major concerns detected. Proceeding...", mentaldashboard.class);
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
