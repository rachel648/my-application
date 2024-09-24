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
    private boolean isEditing = false; // Track if the user is in edit mode or not
    private String defaultTelephone = "0712671173";
    private String defaultAddress = "Nairobi 220022";
    private String defaultLocation = "Nairobi";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_green_card);

        mAuth = FirebaseAuth.getInstance();

        // Password change functionality
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
                                                        mAuth.signOut(); // Optionally log out the user after changing the password
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

        // Profile editing functionality
        EditText telephoneEditText = findViewById(R.id.textBox1);
        EditText addressEditText = findViewById(R.id.textBox2);
        EditText locationEditText = findViewById(R.id.textBox3);
        Button editButton = findViewById(R.id.editButton);

        // Set default values for telephone, address, and location fields
        telephoneEditText.setText(defaultTelephone);
        addressEditText.setText(defaultAddress);
        locationEditText.setText(defaultLocation);

        // Make the fields non-editable initially
        setFieldsEditable(false, telephoneEditText, addressEditText, locationEditText);

        // Add click listener to the edit button for profile editing
        editButton.setOnClickListener(v -> {
            if (isEditing) {
                // If in edit mode, save the new values and make fields non-editable
                String newTelephone = telephoneEditText.getText().toString();
                String newAddress = addressEditText.getText().toString();
                String newLocation = locationEditText.getText().toString();

                if (newTelephone.isEmpty() || newAddress.isEmpty() || newLocation.isEmpty()) {
                    Toast.makeText(GreenCard.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Save new values as the new defaults
                defaultTelephone = newTelephone;
                defaultAddress = newAddress;
                defaultLocation = newLocation;

                // Make fields non-editable
                setFieldsEditable(false, telephoneEditText, addressEditText, locationEditText);
                Toast.makeText(GreenCard.this, "Profile updated", Toast.LENGTH_SHORT).show();

            } else {
                // If not in edit mode, allow editing
                telephoneEditText.setText("");
                addressEditText.setText("");
                locationEditText.setText("");

                // Enable editing
                setFieldsEditable(true, telephoneEditText, addressEditText, locationEditText);
                Toast.makeText(GreenCard.this, "You can now edit the fields", Toast.LENGTH_SHORT).show();
            }

            // Toggle edit mode
            isEditing = !isEditing;
        });

        // Navigate to the notifications page
        CardView settingsCardView = findViewById(R.id.notifications);
        settingsCardView.setOnClickListener(v -> {
            Intent intent = new Intent(GreenCard.this, Not.class); // Start the Not activity
            startActivity(intent);
        });
    }

    // Helper method to toggle editability of fields
    private void setFieldsEditable(boolean editable, EditText... editTexts) {
        for (EditText editText : editTexts) {
            editText.setFocusableInTouchMode(editable);
            editText.setFocusable(editable);
            editText.setClickable(editable);
        }
    }
}
