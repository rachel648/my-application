package com.example.yogademoapp;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Payment extends AppCompatActivity {
    Toolbar toolbar;
    TextView TrainFees, Discount, PointsEarned, sub_total, checkOutButton;
    private EditText input;

    private static final int SMS_PERMISSION_REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        //Toolbar
        toolbar = findViewById(R.id.payment_toolbar);
        setSupportActionBar(toolbar); //inaharibu
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TrainFees = findViewById(R.id.textView12);
        Discount = findViewById(R.id.textView17);
        PointsEarned = findViewById(R.id.textView18);
       // total = findViewById(R.id.total_amt);
        sub_total = findViewById(R.id.sub_total);

        // Retrieve Train Fees value from the intent
        String trainFees = getIntent().getStringExtra("TrainFees");

        // Set Train Fees value in the TextView
        sub_total.setText(trainFees);


        checkOutButton = findViewById(R.id.pay_btn);
        checkOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Request the user's phone number when Check Out is clicked
                requestPhoneNumber();
            }
        });
    }

    private void requestPhoneNumber() {
        // Create an AlertDialog to collect the phone number
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Your Phone Number");

        input = new EditText(this); // Initialize input here
        input.setInputType(android.text.InputType.TYPE_CLASS_PHONE);
        builder.setView(input);

        builder.setPositiveButton("OK",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String phoneNumber = input.getText().toString();

                // Check for SMS permission
                if (ContextCompat.checkSelfPermission(Payment.this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                    sendPaymentRequestSMS(phoneNumber);
                } else {
                    // Request SMS permission from the user
                    ActivityCompat.requestPermissions(Payment.this, new String[]{Manifest.permission.SEND_SMS}, SMS_PERMISSION_REQUEST_CODE);
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void sendPaymentRequestSMS(String phoneNumber) {
        String message = "Payment for your booking with Life_Boost made .";

        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNumber, null, message, null, null);

        generateAndSaveReceipt(null, phoneNumber, message);
        // Implement your SMS sending logic here

        // You can also add code to handle SMS sent successfully or not.
        Toast.makeText(Payment.this, "Payment request sent!", Toast.LENGTH_SHORT).show();
    }

    private void generateAndSaveReceipt(String sender, String receiver, String message) {
        String receiptContent = "Payment Receipt\n" +
                "Date: " + getCurrentDateTime() + "\n" +
                "Amount: ksh 50.00\n" +
                "Transaction ID: ABC123\n" +
                "Thank you for your payment.\n" +
                "Sender: " + sender + "\n" +
                "Receiver: " + receiver + "\n" +
                "Message: " + message;

        saveReceiptToFile(receiptContent);
    }

    private String getCurrentDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }

    private void saveReceiptToFile(String receiptContent) {
        try {
            File directory = new File(getExternalFilesDir(null), "Receipts");
            if (!directory.exists()) {
                directory.mkdirs();
            }

            String fileName = "receipt_" + System.currentTimeMillis() + ".txt";

            File receiptFile = new File(directory, fileName);
            FileOutputStream outputStream = new FileOutputStream(receiptFile);
            outputStream.write(receiptContent.getBytes());
            outputStream.close();

            Log.d("Receipt", "Receipt saved: " + receiptFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Receipt", "Error saving receipt", e);
            Toast.makeText(this, "Error saving receipt", Toast.LENGTH_SHORT).show();
        }

    }

    // Handle permission request result
   // @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == SMS_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, send the SMS
                String phoneNumber = input.getText().toString();
                sendPaymentRequestSMS(phoneNumber);
                // Make sure to implement the SMS sending logic here


            } else {
                // Permission denied, handle accordingly
                Toast.makeText(this, "SMS permission denied.", Toast.LENGTH_SHORT).show();
            }

            ImageView backButton = findViewById(R.id.backButton);
            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Payment.this, ChooseActivity.class);
                    startActivity(intent);
                }
            });

        }


    }
}