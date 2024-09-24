package com.example.yogademoapp;

import android.Manifest;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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


public class Not extends AppCompatActivity {

    SwitchCompat switchNotifications;

    CardView  settingsCard, paymentCard; //
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

        CardView settingsCard = findViewById(R.id.SettingsCard);
        CardView paymentCard = findViewById(R.id.PaymentCard);


        // Set up click listeners for CardViews
        settingsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Not.this, GreenCard.class);
                startActivity(intent);
            }
        });

        paymentCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Not.this, profpayment.class);
                startActivity(intent);
            }
        });

        // Set initial visibility based on switch state
        buttonSetTime.setVisibility(switchNotifications.isChecked() ? View.VISIBLE : View.GONE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);
            }
        }

        // Handle switch toggle
        switchNotifications.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // Switch is on - enable notifications
                makeNotification();
                buttonSetTime.setVisibility(View.VISIBLE); // Show button when switch is on
            } else {
                // Switch is off - disable notifications
                // Optionally, you could stop notifications here
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
                    // Schedule the notification
                    scheduleNotification();
                }, selectedHour, selectedMinute, true);
        timePickerDialog.show();
    }

    private void scheduleNotification() {
        // Check if the maximum number of notifications has been reached
        if (notificationTimes.size() >= 3) {
            Toast.makeText(this, "Maximum of 3 notifications can be set", Toast.LENGTH_SHORT).show();
            return; // Exit the method if the limit is reached
        }

        Calendar calendar = Calendar.getInstance();
        calendar.set(selectedYear, selectedMonth, selectedDay, selectedHour, selectedMinute, 0);

        notificationTimes.add(calendar); // Store the scheduled time

        // Update the UI with the new scheduled time
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
            RelativeLayout timeLayout = new RelativeLayout(this);

            TextView dateView = new TextView(this);
            dateView.setText(String.format("%d-%d-%d", time.get(Calendar.YEAR), time.get(Calendar.MONTH) + 1, time.get(Calendar.DAY_OF_MONTH)));
            dateView.setId(View.generateViewId());
            dateView.setPadding(16, 16, 16, 16);
            dateView.setTextSize(16);
            dateView.setTextColor(Color.BLACK); // Set text color to black

            TextView timeView = new TextView(this);
            timeView.setText(String.format("%02d:%02d", time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE)));
            timeView.setPadding(16, 16, 16, 16);
            timeView.setTextSize(16);
            timeView.setTextColor(Color.BLACK); // Set text color to black

            RelativeLayout.LayoutParams dateParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            dateParams.addRule(RelativeLayout.ALIGN_PARENT_START);
            dateView.setLayoutParams(dateParams);

            RelativeLayout.LayoutParams timeParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            timeParams.addRule(RelativeLayout.ALIGN_PARENT_END);
            timeParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            timeView.setLayoutParams(timeParams);

            timeLayout.addView(dateView);
            timeLayout.addView(timeView);
            int finalI = i;
            timeLayout.setOnClickListener(v -> handleNotificationClick(finalI)); // Handle double-tap

            RelativeLayout.LayoutParams timeLayoutParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            timeLayoutParams.setMargins(0, 0, 0, 16); // Add bottom margin to create space between notifications
            timeLayout.setLayoutParams(timeLayoutParams);

            notificationTimesContainer.addView(timeLayout);

            // Add a divider after each notification, except the last one
            if (i < notificationTimes.size() - 1) {
                View divider = new View(this);
                LinearLayout.LayoutParams dividerParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, 2); // height of the line
                divider.setLayoutParams(dividerParams);
                divider.setBackgroundColor(Color.BLACK); // Set divider color to black

                // Add top margin to create space between the divider and the notification
                LinearLayout.LayoutParams dividerMarginParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, 2);
                dividerMarginParams.setMargins(0, 16, 0, 0); // Top margin for space above the divider
                divider.setLayoutParams(dividerMarginParams);

                notificationTimesContainer.addView(divider);
            }
        }
    }

    private void handleNotificationClick(int position) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastTapTime < DOUBLE_TAP_TIME_DELTA && tappedPosition == position) {
            // It's a double-tap on the same notification
            removeNotification(position);
        } else {
            // Single tap or double-tap on a different notification
            lastTapTime = currentTime;
            tappedPosition = position;
            // Optionally, you can show a feedback for single tap
        }
    }

    private void removeNotification(int position) {
        // Remove the notification from the list and update the UI
        notificationTimes.remove(position);
        updateNotificationTimesDisplay();

        // Remove the alarm for the removed notification
        Intent intent = new Intent(this, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, position, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }

        Toast.makeText(this, "Notification removed", Toast.LENGTH_SHORT).show();



    }
}
