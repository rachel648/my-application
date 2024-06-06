package com.example.yogademoapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;


public class RegisterActivity extends AppCompatActivity {
    EditText edUsername, edEmail, edPassword, edConfirmPassword;
    Button btn,consultantBtn;
    TextView tv;

    FirebaseAuth mAuth;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){

        }
    }

    //  FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        edUsername = findViewById(R.id.editTextBookingName);
        edPassword = findViewById(R.id.editTextBookingPincode);
        edEmail = findViewById(R.id.editTextBookingAddress);
        edConfirmPassword = findViewById(R.id.editTextContactNumber);
        btn = findViewById(R.id.ButtonBooking);
        consultantBtn = findViewById(R.id.ButtonConsultant);
        tv = findViewById(R.id.textViewBooking);

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser("patient");
            }
        });

        consultantBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser("consultant");
            }
        });
    }

    private void registerUser(String userType) {
        // Obtain data entered
        String Username = edUsername.getText().toString();
        String Password = edPassword.getText().toString();
        String email = edEmail.getText().toString();
        String ConfirmPassword = edConfirmPassword.getText().toString();

        if (Username.length() == 0 || email.length() == 0 || Password.length() == 0 || ConfirmPassword.length() == 0) {
            Toast.makeText(getApplicationContext(), "Please fill all the details", Toast.LENGTH_SHORT).show();
        } else {
            if (Password.equals(ConfirmPassword)) {
                if (isValid(Password)) {
                    // Create a new user account with Firebase Authentication
                    mAuth.createUserWithEmailAndPassword(email, Password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // User registration successful
                                FirebaseUser user = mAuth.getCurrentUser();
                                if (user != null) {
                                    // Update user profile with display name
                                    user.updateProfile(new UserProfileChangeRequest.Builder()
                                            .setDisplayName(Username)
                                            .build());

                                    // Save user type to Firebase Database
                                    String userId = user.getUid();
                                    FirebaseDatabase.getInstance().getReference("Users")
                                            .child(userId)
                                            .setValue(userType);

                                    Toast.makeText(getApplicationContext(), "Record Inserted", Toast.LENGTH_SHORT).show();

                                    // Redirect based on user type
                                    if ("consultant".equals(userType)) {
                                        startActivity(new Intent(RegisterActivity.this, Agent.class));
                                    } else if ("patient".equals(userType)) {
                                        startActivity(new Intent(RegisterActivity.this, ChooseActivity.class));
                                    }
                                }
                            } else {
                                // Registration failed
                                Toast.makeText(getApplicationContext(), "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "Password must contain at least 8 characters, a letter, a digit, and a special character", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Password and Confirm password do not match", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public static boolean isValid(String passwordhere) {
        int f1 = 0, f2 = 0, f3 = 0;
        if (passwordhere.length() < 8) {
            return false;
        } else {
            for (int p = 0; p < passwordhere.length(); p++) {
                if (Character.isLetter(passwordhere.charAt(p))) {
                    f1 = 1;
                }
            }
            for (int r = 0; r < passwordhere.length(); r++) {
                if (Character.isDigit(passwordhere.charAt(r))) {
                    f2 = 1;
                }
            }
            for (int s = 0; s < passwordhere.length(); s++) {
                char c = passwordhere.charAt(s);
                if (c >= 33 && c <= 46 || c == 64) {
                    f3 = 1;
                }
            }
            return f1 == 1 && f2 == 1 && f3 == 1;
        }
    }
}
