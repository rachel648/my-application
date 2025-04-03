package com.example.yogademoapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.io.ByteArrayOutputStream;

public class profpayment extends AppCompatActivity {

    TextView sessionOnePlusSign, emailTextView, usernameTextView, trainerNameTextView;
    TextView totalAmountTextView;
    ImageView profileImageView;
    LinearLayout dynamicSessionsLayout;
    TextView addSessionPlusSign, addSessionMinusSign;
    Button receiptButton;

    int sessionCount = 1;
    String[] sessionFees = new String[5];
    String[] consultantNames = new String[5];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profpayment);

        initializeViews();
        setupSharedPreferences();
        setupNavigation();
        setupSessionManagement();
        setupProfileData();
        setupReceiptButton();
        updateTotalAmount();
    }

    private void initializeViews() {
        sessionOnePlusSign = findViewById(R.id.tvSessionOne);
        dynamicSessionsLayout = findViewById(R.id.dynamicSessionsLayout);
        addSessionPlusSign = findViewById(R.id.addSessionPlusSign);
        addSessionMinusSign = findViewById(R.id.addSessionMinusSign);
        emailTextView = findViewById(R.id.textviewemail);
        usernameTextView = findViewById(R.id.textviewUsername);
        profileImageView = findViewById(R.id.imageView);
     //   trainerNameTextView = findViewById(R.id.trainerNameTextView);
        totalAmountTextView = findViewById(R.id.tvPaymentAmount);
        receiptButton = findViewById(R.id.receiptButton);
    }

    private void setupSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserProfile", Context.MODE_PRIVATE);
        String trainerName = sharedPreferences.getString("trainerName", "Trainer");

        for (int i = 0; i < 5; i++) {
            sessionFees[i] = sharedPreferences.getString("session" + i + "Fee", i == 0 ? "5000" : "0");
            consultantNames[i] = sharedPreferences.getString("session" + i + "Consultant", i == 0 ? trainerName : "Not selected");
        }

        updateAllSessionDisplays();
    }

    private void setupReceiptButton() {
        receiptButton.setOnClickListener(v -> showReceiptDialog());
    }

    private void showReceiptDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Payment Receipt");

        StringBuilder receiptBuilder = new StringBuilder();
        int totalAmount = 0;

        for (int i = 0; i < sessionCount; i++) {
            if (!sessionFees[i].equals("0")) {
                receiptBuilder.append("• Session ").append(i+1)
                        .append(": ").append(consultantNames[i])
                        .append("\n   Fee: ksh ").append(sessionFees[i])
                        .append("\n\n");
                totalAmount += Integer.parseInt(sessionFees[i]);
            }
        }

        receiptBuilder.append("Total Amount: ksh ").append(totalAmount).append("\n\n");

        SharedPreferences sharedPreferences = getSharedPreferences("UserProfile", MODE_PRIVATE);
        String paymentTime = sharedPreferences.getString("scheduledTime", "Not scheduled yet");
        receiptBuilder.append("Payment Time: ").append(paymentTime);

        builder.setMessage(receiptBuilder.toString());
        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());

        builder.setNegativeButton("Share", (dialog, which) -> shareReceipt(receiptBuilder.toString()));

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void shareReceipt(String receiptText) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Yoga App Payment Receipt");
        shareIntent.putExtra(Intent.EXTRA_TEXT, receiptText);
        startActivity(Intent.createChooser(shareIntent, "Share Receipt"));
    }

    private void updateTotalAmount() {
        int total = 0;
        for (int i = 0; i < sessionCount; i++) {
            try {
                total += Integer.parseInt(sessionFees[i]);
            } catch (NumberFormatException e) {
                total += 0;
            }
        }
        totalAmountTextView.setText("Total Amount: ksh " + total);
    }

    private void setupNavigation() {
        CardView notificationsCardView = findViewById(R.id.notifications);
        notificationsCardView.setOnClickListener(v -> startActivity(new Intent(profpayment.this, Not.class)));

        CardView settingsCardView = findViewById(R.id.setting);
        settingsCardView.setOnClickListener(v -> startActivity(new Intent(profpayment.this, GreenCard.class)));
    }

    private void setupSessionManagement() {
        final ScaleAnimation scaleAnimation = new ScaleAnimation(
                1f, 1.2f, 1f, 1.2f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f
        );
        scaleAnimation.setDuration(150);
        scaleAnimation.setRepeatCount(0);
        scaleAnimation.setFillAfter(true);

        sessionOnePlusSign.setOnClickListener(v -> navigateToConsultantActivity(0));

        addSessionPlusSign.setOnClickListener(v -> {
            v.startAnimation(scaleAnimation);
            if (sessionCount < 5) {
                addSession(sessionCount);
                sessionCount++;
                if (sessionCount == 5) {
                    addSessionPlusSign.setEnabled(false);
                }
                updateTotalAmount();
            }
        });

        addSessionMinusSign.setOnClickListener(v -> {
            v.startAnimation(scaleAnimation);
            if (sessionCount > 1) {
                removeSession();
                sessionCount--;
                if (sessionCount < 5) {
                    addSessionPlusSign.setEnabled(true);
                }
                updateTotalAmount();
            }
        });
    }

    private void setupProfileData() {
        String email = getIntent().getStringExtra("EMAIL");
        if (email != null) {
            emailTextView.setText(email);
            String username = email.split("@")[0];
            if (!username.isEmpty()) {
                username = username.substring(0, 1).toUpperCase() + username.substring(1);
                usernameTextView.setText(username);
            }
            saveEmailToSharedPreferences(email);
        }

        byte[] imageBytes = getIntent().getByteArrayExtra("PROFILE_IMAGE");
        if (imageBytes != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            profileImageView.setImageBitmap(bitmap);
            saveImageToSharedPreferences(bitmap);
        }
    }

    private void addSession(int sessionIndex) {
        TextView newSession = new TextView(this);
        newSession.setText("Session " + (sessionIndex + 1) + ": ksh " + sessionFees[sessionIndex]);
        newSession.setTag(sessionIndex);

        newSession.setTextSize(18);
        newSession.setTextColor(getResources().getColor(R.color.white));
        newSession.setGravity(Gravity.CENTER);
        newSession.setPadding(0, 10, 0, 10);

        GradientDrawable gradient = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{getResources().getColor(R.color.green_light), getResources().getColor(R.color.green_dark)}
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

        newSession.setOnClickListener(v -> navigateToConsultantActivity((Integer) v.getTag()));

        AlphaAnimation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setDuration(500);
        newSession.startAnimation(fadeIn);

        dynamicSessionsLayout.addView(newSession);
    }

    private void navigateToConsultantActivity(int sessionIndex) {
        SharedPreferences sharedPreferences = getSharedPreferences("UserProfile", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("currentSessionIndex", sessionIndex);
        editor.apply();

        startActivity(new Intent(profpayment.this, ConsultantActivity.class));
    }

    private void updateAllSessionDisplays() {
        for (int i = 0; i < sessionCount; i++) {
            updateSessionDisplay(i);
        }
    }

    private void updateSessionDisplay(int sessionIndex) {
        String displayText = "Session " + (sessionIndex + 1) + ": ksh " + sessionFees[sessionIndex];

        if (sessionIndex == 0) {
            sessionOnePlusSign.setText(displayText);
        } else {
            for (int i = 0; i < dynamicSessionsLayout.getChildCount(); i++) {
                View child = dynamicSessionsLayout.getChildAt(i);
                if (child.getTag() != null && (int) child.getTag() == sessionIndex) {
                    ((TextView) child).setText(displayText);
                    break;
                }
            }
        }

        SharedPreferences sharedPreferences = getSharedPreferences("UserProfile", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("session" + sessionIndex + "Fee", sessionFees[sessionIndex]);
        editor.putString("session" + sessionIndex + "Consultant", consultantNames[sessionIndex]);
        editor.apply();
    }

    private void removeSession() {
        if (dynamicSessionsLayout.getChildCount() > 0) {
            int lastIndex = sessionCount - 1;
            sessionFees[lastIndex] = "0";
            consultantNames[lastIndex] = "Not selected";

            TextView lastSession = (TextView) dynamicSessionsLayout.getChildAt(dynamicSessionsLayout.getChildCount() - 1);
            AlphaAnimation fadeOut = new AlphaAnimation(1, 0);
            fadeOut.setDuration(500);
            lastSession.startAnimation(fadeOut);
            lastSession.postDelayed(() -> dynamicSessionsLayout.removeViewAt(dynamicSessionsLayout.getChildCount() - 1), 500);

            SharedPreferences sharedPreferences = getSharedPreferences("UserProfile", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("session" + lastIndex + "Fee", "0");
            editor.putString("session" + lastIndex + "Consultant", "Not selected");
            editor.apply();
        }
    }

    private void saveEmailToSharedPreferences(String email) {
        SharedPreferences sharedPreferences = getSharedPreferences("ProfilePrefs", MODE_PRIVATE);
        sharedPreferences.edit().putString("EMAIL", email).apply();
    }

    private void saveImageToSharedPreferences(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
        getSharedPreferences("ProfilePrefs", MODE_PRIVATE)
                .edit()
                .putString("PROFILE_IMAGE", encodedImage)
                .apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkForFeeUpdates();
        updateTotalAmount();
    }

    private void checkForFeeUpdates() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserProfile", MODE_PRIVATE);
        int updatedSessionIndex = sharedPreferences.getInt("updatedSessionIndex", -1);

        if (updatedSessionIndex != -1) {
            String updatedFee = sharedPreferences.getString("updatedFee", "0");
            String consultantName = sharedPreferences.getString("selectedConsultantName", "Not selected");

            sessionFees[updatedSessionIndex] = updatedFee;
            consultantNames[updatedSessionIndex] = consultantName;
            updateSessionDisplay(updatedSessionIndex);

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove("updatedSessionIndex");
            editor.remove("updatedFee");
            editor.remove("selectedConsultantName");
            editor.apply();
        }
    }
}