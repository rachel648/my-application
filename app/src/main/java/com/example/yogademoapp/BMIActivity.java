package com.example.yogademoapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class BMIActivity extends AppCompatActivity {

    private EditText editTextWeight, editTextHeight;
    private Button calculateButton, btnBack;
    private TextView resultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmiactivity);

        // Initialize UI components
        editTextWeight = findViewById(R.id.editTextWeight);
        editTextHeight = findViewById(R.id.editTextHeight);
        calculateButton = findViewById(R.id.CalculateButton);
        resultTextView = findViewById(R.id.ResultTextView);
        btnBack = findViewById(R.id.btnback);

        // Set click listener for the Calculate button
        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateBMI(); // Directly call calculateBMI
            }
        });

        // Set click listener for the Back button
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToChooseActivity(); // Navigate back to ChooseActivity
            }
        });
    }

    private void calculateBMI() {
        String weightString = editTextWeight.getText().toString().trim();
        String heightString = editTextHeight.getText().toString().trim();

        // Validate input
        if (!weightString.isEmpty() && !heightString.isEmpty()) {
            try {
                float weight = Float.parseFloat(weightString);
                float height = Float.parseFloat(heightString);

                float bmi = weight / (height * height);
                String bmiResult = String.format("%.2f", bmi);
                resultTextView.setText("BMI: " + bmiResult);

                // Show alert based on BMI
                if (bmi < 18.5 || bmi > 25.0) {
                    showAlertDialog("Abnormal BMI", "Your BMI indicates an abnormal range.");
                } else {
                    showAlertDialog("Normal BMI", "Your BMI is within the normal range.");
                }
            } catch (NumberFormatException e) {
                resultTextView.setText("Invalid input. Please enter numeric values.");
            }
        } else {
            resultTextView.setText("Please enter weight and height.");
        }
    }

    private void showAlertDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss(); // Dismiss the dialog
                    }
                })
                .create()
                .show();
    }

    private void navigateToChooseActivity() {
        Intent intent = new Intent(BMIActivity.this, ChooseActivity.class);
        startActivity(intent); // Start ChooseActivity
    }
}
