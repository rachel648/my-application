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

public class LoginActivity extends AppCompatActivity {
    EditText edEmail, edPassword;  //ed means edit text
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


    /*  The following are methods:
        onCreate(Bundle savedInstanceState)
        onClick(View view) (anonymous inner class inside onCreate)
        onClick(View view) (anonymous inner class inside tv.setOnClickListener)*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        //objects creation
        edEmail = findViewById(R.id.editTextEmail);/* edUsername,edPassword,btn,tv is a Member variable i.e the data defined by the class*/
        edPassword = findViewById(R.id.editTextPassword);
        btn = findViewById(R.id.buttonLogin); /*Btn for button*/
        tv= findViewById(R.id.textViewBooking);/*tv for text view*/
        //classes creation
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = edEmail.getText().toString();
                String Password = edPassword.getText().toString();
                // Database db = new Database(getApplicationContext(),"YogaDemo",null ,1);

                if (email.length() == 0 || Password.length() == 0) {
                    Toast.makeText(getApplicationContext(), ("Please fill all the details"), Toast.LENGTH_SHORT).show();
                } else {

                    // Use Firebase Authentication to sign in
                    mAuth.signInWithEmailAndPassword(email, Password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {

                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success

                                // if (db.login(Username,Password)==1);
                                Toast.makeText(getApplicationContext(), ("Login Success"), Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(LoginActivity.this, ChooseActivity.class));
                    /*  }else{
                    Toast.makeText(getApplicationContext(), ("invalid username or password"), Toast.LENGTH_SHORT).show();
                    }   */
                            } else {

                                // Sign in failed
                                Toast.makeText(getApplicationContext(), "Login failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        }

                    });

                }
            }
            });
                    tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                        }
                    });
                }

            }
