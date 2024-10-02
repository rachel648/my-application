package com.example.yogademoapp;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Not extends AppCompatActivity {

    private static final int RC_SIGN_IN = 9001;
    private GoogleSignInClient mGoogleSignInClient;

    SwitchCompat switchNotifications;
    CardView settingsCard, paymentCard;
    Button buttonSetTime;
    LinearLayout notificationTimesContainer;
    int selectedYear, selectedMonth, selectedDay, selectedHour, selectedMinute;
    ArrayList<Calendar> notificationTimes = new ArrayList<>();
    private static final long DOUBLE_TAP_TIME_DELTA = 300;
    private long lastTapTime = 0;
    private int tappedPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_not);

        // Initialize Google Sign-In options
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Set up other UI components
        switchNotifications = findViewById(R.id.switchNotifications);
        buttonSetTime = findViewById(R.id.buttonSetTime);
        notificationTimesContainer = findViewById(R.id.notificationTimesContainer);
        settingsCard = findViewById(R.id.SettingsCard);
        paymentCard = findViewById(R.id.PaymentCard);

        settingsCard.setOnClickListener(v -> {
            Intent intent = new Intent(Not.this, GreenCard.class);
            startActivity(intent);
        });

        paymentCard.setOnClickListener(v -> {
            Intent intent = new Intent(Not.this, profpayment.class);
            startActivity(intent);
        });

        buttonSetTime.setVisibility(switchNotifications.isChecked() ? View.VISIBLE : View.GONE);

        // Request notification and calendar permissions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);
            }
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_CALENDAR}, 102);
        }

        switchNotifications.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                makeNotification();
                buttonSetTime.setVisibility(View.VISIBLE);
            } else {
                buttonSetTime.setVisibility(View.GONE);
            }
        });

        buttonSetTime.setOnClickListener(v -> showDatePickerDialog());
    }

    // Start Google Sign-In Intent
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    // Handle the sign-in result
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.
            Toast.makeText(this, "Signed in as: " + account.getEmail(), Toast.LENGTH_SHORT).show();
        } catch (ApiException e) {
            Toast.makeText(this, "Sign-in failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    // Modify openGoogleCalendar method to insert into Google Calendar
    private void openGoogleCalendar() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account == null) {
            // If user is not signed in, prompt them to sign in
            signIn();
        } else {
            // User is already signed in, insert event into Google Calendar
            Intent intent = new Intent(Intent.ACTION_INSERT);
            intent.setData(CalendarContract.Events.CONTENT_URI);
            intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, getTimeInMillis());
            intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, getTimeInMillis() + 60 * 60 * 1000);
            intent.putExtra(CalendarContract.Events.TITLE, "Yoga Session");
            intent.putExtra(CalendarContract.Events.DESCRIPTION, "Scheduled Yoga Session");

            List<ResolveInfo> resolveInfoList = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            if (resolveInfoList.isEmpty()) {
                Toast.makeText(this, "No calendar app found", Toast.LENGTH_SHORT).show();
            } else {
                startActivity(intent);
            }
        }
    }

    private long getTimeInMillis() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(selectedYear, selectedMonth, selectedDay, selectedHour, selectedMinute, 0);
        return calendar.getTimeInMillis();
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        selectedYear = calendar.get(Calendar.YEAR);
        selectedMonth = calendar.get(Calendar.MONTH);
        selectedDay = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    selectedYear = year;
                    selectedMonth = month;
                    selectedDay = dayOfMonth;
                    showTimePickerDialog();
                }, selectedYear, selectedMonth, selectedDay);
        datePickerDialog.show();
    }

    private void showTimePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        selectedHour = calendar.get(Calendar.HOUR_OF_DAY);
        selectedMinute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, hourOfDay, minute) -> {
                    selectedHour = hourOfDay;
                    selectedMinute = minute;
                    showCalendarOptionDialog();
                }, selectedHour, selectedMinute, true);
        timePickerDialog.show();
    }

    private void showCalendarOptionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Set notification using Google Calendar or cancel?")
                .setPositiveButton("Google Calendar", (dialog, which) -> openGoogleCalendar())
                .setNegativeButton("Cancel", (dialog, which) -> {
                    dialog.dismiss();
                    scheduleNotification(); // Set the notification locally when the user cancels
                })
                .show();
    }

    private void makeNotification() {
        String channelID = "CHANNEL_ID_NOTIFICATION";
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this, channelID)
                        .setSmallIcon(R.drawable.baseline_add_alert_24)
                        .setContentTitle("Notification Title")
                        .setContentText("Some text for notification here")
                        .setAutoCancel(true)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        Intent intent = new Intent(this, NotificationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_MUTABLE);
        builder.setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = notificationManager.getNotificationChannel(channelID);
            if (notificationChannel == null) {
                int importance = NotificationManager.IMPORTANCE_HIGH;
                notificationChannel = new NotificationChannel(channelID, "Some description", importance);
                notificationChannel.setLightColor(Color.GREEN);
                notificationChannel.enableVibration(true);
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }

        notificationManager.notify(0, builder.build());
    }

    private void scheduleNotification() {
        if (notificationTimes.size() >= 3) {
            Toast.makeText(this, "Maximum of 3 notifications can be set", Toast.LENGTH_SHORT).show();
            return;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.set(selectedYear, selectedMonth, selectedDay, selectedHour, selectedMinute, 0);
        notificationTimes.add(calendar);

        // Display the notification time to the user
        String timeString = String.format("%02d:%02d, %02d/%02d/%d", selectedHour, selectedMinute, selectedDay, selectedMonth + 1, selectedYear);
        Toast.makeText(this, "Notification time set: " + timeString, Toast.LENGTH_SHORT).show();

        // Show the notification times
        updateNotificationTimesView();
    }

    private void updateNotificationTimesView() {
        notificationTimesContainer.removeAllViews();
        for (Calendar time : notificationTimes) {
            String timeString = String.format("%02d:%02d, %02d/%02d/%d", time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE), time.get(Calendar.DAY_OF_MONTH), time.get(Calendar.MONTH) + 1, time.get(Calendar.YEAR));
            Button timeButton = new Button(this);
            timeButton.setText(timeString);
            timeButton.setOnClickListener(v -> resetNotificationTime(time));
            notificationTimesContainer.addView(timeButton);
        }
    }

    private void resetNotificationTime(Calendar time) {
        notificationTimes.remove(time);
        updateNotificationTimesView();
        Toast.makeText(this, "Notification time reset", Toast.LENGTH_SHORT).show();
    }
}
