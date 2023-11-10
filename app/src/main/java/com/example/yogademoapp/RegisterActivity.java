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


public class RegisterActivity extends AppCompatActivity {
    EditText edUsername, edEmail, edPassword, edConfirmPassword;
    Button btn;
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
        tv= findViewById(R.id.textViewBooking);

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Obtain data entered
                String Username = edUsername.getText().toString();
                String Password = edPassword.getText().toString();
                String email = edEmail.getText().toString();
                String ConfirmPassword = edConfirmPassword.getText().toString();
                Database db = new Database(getApplicationContext(),"YogaDemo",null ,1);

                if (Username.length() == 0 || email.length() == 0 || Password.length() == 0 || ConfirmPassword.length() == 0) {
                    Toast.makeText(getApplicationContext(), ("Please fill all the details"), Toast.LENGTH_SHORT).show();
                }
                else {
                    if (Password.compareTo(ConfirmPassword) == 0) {
                        if (isValid(Password)) {    /*if both passwords are same then check whether it's a valid password containing all specifications*/

                            // Create a new user account with Firebase Authentication
                            mAuth.createUserWithEmailAndPassword(email, Password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                // User registration successful


                                                // db.registar(Username,email,Password);
                                                Toast.makeText(getApplicationContext(), "Record Inserted", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
                                            } else {

                                                // Registration failed
                                                Toast.makeText(getApplicationContext(), "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                        }else {

                    Toast.makeText(getApplicationContext(),("Password must contain at least 8 characters,having letter,digit and alphabet"), Toast.LENGTH_SHORT).show();
                }
                    } else {
                        Toast.makeText(getApplicationContext(), ("Password and Confirm password do not match"), Toast.LENGTH_SHORT).show();

                    }

                }

            }

        });
    }


    /*  We check if password is valid by checking the length eg 8 characters,whether it has a digit, a letter,an alphabet, a special character
    * To do so we use a built in function as used below
    * if less eight return false
    * the one character must be between oscillators as below on the third "for"
    * if all flags are set we return true*/

    public static boolean isValid(String passwordhere) {
        int f1=0,f2=0,f3=0;
        if (passwordhere.length() < 8) {
            return false;
        } else {
            for(int p=0; p< passwordhere.length(); p++) {
                if (Character.isLetter(passwordhere.charAt(p))) {
                    f1 = 1;
                }
            }
            for (int r=0; r < passwordhere.length(); r++) {
                if (Character.isDigit(passwordhere.charAt(r))) {
                    f2 = 1;
                }
            }
            for (int s=0; s < passwordhere.length(); s++) {
                char c = passwordhere.charAt(s);
                if(c>=33&&c<=46||c==64){          /*oscillators*/
                      f3 = 1 ;
                }
            }
            /*flags*/
            return f1 == 1 && f2 == 1 && f3 == 1;
        }
            }


}

