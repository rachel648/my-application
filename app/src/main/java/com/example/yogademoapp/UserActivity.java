package com.example.yogademoapp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.yogademoapp.databinding.ActivityUserBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class UserActivity extends AppCompatActivity {
    Button buttonBooking;
    ActivityUserBinding binding;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedPreferences = getSharedPreferences("UserProfile", MODE_PRIVATE);
        String userEmail = sharedPreferences.getString("userEmail", "user@example.com");
        Toast.makeText(this, userEmail, Toast.LENGTH_SHORT).show();

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("patientEmail", userEmail);
        editor.apply();

        Intent intent = getIntent();
        if (intent != null) {
            String name = intent.getStringExtra("name");
            String phone = intent.getStringExtra("phone");
            String experience = intent.getStringExtra("Experience");
            String fees = intent.getStringExtra("fees");
            String gymNumber = intent.getStringExtra("GymNumber");
            int imageId = intent.getIntExtra("imageid", R.drawable.babe3);

            binding.nameProfile.setText(name);
            binding.phoneProfile.setText(phone);
            binding.Experience.setText(experience);
            binding.fees.setText(fees);
            binding.GymNumber.setText(gymNumber);
            binding.ProfileImage.setImageResource(imageId);
        }

        buttonBooking = findViewById(R.id.buttonBooking);
        buttonBooking.setOnClickListener(view -> showDateTimeDialog());
    }

    private void showDateTimeDialog() {
        final Calendar currentDate = Calendar.getInstance();
        final Calendar date = Calendar.getInstance();

        new DatePickerDialog(UserActivity.this, (view, year, monthOfYear, dayOfMonth) -> {
            date.set(year, monthOfYear, dayOfMonth);
            new TimePickerDialog(UserActivity.this, (view1, hourOfDay, minute) -> {
                date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                date.set(Calendar.MINUTE, minute);
                saveAppointmentDetails(date);
            }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();
    }

    private void saveAppointmentDetails(Calendar date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        String dateTimeString = dateFormat.format(date.getTime());

        SharedPreferences sharedPreferences = getSharedPreferences("UserProfile", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("scheduledTime", dateTimeString);

        int sessionIndex = sharedPreferences.getInt("lastBookedSession", -1);
        if (sessionIndex != -1) {
            String fees = binding.fees.getText().toString();
            String consultant = getIntent().getStringExtra("name");
            editor.putString("session" + sessionIndex + "Fee", fees);
            editor.putString("session" + sessionIndex + "Consultant", consultant);
        }

        editor.apply();

        showBookingConfirmationDialog(dateTimeString);
    }

    private void showBookingConfirmationDialog(String bookingTime) {
        new AlertDialog.Builder(this)
                .setTitle("Booking Confirmed")
                .setMessage("Your appointment has been scheduled for:\n" + bookingTime)
                .setPositiveButton("Proceed to Payment", (dialog, which) -> {
                    Intent intent = new Intent(UserActivity.this, Payment.class);
                    intent.putExtra("TrainFees", binding.fees.getText().toString());
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("OK", (dialog, which) -> finish())
                .show();
    }
}