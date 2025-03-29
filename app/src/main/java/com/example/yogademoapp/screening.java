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
    private RadioGroup question1, question2, question3, question4, question5, question6;
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

        btnSubmit = findViewById(R.id.btnSubmit);

        // Set up button click listener
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                analyzeResults();
            }
        });
    }

    // Analyze results and display only the most probable condition
    private void analyzeResults() {
        int depressionScore = 0;
        int anxietyScore = 0;
        int selfEsteemScore = 0;
        int eatingDisorderScore = 0;
        int drugAbuseScore = 0;

        // Assign scores to conditions based on selections
        int q1 = getSelectedScore(question1); // Sadness
        int q2 = getSelectedScore(question2); // Anxiety
        int q3 = getSelectedScore(question3); // Self-Esteem
        int q4 = getSelectedScore(question4); // Eating Habits
        int q5 = getSelectedScore(question5); // Drug Use
        int q6 = getSelectedScore(question6); // Sleeping Patterns

        // Adjusted scoring system for better diagnosis
        depressionScore = q1 + q3 + q6;
        anxietyScore = q2 + q4 + q6;
        selfEsteemScore = q3 + q1 + q5;
        eatingDisorderScore = q4 + q2 + q6;
        drugAbuseScore = q5 + q3 + q6;

        // Find the condition with the highest score
        String highestCondition = "✅ No major mental health concerns detected.\nKeep maintaining a healthy lifestyle!";
        int highestScore = 3; // Minimum threshold for diagnosis

        if (depressionScore > highestScore) {
            highestCondition = "🔵 You may be experiencing **Depression**.\nConsider speaking to a mental health professional.";
            highestScore = depressionScore;
        }
        if (anxietyScore > highestScore) {
            highestCondition = "🟠 You may have **Anxiety Disorder**.\nProfessional guidance is recommended.";
            highestScore = anxietyScore;
        }
        if (selfEsteemScore > highestScore) {
            highestCondition = "🟢 You may have **Low Self-Esteem**.\nConsider working on self-confidence and seeking support.";
            highestScore = selfEsteemScore;
        }
        if (eatingDisorderScore > highestScore) {
            highestCondition = "🔴 You may have an **Eating Disorder**.\nIt’s important to consult a healthcare professional.";
            highestScore = eatingDisorderScore;
        }
        if (drugAbuseScore > highestScore) {
            highestCondition = "⚠️ Possible **Substance Abuse Issue** detected.\nSeeking help could be beneficial.";
            highestScore = drugAbuseScore;
        }

        // Show toast and navigate
        showToastAndNavigate(highestCondition, mentaldashboard.class);
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
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        startActivity(new Intent(this, destination));
        finish();
    }
}
