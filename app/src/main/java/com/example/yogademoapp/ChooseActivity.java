package com.example.yogademoapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ChooseActivity extends AppCompatActivity {

    Button  logoutButton, consultationButton, mentalStatusButton, physicalFitnessButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose2);


        physicalFitnessButton = findViewById(R.id.physicalFitnessButton);
        mentalStatusButton= findViewById(R.id.mentalStatusButton);
        consultationButton = findViewById(R.id.consultationButton);
        logoutButton = findViewById(R.id.logoutButton);


        physicalFitnessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChooseActivity.this, HomeActivity.class));

            }
        });
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChooseActivity.this,LoginActivity.class));

            }
        });

        mentalStatusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChooseActivity.this,MedDoc.class));

            }
        });




            }
}