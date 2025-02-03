package com.example.yogademoapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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

    private static final int PICK_IMAGE = 1;
    private FirebaseAuth mAuth;
    private boolean isEditing = false; // Track if the user is in edit mode or not
    private String defaultTelephone = "0712671173";
    private String defaultAddress = "Nairobi 220022";
    private String defaultLocation = "Nairobi";

    private EditText telephoneEditText;
    private EditText addressEditText;
    private EditText locationEditText;

    private ImageView imageView;
    private Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_green_card);

        mAuth = FirebaseAuth.getInstance();

        // Initialize views
        telephoneEditText = findViewById(R.id.textBox1);
        addressEditText = findViewById(R.id.textBox2);
        locationEditText = findViewById(R.id.textBox3);
        Button editButton = findViewById(R.id.editButton);
        LinearLayout passwordChangeSection = findViewById(R.id.passwordChangeSection);
        EditText passwordEditText = findViewById(R.id.passwordEditText);
        EditText oldPasswordEditText = findViewById(R.id.oldPassword);
        EditText newPasswordEditText = findViewById(R.id.newPassword);
        Button changePasswordButton = findViewById(R.id.changePasswordButton);
        TextView emailTextView = findViewById(R.id.textviewemail); // Ensure you have a TextView for displaying the email
        imageView = findViewById(R.id.imageView);

        // Load saved email from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserProfile", MODE_PRIVATE);
        String savedEmail = sharedPreferences.getString("userEmail", "johndoe@gmail.com"); // Default email
        emailTextView.setText(savedEmail); // Display the email

        // Load saved details from SharedPreferences
        loadProfileDetails();

        // Make the fields non-editable initially
        setFieldsEditable(false, telephoneEditText, addressEditText, locationEditText);

        // Toggle visibility of the password change section
        passwordEditText.setOnClickListener(v -> {
            if (passwordChangeSection.getVisibility() == View.GONE) {
                passwordChangeSection.setVisibility(View.VISIBLE);
            } else {
                passwordChangeSection.setVisibility(View.GONE);
            }
        });

        // Handle password change
        changePasswordButton.setOnClickListener(v -> changePassword(oldPasswordEditText, newPasswordEditText));

        // Add click listener to the edit button for profile editing
        editButton.setOnClickListener(v -> toggleEditMode());


        imageView.setOnClickListener(v -> {
            if (isEditing) {
                // Only open the gallery if in edit mode
                openGallery();
            }
        });

        // Navigate to the notifications page
        CardView settingsCardView = findViewById(R.id.notifications);
        settingsCardView.setOnClickListener(v -> {
            Intent intent = new Intent(GreenCard.this, Not.class); // Start the Not activity
            startActivity(intent);
        });

        CardView settingprofpayment = findViewById(R.id.profpayment);
        settingprofpayment.setOnClickListener(v -> {
            Intent intent = new Intent(GreenCard.this, profpayment.class);
            startActivity(intent);
        });

    }


    private void openGallery() {
        // Intent to open the device's gallery
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            // Get the image URI from the gallery selection
            selectedImageUri = data.getData();

            // Set the selected image as the ImageView's source
            imageView.setImageURI(selectedImageUri);

            // You can save this URI to SharedPreferences or Firebase
            saveImageUri(selectedImageUri);
        }
    }

    private void saveImageUri(Uri imageUri) {
        // Save the image URI in SharedPreferences
        SharedPreferences preferences = getSharedPreferences("UserProfile", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("profile_image", imageUri.toString());
        editor.apply();
    }

    private void loadImage() {
        // Load the saved image URI if available
        SharedPreferences preferences = getSharedPreferences("UserProfile", MODE_PRIVATE);
        String imageUriString = preferences.getString("profile_image", null);

        if (imageUriString != null) {
            Uri imageUri = Uri.parse(imageUriString);
            imageView.setImageURI(imageUri);
        }
    }

    private void changePassword(EditText oldPasswordEditText, EditText newPasswordEditText) {
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
    }

    private void toggleEditMode() {
        if (isEditing) {
            // If in edit mode, save the new values and make fields non-editable
            String newTelephone = telephoneEditText.getText().toString();
            String newAddress = addressEditText.getText().toString();
            String newLocation = locationEditText.getText().toString();

            if (newTelephone.isEmpty() || newAddress.isEmpty() || newLocation.isEmpty()) {
                Toast.makeText(GreenCard.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Save new values to SharedPreferences
            saveProfileDetails(newTelephone, newAddress, newLocation);

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
    }

    private void saveProfileDetails(String telephone, String address, String location) {
        SharedPreferences sharedPreferences = getSharedPreferences("UserProfile", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("telephone", telephone);
        editor.putString("address", address);
        editor.putString("location", location);
        editor.apply(); // Save changes
    }

    private void loadProfileDetails() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserProfile", MODE_PRIVATE);
        String savedTelephone = sharedPreferences.getString("telephone", defaultTelephone);
        String savedAddress = sharedPreferences.getString("address", defaultAddress);
        String savedLocation = sharedPreferences.getString("location", defaultLocation);

        telephoneEditText.setText(savedTelephone);
        addressEditText.setText(savedAddress);
        locationEditText.setText(savedLocation);
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
