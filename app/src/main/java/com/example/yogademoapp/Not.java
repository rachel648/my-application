package com.example.yogademoapp;

import android.Manifest;
import android.app.*;
import android.content.*;
import android.content.pm.*;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.*;
import android.util.Base64;
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
    private ImageView profileImageView;
    private TextView emailTextView, nameTextView;
    private AlarmManager alarmManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_not);

        initViews();
        requestPermissions();
        setupListeners();
        loadProfileData();
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        restoreScheduledNotifications();
    }

    private void initViews() {
        switchNotifications = findViewById(R.id.switchNotifications);
        buttonSetTime = findViewById(R.id.buttonSetTime);
        notificationTimesContainer = findViewById(R.id.notificationTimesContainer);
        settingsCard = findViewById(R.id.SettingsCard);
        paymentCard = findViewById(R.id.PaymentCard);
        profileImageView = findViewById(R.id.imageView);
        emailTextView = findViewById(R.id.textviewemail);
        nameTextView = findViewById(R.id.textView7);
        buttonSetTime.setVisibility(switchNotifications.isChecked() ? View.VISIBLE : View.GONE);

        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(Not.this, mentaldashboard.class);
            startActivity(intent);
        });
    }

    private void loadProfileData() {
        SharedPreferences sharedPreferences = getSharedPreferences("ProfilePrefs", MODE_PRIVATE);
        String email = sharedPreferences.getString("EMAIL", null);

        if (email != null) {
            emailTextView.setText(email);
            nameTextView.setText(extractNameFromEmail(email));
        }

        String encodedImage = sharedPreferences.getString("PROFILE_IMAGE", null);
        if (encodedImage != null) {
            byte[] byteArray = Base64.decode(encodedImage, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            profileImageView.setImageBitmap(bitmap);
        }
    }

    private String extractNameFromEmail(String email) {
        if (email == null || email.isEmpty()) return "John Doe";

        int atIndex = email.indexOf('@');
        if (atIndex > 0) {
            String namePart = email.substring(0, atIndex);
            return namePart.substring(0, 1).toUpperCase() +
                    (namePart.length() > 1 ? namePart.substring(1) : "");
        }
        return "John Doe";
    }

    private void requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);
        }
    }

    private void setupListeners() {
        settingsCard.setOnClickListener(v -> startActivity(new Intent(this, GreenCard.class)));
        paymentCard.setOnClickListener(v -> startActivity(new Intent(this, profpayment.class)));

        switchNotifications.setOnCheckedChangeListener((buttonView, isChecked) -> {
            buttonSetTime.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            if (isChecked) makeInitialNotification();
        });

        buttonSetTime.setOnClickListener(v -> showDateTimePicker());
    }

    private void showDateTimePicker() {
        Calendar current = Calendar.getInstance();
        new DatePickerDialog(this, (dateView, year, month, day) -> {
            selectedYear = year;
            selectedMonth = month;
            selectedDay = day;
            new TimePickerDialog(this, (timeView, hour, minute) -> {
                selectedHour = hour;
                selectedMinute = minute;
                scheduleNotification();
            }, current.get(Calendar.HOUR_OF_DAY), current.get(Calendar.MINUTE), true).show();
        }, current.get(Calendar.YEAR), current.get(Calendar.MONTH), current.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void makeInitialNotification() {
        createNotificationChannel();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "CHANNEL_ID_NOTIFICATION")
                .setSmallIcon(R.drawable.baseline_add_alert_24)
                .setContentTitle("iMental Reminders Active")
                .setContentText("You'll receive notifications at your scheduled times")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat.from(this).notify(0, builder.build());
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "CHANNEL_ID_NOTIFICATION",
                    "Reminder Notifications",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Channel for mental health reminders");
            channel.enableLights(true);
            channel.setLightColor(Color.GREEN);
            channel.enableVibration(true);

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

    private void scheduleNotification() {
        if (notificationTimes.size() >= MAX_NOTIFICATIONS) {
            Toast.makeText(this, "Max " + MAX_NOTIFICATIONS + " notifications allowed", Toast.LENGTH_SHORT).show();
            return;
        }

        Calendar notificationTime = Calendar.getInstance();
        notificationTime.set(selectedYear, selectedMonth, selectedDay, selectedHour, selectedMinute, 0);

        if (notificationTime.before(Calendar.getInstance())) {
            Toast.makeText(this, "Please select a future time", Toast.LENGTH_SHORT).show();
            return;
        }

        long triggerTime = notificationTime.getTimeInMillis();
        saveNotification(triggerTime);

        Intent intent = new Intent(this, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                (int) triggerTime,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        if (alarmManager != null) {
            alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerTime,
                    pendingIntent
            );
        }

        notificationTimes.add(notificationTime);
        updateNotificationTimesDisplay();
        Toast.makeText(this, "Reminder set for " + formatTime(notificationTime), Toast.LENGTH_LONG).show();
    }

    private String formatTime(Calendar calendar) {
        return String.format("%02d/%02d at %02d:%02d",
                calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE));
    }

    private void saveNotification(long timeInMillis) {
        SharedPreferences prefs = getSharedPreferences("NotificationPrefs", MODE_PRIVATE);
        int id = prefs.getInt("next_id", 0);

        prefs.edit()
                .putLong("notification_" + id, timeInMillis)
                .putInt("next_id", id + 1)
                .apply();
    }

    private void restoreScheduledNotifications() {
        SharedPreferences prefs = getSharedPreferences("NotificationPrefs", MODE_PRIVATE);
        int count = prefs.getInt("next_id", 0);

        for (int i = 0; i < count; i++) {
            long time = prefs.getLong("notification_" + i, 0);
            if (time > System.currentTimeMillis()) {
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(time);
                notificationTimes.add(cal);

                Intent intent = new Intent(this, NotificationReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(
                        this,
                        (int) time,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
                );

                if (alarmManager != null) {
                    alarmManager.setExactAndAllowWhileIdle(
                            AlarmManager.RTC_WAKEUP,
                            time,
                            pendingIntent
                    );
                }
            }
        }
        updateNotificationTimesDisplay();
    }

    private void updateNotificationTimesDisplay() {
        notificationTimesContainer.removeAllViews();

        for (Calendar time : notificationTimes) {
            TextView tv = new TextView(this);
            tv.setText(formatTime(time));
            tv.setTextSize(16);
            tv.setTextColor(Color.BLACK);
            tv.setPadding(0, 8, 0, 8);
            notificationTimesContainer.addView(tv);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Clear old notifications when leaving
        NotificationManagerCompat.from(this).cancel(0);
    }
}