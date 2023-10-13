package com.example.yogademoapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ConsultantActivity extends AppCompatActivity {

    private TextView Consultationtitle,headtextview;
    private ListView listviewConsultants;

    private Button consultationback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultant);

         Consultationtitle = findViewById(R.id.TextviewGymDetailsName);
         headtextview = findViewById(R.id.TextviewGymDetailsTitle);
         listviewConsultants = findViewById(R.id.listviewConsultants);
        consultationback= findViewById(R.id.buttonConsultantDetailback);


        consultationback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent paymentIntent = new Intent(ConsultantActivity.this, Payment.class);
            }
        });

    }
}