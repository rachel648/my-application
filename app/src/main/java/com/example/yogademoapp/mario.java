package com.example.yogademoapp;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.Calendar;

public class mario extends AppCompatActivity {

    // Progress counters
    private int guidedSessions = 0;
    private int personalSessions = 0;
    private int journalReadings = 0;
    private int meditationSessions = 0;

    // Constants
    private static final int MAX_SESSIONS = 5;
    private static final String PREFS_NAME = "YogaProgressPrefs";
    private static final String KEY_GUIDED = "guided_sessions";
    private static final String KEY_PERSONAL = "personal_sessions";
    private static final String KEY_JOURNAL = "journal_readings";
    private static final String KEY_MEDITATION = "meditation_sessions";
    private static final String KEY_LAST_RESET_DAY = "last_reset_day";
    private static final String KEY_CURRENT_MOOD = "current_mood";
    private static final String KEY_BUTTON_X = "button_x";
    private static final String KEY_BUTTON_Y = "button_y";

    // UI Components
    private ProgressBar guidedProgressBar, personalProgressBar, journalProgressBar,
            meditationProgressBar, totalProgressBar;
    private TextView guidedText, personalText, journalText, meditationText, totalProgressText;
    private TextView moodTextView, suggestionTextView;
    private ScrollView scrollView;
    private LinearLayout moodLayout;
    private FloatingActionButton refreshFab;
    private RelativeLayout mainLayout;

    // Drag variables
    private int xDelta, yDelta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mario);

        initializeViews();
        setupButtons();
        initializeMoodIcons();
        setupDraggableRefreshButton();

        loadProgress();
        loadButtonPosition();
    }

    private void initializeViews() {
        mainLayout = findViewById(R.id.mainLayout);
        scrollView = findViewById(R.id.scrollView);

        // Progress Bars
        guidedProgressBar = findViewById(R.id.guidedProgressBar);
        personalProgressBar = findViewById(R.id.personalProgressBar);
        journalProgressBar = findViewById(R.id.journalProgressBar);
        meditationProgressBar = findViewById(R.id.meditationProgressBar);
        totalProgressBar = findViewById(R.id.totalProgressBar);

        // Text Views
        guidedText = findViewById(R.id.guidedText);
        personalText = findViewById(R.id.personalText);
        journalText = findViewById(R.id.journalText);
        meditationText = findViewById(R.id.meditationText);
        totalProgressText = findViewById(R.id.totalProgressText);
        moodTextView = findViewById(R.id.moodTextView);
        suggestionTextView = findViewById(R.id.suggestionTextView);

        // Layouts
        moodLayout = findViewById(R.id.moodLayout);
        refreshFab = findViewById(R.id.refreshFab);
    }

    private void setupButtons() {
        setupIncrementButton(R.id.guidedButton, "guided");
        setupIncrementButton(R.id.personalButton, "personal");
        setupIncrementButton(R.id.journalButton, "journal");
        setupIncrementButton(R.id.meditationButton, "meditation");
    }

    private void setupIncrementButton(int buttonId, String type) {
        Button button = findViewById(buttonId);
        button.setOnClickListener(v -> {
            incrementProgress(type);
            saveProgress();
            updateProgressUI();
        });
    }

    private void setupDraggableRefreshButton() {
        refreshFab.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int X = (int) event.getRawX();
                final int Y = (int) event.getRawY();

                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) refreshFab.getLayoutParams();
                        xDelta = X - lParams.leftMargin;
                        yDelta = Y - lParams.topMargin;
                        break;

                    case MotionEvent.ACTION_MOVE:
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) refreshFab.getLayoutParams();
                        // Keep button within screen bounds
                        layoutParams.leftMargin = Math.max(0, Math.min(X - xDelta, mainLayout.getWidth() - refreshFab.getWidth()));
                        layoutParams.topMargin = Math.max(0, Math.min(Y - yDelta, mainLayout.getHeight() - refreshFab.getHeight()));
                        layoutParams.rightMargin = -250;
                        layoutParams.bottomMargin = -250;
                        refreshFab.setLayoutParams(layoutParams);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Save new position
                        saveButtonPosition();
                        // Check if it was a click (not drag)
                        if (Math.abs(X - xDelta - refreshFab.getLeft()) < 10 &&
                                Math.abs(Y - yDelta - refreshFab.getTop()) < 10) {
                            showResetConfirmationDialog();
                        }
                        break;
                }
                return true;
            }
        });
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
    }

    private void loadProgress() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        guidedSessions = prefs.getInt(KEY_GUIDED, 0);
        personalSessions = prefs.getInt(KEY_PERSONAL, 0);
        journalReadings = prefs.getInt(KEY_JOURNAL, 0);
        meditationSessions = prefs.getInt(KEY_MEDITATION, 0);

        String savedMood = prefs.getString(KEY_CURRENT_MOOD, null);
        if (savedMood != null) {
            setMood(savedMood);
        }

        checkForWeeklyReset();
        updateProgressUI();
    }

    private void loadButtonPosition() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int buttonX = prefs.getInt(KEY_BUTTON_X, -1);
        int buttonY = prefs.getInt(KEY_BUTTON_Y, -1);

        if (buttonX != -1 && buttonY != -1) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) refreshFab.getLayoutParams();
            params.leftMargin = buttonX;
            params.topMargin = buttonY;
            refreshFab.setLayoutParams(params);
        }
    }

    private void saveProgress() {
        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
        editor.putInt(KEY_GUIDED, guidedSessions);
        editor.putInt(KEY_PERSONAL, personalSessions);
        editor.putInt(KEY_JOURNAL, journalReadings);
        editor.putInt(KEY_MEDITATION, meditationSessions);
        editor.apply();
    }

    private void saveButtonPosition() {
        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
        editor.putInt(KEY_BUTTON_X, refreshFab.getLeft());
        editor.putInt(KEY_BUTTON_Y, refreshFab.getTop());
        editor.apply();
    }

    private void updateProgressUI() {
        int guidedProgress = (int) ((double) guidedSessions / MAX_SESSIONS * 100);
        int personalProgress = (int) ((double) personalSessions / MAX_SESSIONS * 100);
        int journalProgress = (int) ((double) journalReadings / MAX_SESSIONS * 100);
        int meditationProgress = (int) ((double) meditationSessions / MAX_SESSIONS * 100);

        int totalProgress = (guidedProgress + personalProgress + journalProgress + meditationProgress) / 4;

        guidedProgressBar.setProgress(guidedProgress);
        personalProgressBar.setProgress(personalProgress);
        journalProgressBar.setProgress(journalProgress);
        meditationProgressBar.setProgress(meditationProgress);
        totalProgressBar.setProgress(totalProgress);

        guidedText.setText(String.format("Guided Sessions: %d/%d", guidedSessions, MAX_SESSIONS));
        personalText.setText(String.format("Personal Sessions: %d/%d", personalSessions, MAX_SESSIONS));
        journalText.setText(String.format("Journal Readings: %d/%d", journalReadings, MAX_SESSIONS));
        meditationText.setText(String.format("Meditation Sessions: %d/%d", meditationSessions, MAX_SESSIONS));
        totalProgressText.setText(String.format("Total Progress: %d%%", totalProgress));

        if (totalProgress == 100) {
            showMoodFollowUpDialog();
        }
    }

    private void showResetConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Reset Progress")
                .setMessage("Are you sure you want to reset all progress?")
                .setPositiveButton("Yes", (dialog, which) -> resetAllProgress())
                .setNegativeButton("No", null)
                .show();
    }

    private void resetAllProgress() {
        guidedSessions = 0;
        personalSessions = 0;
        journalReadings = 0;
        meditationSessions = 0;

        saveProgress();
        updateProgressUI();

        // Reset weekly tracker
        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
        editor.putInt(KEY_LAST_RESET_DAY, -1);
        editor.apply();
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
            moodImage.setOnClickListener(v -> {
                setMood(mood);
                saveMood(mood);
            });
            moodLayout.addView(moodImage);
        }
    }

    private void setMood(String mood) {
        moodTextView.setText("Today's Mood: " + mood);
        suggestionTextView.setText(getMoodSuggestion(mood));
    }

    private void saveMood(String mood) {
        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
        editor.putString(KEY_CURRENT_MOOD, mood);
        editor.apply();
    }

    private String getMoodSuggestion(String mood) {
        switch (mood) {
            case "😊": return "Keep spreading positivity!";
            case "😢": return "Take some time to reflect and relax.";
            case "😡": return "Consider deep breaths to calm down.";
            case "😴": return "A quick nap might help refresh you.";
            case "😎": return "Enjoy your confidence today!";
            default: return "Stay positive!";
        }
    }

    private void checkForWeeklyReset() {
        Calendar calendar = Calendar.getInstance();
        int currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int lastResetDay = prefs.getInt(KEY_LAST_RESET_DAY, -1);

        if (lastResetDay != currentDayOfWeek && currentDayOfWeek == Calendar.SUNDAY) {
            resetAllProgress();
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt(KEY_LAST_RESET_DAY, currentDayOfWeek);
            editor.apply();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveButtonPosition();
    }
}