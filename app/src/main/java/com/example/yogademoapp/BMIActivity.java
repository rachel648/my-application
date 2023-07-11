package com.example.yogademoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AlertDialog;
import android.content.DialogInterface;
public class BMIActivity extends AppCompatActivity {

    Button btn;

    TextView tv;

    EditText ed;
    private EditText editTextWeight, editTextHeight;
    private Button CalculateButton;
    private TextView ResultTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmiactivity);

        editTextWeight = findViewById(R.id.editTextWeight);
        editTextHeight = findViewById(R.id.editTextHeight);
        CalculateButton = findViewById(R.id.CalculateButton);
        ResultTextView = findViewById(R.id.ResultTextView);

        CalculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateBMI();
            }
        });
    }

    private void calculateBMI() {
        String weightString = editTextWeight.getText().toString();
        String heightString = editTextHeight.getText().toString();

        if (!weightString.isEmpty() && !heightString.isEmpty()) {
            float weight = Float.parseFloat(weightString);
            float height = Float.parseFloat(heightString);

            float bmi = weight / (height * height);

            String bmiResult = String.format("%.2f", bmi);

            ResultTextView.setText("BMI: " + bmiResult);
            // Display an alert for normal BMI or abnormal BMI
            if (bmi < 18.5 || bmi > 25.0) {
                showAlertDialog("Abnormal BMI", "Your BMI indicates an abnormal range.");
            } else {
                showAlertDialog("Normal BMI", "Your BMI is within the normal range.");
            }
        } else {
            ResultTextView.setText("Please enter weight and height.");
        }
    }

    private void showAlertDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Dismiss the dialog
                        dialog.dismiss();
                    }
                })
                .create()
                .show();
    }
}


