package com.example.yogademoapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class mario extends AppCompatActivity {

    private int guidedSessions = 0;
    private int personalSessions = 0;
    private int journalReadings = 0;
    private int meditationSessions = 0;

    private static final int MAX_SESSIONS = 5;

    private ProgressBar guidedProgressBar, personalProgressBar, journalProgressBar, meditationProgressBar, totalProgressBar;
    private TextView guidedText, personalText, journalText, meditationText, totalProgressText;

    private TextView moodTextView, suggestionTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mario);

        guidedProgressBar = findViewById(R.id.guidedProgressBar);
        personalProgressBar = findViewById(R.id.personalProgressBar);
        journalProgressBar = findViewById(R.id.journalProgressBar);
        meditationProgressBar = findViewById(R.id.meditationProgressBar);
        totalProgressBar = findViewById(R.id.totalProgressBar);

        guidedText = findViewById(R.id.guidedText);
        personalText = findViewById(R.id.personalText);
        journalText = findViewById(R.id.journalText);
        meditationText = findViewById(R.id.meditationText);
        totalProgressText = findViewById(R.id.totalProgressText);

        setupIncrementButton(R.id.guidedButton, "guided");
        setupIncrementButton(R.id.personalButton, "personal");
        setupIncrementButton(R.id.journalButton, "journal");
        setupIncrementButton(R.id.meditationButton, "meditation");

        moodTextView = findViewById(R.id.moodTextView);
        suggestionTextView = findViewById(R.id.suggestionTextView);

        // Define mood emojis and their corresponding images
        final String[] moods = {"😊", "😢", "😡", "😴", "😎"};
        int[] moodImages = {R.drawable.smile, R.drawable.sad, R.drawable.angry, R.drawable.sleep, R.drawable.cool};

        // Initialize mood icons dynamically
        LinearLayout moodLayout = findViewById(R.id.moodLayout);
        for (int i = 0; i < moods.length; i++) {
            final String mood = moods[i];
            ImageView moodImage = new ImageView(this);
            moodImage.setImageResource(moodImages[i]);
            moodImage.setLayoutParams(new LinearLayout.LayoutParams(200, 200));
            moodImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setMood(mood);
                }
            });
            moodLayout.addView(moodImage);
        }

        updateProgress();
    }

    private void setupIncrementButton(int buttonId, String type) {
        Button button = findViewById(buttonId);
        button.setOnClickListener(v -> incrementProgress(type));
    }

    private void incrementProgress(String type) {
        switch (type) {
            case "guided":
                if (guidedSessions < MAX_SESSIONS) guidedSessions++;
                break;
            case "personal":
                if (personalSessions < MAX_SESSIONS) personalSessions++;
                break;
            case "journal":
                if (journalReadings < MAX_SESSIONS) journalReadings++;
                break;
            case "meditation":
                if (meditationSessions < MAX_SESSIONS) meditationSessions++;
                break;
        }
        updateProgress();
    }

    private void updateProgress() {
        double guidedProgress = (double) guidedSessions / MAX_SESSIONS;
        double personalProgress = (double) personalSessions / MAX_SESSIONS;
        double journalProgress = (double) journalReadings / MAX_SESSIONS;
        double meditationProgress = (double) meditationSessions / MAX_SESSIONS;

        double totalProgress = (guidedProgress + personalProgress + journalProgress + meditationProgress) / 4;

        guidedProgressBar.setProgress((int) (guidedProgress * 100));
        personalProgressBar.setProgress((int) (personalProgress * 100));
        journalProgressBar.setProgress((int) (journalProgress * 100));
        meditationProgressBar.setProgress((int) (meditationProgress * 100));
        totalProgressBar.setProgress((int) (totalProgress * 100));

        guidedText.setText("Guided Sessions: " + guidedSessions + " / " + MAX_SESSIONS);
        personalText.setText("Personal Sessions: " + personalSessions + " / " + MAX_SESSIONS);
        journalText.setText("Journal Readings: " + journalReadings + " / " + MAX_SESSIONS);
        meditationText.setText("Meditation Sessions: " + meditationSessions + " / " + MAX_SESSIONS);
        totalProgressText.setText("Total Progress: " + (int) (totalProgress * 100) + "%");
    }

    // Function to set mood and suggestion
    private void setMood(String mood) {
        // Mood tracking fields
        moodTextView.setText("Today's Mood: " + mood);
        suggestionTextView.setText(getMoodSuggestion(mood));

    }

    // Function to get mood suggestion
    private String getMoodSuggestion(String mood) {
        switch (mood) {
            case "😊":
                return "Keep spreading positivity!";
            case "😢":
                return "Take some time to reflect and relax.";
            case "😡":
                return "Consider deep breaths to calm down.";
            case "😴":
                return "A quick nap might help refresh you.";
            case "😎":
                return "Enjoy your confidence today!";
            default:
                return "Stay positive!";
        }
    }
}
