package com.example.yogademoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

public class BookAppointmentActivity extends AppCompatActivity {
    EditText ed1,ed2,ed3,ed4;
    TextView tv;
    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appointment);

        tv =  findViewById(R.id.textViewAppTitle);
        ed1 = findViewById(R.id.editTextFullName);
        ed2 = findViewById(R.id.editTextAppAddress);
        ed3=  findViewById(R.id.editTextAppContact);
        ed4 = findViewById(R.id.editTextAppFees);


        ed1.setKeyListener(null);
        ed2.setKeyListener(null);
        ed3.setKeyListener(null);
        ed4.setKeyListener(null);

        Intent it = getIntent(); //fetching data with the help of intent
        String title = it.getStringExtra("text1"); //set info to all the variables:title,fullname,address,contant,fees
        String fullname = it.getStringExtra("text2");
        String address = it.getStringExtra("text3");
        String contact = it.getStringExtra("text4");
        String fees = it.getStringExtra("text5");

        tv.setText(title); //Display
        ed1.setText(fullname);
        ed2.setText(address);
        ed3.setText(contact);
        ed4.setText( "Train Fees:"+fees+ "ksh");

    }
}