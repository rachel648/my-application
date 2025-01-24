package com.example.yogademoapp;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.Calendar;

public class mario extends AppCompatActivity {

    private int guidedSessions = 0;
    private int personalSessions = 0;
    private int journalReadings = 0;
    private int meditationSessions = 0;

    private static final int MAX_SESSIONS = 5;

    private ProgressBar guidedProgressBar, personalProgressBar, journalProgressBar, meditationProgressBar, totalProgressBar;
    private TextView guidedText, personalText, journalText, meditationText, totalProgressText;

    private TextView moodTextView, suggestionTextView;
    private ScrollView scrollView;
    private LinearLayout moodLayout;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mario);

        databaseReference = FirebaseDatabase.getInstance().getReference("YogaSessions");

        // Initialize views and buttons
        scrollView = findViewById(R.id.scrollView);
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
        moodLayout = findViewById(R.id.moodLayout);

        initializeMoodIcons();
        checkForWeeklyReset();

        // Load data when activity starts
        loadDataFromFirebase();

        updateProgress(); // Update the progress initially as well
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveToFirebase();  // Save data to Firebase when the activity is paused
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDataFromFirebase();  // Ensure data is loaded from Firebase when the activity is resumed
    }

    private void loadDataFromFirebase() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                SessionData data = dataSnapshot.getValue(SessionData.class);
                if (data != null) {
                    guidedSessions = data.guidedSessions;
                    personalSessions = data.personalSessions;
                    journalReadings = data.journalReadings;
                    meditationSessions = data.meditationSessions;
                    updateProgress(); // Update UI with the retrieved data
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors
            }
        });
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
        saveToFirebase();
        updateProgress();
    }

    private void saveToFirebase() {
        String sessionId = databaseReference.push().getKey(); // Automatically generates a unique ID
        SessionData sessionData = new SessionData(guidedSessions, personalSessions, journalReadings, meditationSessions);
        if (sessionId != null) {
            databaseReference.child(sessionId).setValue(sessionData); // Save under a unique session ID
        }
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

        if ((int) (totalProgress * 100) == 100) {
            showMoodFollowUpDialog();
        }
    }

    private void showMoodFollowUpDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Mood Follow-Up")
                .setMessage("Would you like to follow up on your mood?")
                .setPositiveButton("Yes", (dialog, which) -> scrollToMoodSection())
                .setNegativeButton("No", null)
                .show();
    }

    private void scrollToMoodSection() {
        scrollView.post(() -> scrollView.smoothScrollTo(0, moodLayout.getTop()));
    }

    private void initializeMoodIcons() {
        final String[] moods = {"😊", "😢", "😡", "😴", "😎"};
        int[] moodImages = {R.drawable.smile, R.drawable.sad, R.drawable.angry, R.drawable.sleep, R.drawable.cool};

        for (int i = 0; i < moods.length; i++) {
            final String mood = moods[i];
            ImageView moodImage = new ImageView(this);
            moodImage.setImageResource(moodImages[i]);
            moodImage.setLayoutParams(new LinearLayout.LayoutParams(200, 200));
            moodImage.setOnClickListener(v -> setMood(mood));
            moodLayout.addView(moodImage);
        }
    }

    private void setMood(String mood) {
        moodTextView.setText("Today's Mood: " + mood);
        suggestionTextView.setText(getMoodSuggestion(mood));
    }

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

    private void checkForWeeklyReset() {
        Calendar calendar = Calendar.getInstance();
        int currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        if (currentDayOfWeek == Calendar.SUNDAY) {
            resetProgress();
        }
    }

    private void resetProgress() {
        guidedSessions = 0;
        personalSessions = 0;
        journalReadings = 0;
        meditationSessions = 0;
        saveToFirebase();
        updateProgress();
    }

    private static class SessionData {
        public int guidedSessions, personalSessions, journalReadings, meditationSessions;

        public SessionData(int guided, int personal, int journal, int meditation) {
            this.guidedSessions = guided;
            this.personalSessions = personal;
            this.journalReadings = journal;
            this.meditationSessions = meditation;
        }
    }
}
