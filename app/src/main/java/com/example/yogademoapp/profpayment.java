package com.example.yogademoapp;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class profpayment extends AppCompatActivity {

    TextView sessionOnePlusSign;
    LinearLayout dynamicSessionsLayout;
    TextView addSessionPlusSign, addSessionMinusSign;

    int sessionCount = 1;  // Starting with session one already present

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profpayment);

        sessionOnePlusSign = findViewById(R.id.tvSessionOne);
        dynamicSessionsLayout = findViewById(R.id.dynamicSessionsLayout);
        addSessionPlusSign = findViewById(R.id.addSessionPlusSign);
        addSessionMinusSign = findViewById(R.id.addSessionMinusSign);

        // Set initial session value
        sessionOnePlusSign.setText("Session One: ksh 0.00");

        // Define the scale animation
        final ScaleAnimation scaleAnimation = new ScaleAnimation(
                1f, 1.2f, // Scale from 100% to 120%
                1f, 1.2f, // Scale from 100% to 120%
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f, // Pivot point at the center
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f  // Pivot point at the center
        );
        scaleAnimation.setDuration(150); // Duration of the animation
        scaleAnimation.setRepeatCount(0); // No repetition
        scaleAnimation.setFillAfter(true); // Retain the final state after animation

        // Handle the click event for adding more sessions
        addSessionPlusSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the scale animation
                v.startAnimation(scaleAnimation);

                if (sessionCount < 5) {
                    addSession(sessionCount + 1);
                    sessionCount++;
                }

                // Disable the button if the maximum sessions (5) have been added
                if (sessionCount == 5) {
                    addSessionPlusSign.setEnabled(false);  // Disable the + button
                }
            }
        });

        // Handle the click event for removing sessions
        addSessionMinusSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the scale animation
                v.startAnimation(scaleAnimation);

                if (sessionCount > 1) {  // Ensure that at least one session remains
                    removeSession();
                    sessionCount--;
                }

                // Enable the + button if less than 5 sessions exist
                if (sessionCount < 5) {
                    addSessionPlusSign.setEnabled(true);  // Re-enable the + button
                }
            }
        });
    }

    private void addSession(int sessionNumber) {
        // Create a new session TextView dynamically
        TextView newSession = new TextView(this);
        newSession.setText("Session " + sessionNumber + ": ksh 0.00");
        newSession.setTextSize(18);
        newSession.setTextColor(getResources().getColor(R.color.white));  // Set color for text
        newSession.setGravity(Gravity.CENTER);  // Center the text within the TextView
        newSession.setPadding(0, 10, 0, 10);  // Add some padding

        // Create a gradient background programmatically with colors that go well with green
        GradientDrawable gradient = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{
                        getResources().getColor(R.color.green_light),  // Light green
                        getResources().getColor(R.color.green_dark)   // Darker green
                }
        );
        gradient.setCornerRadius(10); // Set rounded corners

        newSession.setBackground(gradient);

        // Set layout parameters for centering the session within the LinearLayout
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,  // Width set to match parent
                LinearLayout.LayoutParams.WRAP_CONTENT); // Height set to wrap content
        params.setMargins(20, 10, 20, 10);  // Add margins for spacing between sessions
        params.gravity = Gravity.CENTER_HORIZONTAL;  // Center horizontally

        newSession.setLayoutParams(params);

        // Adding fade-in animation to the new session
        AlphaAnimation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setDuration(500);
        newSession.startAnimation(fadeIn);

        // Add the new session to the dynamic session layout
        dynamicSessionsLayout.addView(newSession);
    }

    private void removeSession() {
        // Remove the last session added with an animation
        if (dynamicSessionsLayout.getChildCount() > 0) {
            TextView lastSession = (TextView) dynamicSessionsLayout.getChildAt(dynamicSessionsLayout.getChildCount() - 1);
            AlphaAnimation fadeOut = new AlphaAnimation(1, 0);
            fadeOut.setDuration(500);
            lastSession.startAnimation(fadeOut);

            // Wait for the animation to complete before removing the view
            lastSession.postDelayed(new Runnable() {
                @Override
                public void run() {
                    dynamicSessionsLayout.removeViewAt(dynamicSessionsLayout.getChildCount() - 1);
                }
            }, 500);
        }
    }
}

// pop