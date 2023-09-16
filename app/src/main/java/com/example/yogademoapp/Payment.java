package com.example.yogademoapp;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;


import androidx.appcompat.app.AppCompatActivity;

public class Payment extends AppCompatActivity {
    Toolbar toolbar;
    TextView subTotal,discount,shipping,total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        //Toolbar
        toolbar = findViewById(R.id.payment_toolbar);
        //setSupportActionBar(toolbar); //inaharibu
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        subTotal = findViewById(R.id.sub_total);
        discount = findViewById(R.id.textView17);
        shipping = findViewById(R.id.textView18);
        total = findViewById(R.id.total_amt);


    }
}