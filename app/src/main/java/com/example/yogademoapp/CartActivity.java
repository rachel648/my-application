package com.example.yogademoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class CartActivity extends AppCompatActivity {
    HashMap<String, String> item;
    ArrayList List;
    SimpleAdapter sa;
    TextView tvTotal;

    private DatePickerDialog datePickerDialog;

    private TimePickerDialog tinePickerDialog;

    private Button dateButton,timeButton,btnCheckout,btnBack;
    ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        dateButton = findViewById(R.id.buttonCheckoutDate);
        timeButton = findViewById(R.id.buttonCheckoutTime);
        btnCheckout = findViewById(R.id.buttonCheckOutCart);
        btnBack = findViewById(R.id.buttonCheckoutback);



    }
}