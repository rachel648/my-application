package com.example.yogademoapp;

import android.Manifest;
import android.app.*;
import android.content.*;
import android.content.pm.*;
import android.graphics.Color;
import android.os.*;
import android.provider.CalendarContract;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.cardview.widget.CardView;
import androidx.core.app.*;
import java.util.*;

public class Not extends AppCompatActivity {

    private SwitchCompat switchNotifications;
    private Button buttonSetTime;
    private LinearLayout notificationTimesContainer;
    private CardView settingsCard, paymentCard;
    private ArrayList<Calendar> notificationTimes = new ArrayList<>();
    private int selectedYear, selectedMonth, selectedDay, selectedHour, selectedMinute;
    private static final int MAX_NOTIFICATIONS = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_not);

        initViews();
        requestPermissions();
        setupListeners();
    }

    private void initViews() {
        switchNotifications = findViewById(R.id.switchNotifications);
        buttonSetTime = findViewById(R.id.buttonSetTime);
        notificationTimesContainer = findViewById(R.id.notificationTimesContainer);
        settingsCard = findViewById(R.id.SettingsCard);
        paymentCard = findViewById(R.id.PaymentCard);
        buttonSetTime.setVisibility(switchNotifications.isChecked() ? View.VISIBLE : View.GONE);
    }

    private void requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);
        }
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_CALENDAR}, 102);
    }

    private void setupListeners() {
        settingsCard.setOnClickListener(v -> startActivity(new Intent(this, GreenCard.class)));
        paymentCard.setOnClickListener(v -> startActivity(new Intent(this, profpayment.class)));
        switchNotifications.setOnCheckedChangeListener((buttonView, isChecked) -> {
            buttonSetTime.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            if (isChecked) makeNotification();
        });
        buttonSetTime.setOnClickListener(v -> showDatePickerDialog());
    }

    private void makeNotification() {
        String channelID = "CHANNEL_ID_NOTIFICATION";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelID)
                .setSmallIcon(R.drawable.baseline_add_alert_24)
                .setContentTitle("iMental Reminder Set")
                .setContentText("You have set a notification")
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(PendingIntent.getActivity(this, 0,
                        new Intent(this, NotificationActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP),
                        PendingIntent.FLAG_MUTABLE));

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && notificationManager != null) {
            NotificationChannel channel = new NotificationChannel(channelID, "Notifications", NotificationManager.IMPORTANCE_HIGH);
            channel.setLightColor(Color.GREEN);
            channel.enableVibration(true);
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(0, builder.build());
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            selectedYear = year;
            selectedMonth = month;
            selectedDay = dayOfMonth;
            showTimePickerDialog();
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void showTimePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        new TimePickerDialog(this, (view, hourOfDay, minute) -> {
            selectedHour = hourOfDay;
            selectedMinute = minute;
            showCalendarOptionDialog();
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
    }

    private void showCalendarOptionDialog() {
        new AlertDialog.Builder(this)
                .setMessage("Set notification using Google Calendar or cancel?")
                .setPositiveButton("Google Calendar", (dialog, which) -> openGoogleCalendar())
                .setNegativeButton("Cancel", (dialog, which) -> {
                    dialog.dismiss();
                    scheduleNotification();
                }).show();
    }

    private void openGoogleCalendar() {
        Intent intent = new Intent(Intent.ACTION_INSERT).setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, getTimeInMillis())
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, getTimeInMillis() + 3600000)
                .putExtra(CalendarContract.Events.TITLE, "Yoga Session")
                .putExtra(CalendarContract.Events.DESCRIPTION, "Scheduled Yoga Session");
        if (getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY).isEmpty()) {
            Toast.makeText(this, "No calendar app found", Toast.LENGTH_SHORT).show();
        } else {
            startActivity(intent);
        }
    }

    private long getTimeInMillis() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(selectedYear, selectedMonth, selectedDay, selectedHour, selectedMinute, 0);
        return calendar.getTimeInMillis();
    }

    private void scheduleNotification() {
        if (notificationTimes.size() >= MAX_NOTIFICATIONS) {
            Toast.makeText(this, "Maximum of " + MAX_NOTIFICATIONS + " notifications can be set", Toast.LENGTH_SHORT).show();
            return;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.set(selectedYear, selectedMonth, selectedDay, selectedHour, selectedMinute, 0);
        notificationTimes.add(calendar);
        updateNotificationTimesDisplay();

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, notificationTimes.size() - 1,
                new Intent(this, NotificationReceiver.class), PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
        Toast.makeText(this, "Notification scheduled", Toast.LENGTH_LONG).show();
    }

    private void updateNotificationTimesDisplay() {
        notificationTimesContainer.removeAllViews();
        for (Calendar time : notificationTimes) {
            TextView timeView = new TextView(this);
            timeView.setText(String.format("%d-%d-%d %02d:%02d", time.get(Calendar.YEAR), time.get(Calendar.MONTH) + 1,
                    time.get(Calendar.DAY_OF_MONTH), time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE)));
            timeView.setTextSize(16);
            timeView.setTextColor(Color.BLACK);
            notificationTimesContainer.addView(timeView);
        }
    }
}
