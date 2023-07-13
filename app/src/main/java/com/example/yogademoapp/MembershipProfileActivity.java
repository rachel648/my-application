package com.example.yogademoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;import android.widget.ArrayAdapter;


public class MembershipProfileActivity extends AppCompatActivity {

    Button btn;
    EditText ed;
    Spinner sp;

    private EditText editTextName, editTextEmail, editTextPassword, editTextUsername;
    private Spinner spinnerMembershipType;
    private Button buttonSubmit, buttonSignup,buttonForgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_membership_profile);

        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        spinnerMembershipType = findViewById(R.id.spinnerMembershipType);
        buttonSubmit = findViewById(R.id.buttonSubmit);
        buttonForgotPassword=findViewById(R.id.buttonForgotPassword);
       // editTextUsername = findViewById(R.id.editTextUsername);
       editTextPassword = findViewById(R.id.editTextPassword);
        editTextPassword.setHintTextColor(ColorStateList.valueOf(getResources().getColor(android.R.color.white)));

        buttonSignup = findViewById(R.id.buttonSignup);
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextName.getText().toString();
                String email = editTextEmail.getText().toString();
                String membershipType = spinnerMembershipType.getSelectedItem().toString();

                // Do something with the entered data, such as storing it in a database

                // Display a toast message to indicate successful submission
                Toast.makeText(
                        MembershipProfileActivity.this,
                        "Profile submitted: Name = " + name + ", Email = " + email + ", Membership Type = " + membershipType,
                        Toast.LENGTH_SHORT
                ).show();
            }
        });

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextName.getText().toString();
                String password = editTextPassword.getText().toString();

                // Perform login authentication logic
                if (username.equals("admin") && password.equals("password")) {
                    Toast.makeText(MembershipProfileActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                    // Perform any additional actions after successful login
                } else {
                    Toast.makeText(MembershipProfileActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });
        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the Register New User activity
                Intent intent = new Intent(MembershipProfileActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the Login activity
                Intent intent = new Intent(MembershipProfileActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        buttonForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implement the logic for forgot password functionality
                Toast.makeText(MembershipProfileActivity.this, "Forgot Password Clicked", Toast.LENGTH_SHORT).show();
                // Add your forgot password logic here
            }
        });


        // Add four types of membership to the spinner
        String[] membershipTypes = {"OneDayMembership", "WeekMembership","MonthMembership","YearMembership"};
      // ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, membershipTypes);
      //  spinnerMembershipType.setAdapter(adapter);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, membershipTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMembershipType.setAdapter(adapter);


    }
}



