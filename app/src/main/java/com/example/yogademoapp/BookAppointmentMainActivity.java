package com.example.yogademoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class BookAppointmentMainActivity extends AppCompatActivity {
 EditText ed1,ed2,ed3,ed4;
 TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appointment_main);
    tv =  findViewById(R.id.textViewAppTitle);
    ed1 = findViewById(R.id.editTextAppAddress);
    ed2=  findViewById(R.id.editTextAppContact);
    ed3 = findViewById(R.id.editTextAppFees);
    ed4 = findViewById(R.id.editTextFullName);

        ed1.setKeyListener(null);
        ed1.setKeyListener(null);
        ed1.setKeyListener(null);
        ed1.setKeyListener(null);
    }

}