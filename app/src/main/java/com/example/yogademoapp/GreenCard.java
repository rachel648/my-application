package com.example.yogademoapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

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
    private FirebaseFirestore db;
    private FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_green_card);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        // Initialize views
        telephoneEditText = findViewById(R.id.textBox1);
        addressEditText = findViewById(R.id.textBox2);
        locationEditText = findViewById(R.id.textBox3);
        LinearLayout passwordChangeSection = findViewById(R.id.passwordChangeSection);
        EditText passwordEditText = findViewById(R.id.passwordEditText);
        EditText oldPasswordEditText = findViewById(R.id.oldPassword);
        EditText newPasswordEditText = findViewById(R.id.newPassword);
        TextView emailTextView = findViewById(R.id.textviewemail); // Ensure you have a TextView for displaying the email
        imageView = findViewById(R.id.imageView);

        // Load user email from Firebase
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            emailTextView.setText(currentUser.getEmail());
        }

        // Load saved profile details from Firestore
        loadProfileDetails();

        // Toggle visibility of the password change section
        passwordEditText.setOnClickListener(v -> {
            if (passwordChangeSection.getVisibility() == View.GONE) {
                passwordChangeSection.setVisibility(View.VISIBLE);
            } else {
                passwordChangeSection.setVisibility(View.GONE);
            }
        });

        // Handle password change
        findViewById(R.id.changePasswordButton).setOnClickListener(v -> changePassword(oldPasswordEditText, newPasswordEditText));

        // Add click listener to the edit button for profile editing
        findViewById(R.id.editButton).setOnClickListener(v -> toggleEditMode());

        // Image click to open gallery
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

            // You can upload this URI to Firebase Storage
            uploadImageToFirebase();
        }
    }

    private void uploadImageToFirebase() {
        // Get the current user
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null && selectedImageUri != null) {
            StorageReference storageRef = storage.getReference().child("profile_images/" + user.getUid() + ".jpg");
            storageRef.putFile(selectedImageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        // Handle success
                        storageRef.getDownloadUrl().addOnCompleteListener(uriTask -> {
                            if (uriTask.isSuccessful()) {
                                String imageUrl = uriTask.getResult().toString();
                                // Save the image URL to Firestore
                                saveImageUri(imageUrl);
                            } else {
                                // Handle failure to get download URL
                                Log.e("ImageUpload", "Failed to get download URL", uriTask.getException());
                                Toast.makeText(GreenCard.this, "Failed to retrieve image URL", Toast.LENGTH_SHORT).show();
                            }
                        });
                    })
                    .addOnFailureListener(exception -> {
                        // Log the error
                        Log.e("ImageUpload", "Upload failed", exception);
                        Toast.makeText(GreenCard.this, "Image upload failed: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            // Handle case where user is null or selectedImageUri is null
            Log.e("ImageUpload", "User or image URI is null");
            Toast.makeText(GreenCard.this, "User or image URI is null", Toast.LENGTH_SHORT).show();
        }
    }


    private void saveImageUri(String imageUrl) {
        // Get the current user
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            DocumentReference userRef = db.collection("users").document(user.getUid());
            userRef.update("profile_image", imageUrl)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(GreenCard.this, "Profile image updated", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void loadProfileDetails() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            DocumentReference userRef = db.collection("users").document(user.getUid());
            userRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    // Get the document snapshot
                    if (task.getResult() != null && task.getResult().exists()) {
                        String telephone = task.getResult().getString("telephone");
                        String address = task.getResult().getString("address");
                        String location = task.getResult().getString("location");
                        String profileImageUrl = task.getResult().getString("profile_image");

                        // Set the data in the fields
                        telephoneEditText.setText(telephone != null ? telephone : defaultTelephone);
                        addressEditText.setText(address != null ? address : defaultAddress);
                        locationEditText.setText(location != null ? location : defaultLocation);

                        // Set the profile image if available
                        if (profileImageUrl != null) {
                            imageView.setImageURI(Uri.parse(profileImageUrl));
                        }
                    }
                }
            });
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
            user.updatePassword(newPassword)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(GreenCard.this, "Password updated successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(GreenCard.this, "Password update failed", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void toggleEditMode() {
        if (isEditing) {
            // If in edit mode, save the new values to Firebase
            String newTelephone = telephoneEditText.getText().toString();
            String newAddress = addressEditText.getText().toString();
            String newLocation = locationEditText.getText().toString();

            if (newTelephone.isEmpty() || newAddress.isEmpty() || newLocation.isEmpty()) {
                Toast.makeText(GreenCard.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Save new values to Firestore
            saveProfileDetails(newTelephone, newAddress, newLocation);
        } else {
            // If not in edit mode, allow editing
            telephoneEditText.setText("");
            addressEditText.setText("");
            locationEditText.setText("");

            Toast.makeText(GreenCard.this, "You can now edit the fields", Toast.LENGTH_SHORT).show();
        }

        // Toggle edit mode
        isEditing = !isEditing;
    }

    private void saveProfileDetails(String telephone, String address, String location) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            DocumentReference userRef = db.collection("users").document(user.getUid());
            userRef.update("telephone", telephone, "address", address, "location", location)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(GreenCard.this, "Profile updated", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}
