package com.example.yogademoapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class GreenCard extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_green_card);

        mAuth = FirebaseAuth.getInstance();

        EditText passwordEditText = findViewById(R.id.passwordEditText);
        LinearLayout passwordChangeSection = findViewById(R.id.passwordChangeSection);
        EditText oldPasswordEditText = findViewById(R.id.oldPassword);
        EditText newPasswordEditText = findViewById(R.id.newPassword);
        Button changePasswordButton = findViewById(R.id.changePasswordButton);

        // Toggle visibility of the password change section
        passwordEditText.setOnClickListener(v -> {
            if (passwordChangeSection.getVisibility() == View.GONE) {
                passwordChangeSection.setVisibility(View.VISIBLE);
            } else {
                passwordChangeSection.setVisibility(View.GONE);
            }
        });

        // Handle password change
        changePasswordButton.setOnClickListener(v -> {
            String oldPassword = oldPasswordEditText.getText().toString();
            String newPassword = newPasswordEditText.getText().toString();

            if (oldPassword.isEmpty() || newPassword.isEmpty()) {
                Toast.makeText(GreenCard.this, "Please fill in both fields", Toast.LENGTH_SHORT).show();
                return;
            }

            FirebaseUser user = mAuth.getCurrentUser();
            if (user != null) {
                String email = user.getEmail();
                if (email != null) {
                    // Re-authenticate the user
                    user.reauthenticate(EmailAuthProvider.getCredential(email, oldPassword))
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    user.updatePassword(newPassword)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(GreenCard.this, "Password updated successfully", Toast.LENGTH_SHORT).show();
                                                        // Optionally, log out the user or redirect to login page
                                                        mAuth.signOut();
                                                        finish(); // Close the current activity
                                                    } else {
                                                        Toast.makeText(GreenCard.this, "Password update failed", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                } else {
                                    Toast.makeText(GreenCard.this, "Re-authentication failed", Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    Toast.makeText(GreenCard.this, "Email not found", Toast.LENGTH_SHORT).show();
                }
            }
        });
        // Initialize CardViews
        CardView settingsCardView = findViewById(R.id.notifications);
        // Set up the click listener for the settings CardView
        settingsCardView.setOnClickListener(v -> {
            Intent intent = new Intent(GreenCard.this, Not.class); // Start the Not activity
            startActivity(intent);
        });
    }
}
