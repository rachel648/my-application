package com.example.yogademoapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.animation.AlphaAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class profpayment extends AppCompatActivity {

    TextView sessionOnePlusSign, emailTextView;
    ImageView profileImageView;
    LinearLayout dynamicSessionsLayout;
    TextView addSessionPlusSign, addSessionMinusSign;

    int sessionCount = 1; // Starting with session one already present

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profpayment);

        sessionOnePlusSign = findViewById(R.id.tvSessionOne);
        dynamicSessionsLayout = findViewById(R.id.dynamicSessionsLayout);
        addSessionPlusSign = findViewById(R.id.addSessionPlusSign);
        addSessionMinusSign = findViewById(R.id.addSessionMinusSign);
        emailTextView = findViewById(R.id.textviewemail);
        profileImageView = findViewById(R.id.imageView);

        // Retrieve Email
        String email = getIntent().getStringExtra("EMAIL");
        if (email != null) {
            emailTextView.setText(email);
        }

        // Retrieve Profile Image
        byte[] imageBytes = getIntent().getByteArrayExtra("PROFILE_IMAGE");
        if (imageBytes != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            profileImageView.setImageBitmap(bitmap);
        }

        // Set initial session value
        sessionOnePlusSign.setText("Session One: ksh 0.00");

        // Define the scale animation
        final ScaleAnimation scaleAnimation = new ScaleAnimation(
                1f, 1.2f, 1f, 1.2f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f
        );
        scaleAnimation.setDuration(150);
        scaleAnimation.setRepeatCount(0);
        scaleAnimation.setFillAfter(true);

        // Handle the click event for adding more sessions
        addSessionPlusSign.setOnClickListener(v -> {
            v.startAnimation(scaleAnimation);

            if (sessionCount < 5) {
                addSession(sessionCount + 1);
                sessionCount++;
            }

            if (sessionCount == 5) {
                addSessionPlusSign.setEnabled(false);
            }
        });

        // Handle the click event for removing sessions
        addSessionMinusSign.setOnClickListener(v -> {
            v.startAnimation(scaleAnimation);

            if (sessionCount > 1) {
                removeSession();
                sessionCount--;
            }

            if (sessionCount < 5) {
                addSessionPlusSign.setEnabled(true);
            }
        });
    }

    private void addSession(int sessionNumber) {
        TextView newSession = new TextView(this);
        newSession.setText("Session " + sessionNumber + ": ksh 0.00");
        newSession.setTextSize(18);
        newSession.setTextColor(getResources().getColor(R.color.white));
        newSession.setGravity(Gravity.CENTER);
        newSession.setPadding(0, 10, 0, 10);

        GradientDrawable gradient = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{
                        getResources().getColor(R.color.green_light),
                        getResources().getColor(R.color.green_dark)
                }
        );
        gradient.setCornerRadius(10);
        newSession.setBackground(gradient);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(20, 10, 20, 10);
        params.gravity = Gravity.CENTER_HORIZONTAL;

        newSession.setLayoutParams(params);

        AlphaAnimation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setDuration(500);
        newSession.startAnimation(fadeIn);

        dynamicSessionsLayout.addView(newSession);
    }

    private void removeSession() {
        if (dynamicSessionsLayout.getChildCount() > 0) {
            TextView lastSession = (TextView) dynamicSessionsLayout.getChildAt(dynamicSessionsLayout.getChildCount() - 1);
            AlphaAnimation fadeOut = new AlphaAnimation(1, 0);
            fadeOut.setDuration(500);
            lastSession.startAnimation(fadeOut);

            lastSession.postDelayed(() -> dynamicSessionsLayout.removeViewAt(dynamicSessionsLayout.getChildCount() - 1), 500);
        }



    }

}
