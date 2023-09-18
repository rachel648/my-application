package com.example.yogademoapp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;


public class BookAppointmentActivity extends AppCompatActivity {
    EditText ed1, ed2, ed3, ed4;
    TextView tv;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;

    private Button dateButton, timeButton, btnBook, btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appointment);

        tv = findViewById(R.id.textViewAppTitle);
        ed1 = findViewById(R.id.editTextBookingName);
        ed2 = findViewById(R.id.editTextBookingAddress);
        ed3 = findViewById(R.id.editTextBookingPincode);
        ed4 = findViewById(R.id.editTextContactNumber);
        dateButton = findViewById(R.id.buttonDate); //mapping
        timeButton = findViewById(R.id.buttonSelectTime);
        btnBook = findViewById(R.id.ButtonBooking);
        btnBack = findViewById(R.id.BackBookApp);

        ed1.setKeyListener(null);
        ed2.setKeyListener(null);
        ed3.setKeyListener(null);
        ed4.setKeyListener(null);

        dateButton = findViewById(R.id.buttonDate);
        timeButton = findViewById(R.id.buttonSelectTime);

        Intent it = getIntent(); //fetching data with the help of intentA
        String title = it.getStringExtra("text1"); //set info to all the variables:title,fullname,address,contant,fees
        String fullname = it.getStringExtra("text2");
        String address = it.getStringExtra("text3");
        String contact = it.getStringExtra("text4");
        String fees = it.getStringExtra("text5");

        tv.setText(title); //Display
        ed1.setText(fullname);
        ed2.setText(address);
        ed3.setText(contact);
        ed4.setText("Train Fees: " + fees + "ksh");

        // datepicker
        initDatePicker();
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();
            }
        });

        // timepicker
        initTimePicker();
        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePickerDialog.show();
            }
        });
        // Initialize the time picker
        //initTimePicker();
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               startActivity(new Intent(BookAppointmentActivity.this,BoxActivity.class));
            }
        });

        btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

// Perform the booking logic here
                // You can add your booking logic,saving the appointment details to a database.

                // Show a success message using a Toast
                Toast.makeText(BookAppointmentActivity.this, "Successfully booked!", Toast.LENGTH_SHORT).show();

// Create an Intent to start the Payment activity

                Intent paymentIntent = new Intent(BookAppointmentActivity.this, Payment.class);

                // Pass the Train Fees value to the Payment activity
                String trainFees = getIntent().getStringExtra("text5");
                paymentIntent.putExtra("TrainFees", trainFees);

                startActivity(paymentIntent);
            }
        });
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = (datePicker, i, i1, i2) -> {
            i1 = i1 + 1;
            dateButton.setText(i2 + "/" + i1 + "/" + i);
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(this, dateSetListener, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(cal.getTimeInMillis() + 86400000);
    }

    private void initTimePicker() {
        TimePickerDialog.OnTimeSetListener timeSetListener = (timePicker, i, i1) -> timeButton.setText(i + ":" + i1);

        Calendar cal = Calendar.getInstance();
        int hrs = cal.get(Calendar.HOUR);
        int mins = cal.get(Calendar.MINUTE);

        int style = AlertDialog.THEME_HOLO_DARK;
        timePickerDialog = new TimePickerDialog(this, style, timeSetListener, hrs, mins, true);



    }
}
