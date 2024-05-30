package com.example.yogademoapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.example.yogademoapp.databinding.ActivityUserBinding;

import java.util.Calendar;

public class UserActivity extends AppCompatActivity {

    Button buttonBooking;
    ActivityUserBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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
        buttonBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateTimeDialog();
            }
        });
    }

    private void showDateTimeDialog() {
        final Calendar currentDate = Calendar.getInstance();
        final Calendar date = Calendar.getInstance();

        new DatePickerDialog(UserActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                date.set(year, monthOfYear, dayOfMonth);
                new TimePickerDialog(UserActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        date.set(Calendar.MINUTE, minute);
                        proceedToPayment(date);
                    }
                }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();
            }
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();
    }

    private void proceedToPayment(Calendar date) {
        Intent bookAppointmentIntent = new Intent(UserActivity.this, Payment.class);
        String fees = binding.fees.getText().toString();
        bookAppointmentIntent.putExtra("TrainFees", fees);
        bookAppointmentIntent.putExtra("AppointmentDateTime", date.getTimeInMillis());
        startActivity(bookAppointmentIntent);
    }
}
