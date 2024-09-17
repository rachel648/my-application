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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    EditText edEmail, edPassword;  //ed means edit text
    Button btn;
    TextView tv;

    FirebaseAuth mAuth;

    @Override
    public void onStart() {
        super.onStart();
        // Initialize FirebaseAuth before using it
        mAuth = FirebaseAuth.getInstance();

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // You can redirect to the appropriate activity here if needed
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        edEmail = findViewById(R.id.editTextEmail);
        edPassword = findViewById(R.id.editTextPassword);
        btn = findViewById(R.id.buttonLogin);
        tv = findViewById(R.id.textViewBooking);

        // Handle Login button click
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edEmail.getText().toString();
                String password = edPassword.getText().toString();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please fill all the details", Toast.LENGTH_SHORT).show();
                } else {
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Login successful
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        if (user != null) {
                                            // Get user role from Firebase Database
                                            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
                                            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    // Make sure userType is not null
                                                    String userType = snapshot.getValue(String.class);
                                                    if (userType != null) {
                                                        if ("consultant".equals(userType)) {
                                                            startActivity(new Intent(LoginActivity.this, Agent.class));
                                                        } else if ("patient".equals(userType)) {
                                                            startActivity(new Intent(LoginActivity.this, ChooseActivity.class));
                                                        } else {
                                                            Toast.makeText(getApplicationContext(), "User type not recognized", Toast.LENGTH_SHORT).show();
                                                        }
                                                    } else {
                                                        Toast.makeText(getApplicationContext(), "User type is null", Toast.LENGTH_SHORT).show();
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {
                                                    Toast.makeText(getApplicationContext(), "Failed to read user type", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    } else {
                                        // Login failed
                                        Toast.makeText(getApplicationContext(), "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

        // Handle Register link click
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }
}
