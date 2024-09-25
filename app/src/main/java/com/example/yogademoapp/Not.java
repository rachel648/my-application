package com.example.yogademoapp;

import android.Manifest;
import android.app.AlarmManager;
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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Not extends AppCompatActivity {

    SwitchCompat switchNotifications;
    CardView settingsCard, paymentCard;
    Button buttonSetTime;
    LinearLayout notificationTimesContainer;
    int selectedYear, selectedMonth, selectedDay, selectedHour, selectedMinute;
    ArrayList<Calendar> notificationTimes = new ArrayList<>();
    private static final long DOUBLE_TAP_TIME_DELTA = 300; // Time interval for double-tap (milliseconds)
    private long lastTapTime = 0;
    private int tappedPosition = -1; // Store position of the last tapped notification

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_not);

        switchNotifications = findViewById(R.id.switchNotifications);
        buttonSetTime = findViewById(R.id.buttonSetTime);
        notificationTimesContainer = findViewById(R.id.notificationTimesContainer);
        settingsCard = findViewById(R.id.SettingsCard);
        paymentCard = findViewById(R.id.PaymentCard);

        // Set up click listeners for CardViews
        settingsCard.setOnClickListener(v -> {
            Intent intent = new Intent(Not.this, GreenCard.class);
            startActivity(intent);
        });

        paymentCard.setOnClickListener(v -> {
            Intent intent = new Intent(Not.this, profpayment.class);
            startActivity(intent);
        });

        // Set initial visibility based on switch state
        buttonSetTime.setVisibility(switchNotifications.isChecked() ? View.VISIBLE : View.GONE);

        // Request notification permissions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);
            }
        }

        // Request calendar permissions
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_CALENDAR}, 102);
        }

        // Handle switch toggle
        switchNotifications.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // Switch is on - enable notifications
                makeNotification();
                buttonSetTime.setVisibility(View.VISIBLE); // Show button when switch is on
            } else {
                // Switch is off - disable notifications
                buttonSetTime.setVisibility(View.GONE); // Hide button when switch is off
            }
        });

        // Handle button click to set notification time
        buttonSetTime.setOnClickListener(v -> showDatePickerDialog());
    }

    // Function to create and display a notification
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
        intent.putExtra("data", "some value to be passed here");

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
                    // Show the dialog to choose Google Calendar or Cancel
                    showCalendarOptionDialog();
                }, selectedHour, selectedMinute, true);
        timePickerDialog.show();
    }

    // Function to display dialog with Google Calendar option
    private void showCalendarOptionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Set notification using Google Calendar or cancel?")
                .setPositiveButton("Google Calendar", (dialog, which) -> openGoogleCalendar()) // Open Google Calendar
                .setNegativeButton("Cancel", (dialog, which) -> {
                    dialog.dismiss(); // Dismiss the dialog
                    scheduleNotification(); // Set the notification locally
                })
                .show();
    }

    // Function to open Google Calendar
    private void openGoogleCalendar() {
        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setData(CalendarContract.Events.CONTENT_URI);
        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, getTimeInMillis());
        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, getTimeInMillis() + 60 * 60 * 1000); // Event duration is 1 hour
        intent.putExtra(CalendarContract.Events.TITLE, "Yoga Session");
        intent.putExtra(CalendarContract.Events.DESCRIPTION, "Scheduled Yoga Session");

        // Add this line
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Log the available calendar apps
        List<ResolveInfo> resolveInfoList = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : resolveInfoList) {
            Log.d("CalendarApp", "Package: " + resolveInfo.activityInfo.packageName);
        }

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent); // Open Google Calendar
        } else {
            Toast.makeText(this, "No calendar app found", Toast.LENGTH_SHORT).show();
        }
    }



    // Helper function to convert selected date and time to milliseconds
    private long getTimeInMillis() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(selectedYear, selectedMonth, selectedDay, selectedHour, selectedMinute, 0);
        return calendar.getTimeInMillis();
    }

    private void scheduleNotification() {
        if (notificationTimes.size() >= 3) {
            Toast.makeText(this, "Maximum of 3 notifications can be set", Toast.LENGTH_SHORT).show();
            return;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.set(selectedYear, selectedMonth, selectedDay, selectedHour, selectedMinute, 0);

        notificationTimes.add(calendar); // Store the scheduled time
        updateNotificationTimesDisplay();

        Intent intent = new Intent(this, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, notificationTimes.size() - 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            Toast.makeText(this, "Notification scheduled for " + selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDay + " " + selectedHour + ":" + selectedMinute, Toast.LENGTH_LONG).show();
        }
    }

    private void updateNotificationTimesDisplay() {
        notificationTimesContainer.removeAllViews();
        for (int i = 0; i < notificationTimes.size(); i++) {
            Calendar time = notificationTimes.get(i);

            // Use LinearLayout instead of RelativeLayout
            LinearLayout timeLayout = new LinearLayout(this);
            timeLayout.setOrientation(LinearLayout.HORIZONTAL);
            timeLayout.setPadding(16, 16, 16, 16);

            TextView dateView = new TextView(this);
            dateView.setText(String.format("%d-%d-%d", time.get(Calendar.YEAR), time.get(Calendar.MONTH) + 1, time.get(Calendar.DAY_OF_MONTH)));
            dateView.setTextSize(16);
            dateView.setTextColor(Color.BLACK);
            // Set layout weight for the date view to take up space
            LinearLayout.LayoutParams dateParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
            dateView.setLayoutParams(dateParams);
            timeLayout.addView(dateView);

            TextView timeView = new TextView(this);
            timeView.setText(String.format("%02d:%02d", time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE)));
            timeView.setTextSize(16);
            timeView.setTextColor(Color.BLACK);
            // Set layout weight for the time view to take up space
            LinearLayout.LayoutParams timeParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
            timeView.setLayoutParams(timeParams);
            timeLayout.addView(timeView);

            // Set click listener for the timeView to reset the notification time
            int finalI = i;
            timeView.setOnClickListener(v -> {
                // Check if double-tap
                long currentTime = System.currentTimeMillis();
                if (currentTime - lastTapTime < DOUBLE_TAP_TIME_DELTA) {
                    // Double-tap detected
                    resetNotificationTime(finalI);
                } else {
                    // Regular tap detected
                    tappedPosition = finalI; // Update tapped position
                }
                lastTapTime = currentTime; // Update last tap time
            });

            notificationTimesContainer.addView(timeLayout);
        }
    }

    private void resetNotificationTime(int position) {
        // Reset the notification time by removing it from the list
        notificationTimes.remove(position);
        updateNotificationTimesDisplay(); // Refresh the display
        Toast.makeText(this, "Notification time reset", Toast.LENGTH_SHORT).show();
    }
}
