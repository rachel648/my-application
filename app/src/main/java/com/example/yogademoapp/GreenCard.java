package com.example.yogademoapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
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
        TextView emailTextView = findViewById(R.id.textviewemail); // Ensure you have a TextView for displaying the email
        imageView = findViewById(R.id.imageView);

        // Load user email from Firebase
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            emailTextView.setText(currentUser.getEmail());
        }

        // Load saved profile details from Firestore
        loadProfileDetails();

        // Image click to open gallery
        imageView.setOnClickListener(v -> {
            if (isEditing) {
                // Only open the gallery if in edit mode
                openGallery();
            }
        });

        // Add click listener to the edit button for profile editing
        findViewById(R.id.editButton).setOnClickListener(v -> toggleEditMode());

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
            Log.d("ImageUpload", "Selected Image URI: " + selectedImageUri.toString());

            // Set the selected image as the ImageView's source
            imageView.setImageURI(selectedImageUri);

            // Upload this URI to Firebase Storage
            uploadImageToFirebase();
        } else {
            Log.e("ImageUpload", "Failed to get image from gallery");
        }
    }

    private void uploadImageToFirebase() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null && selectedImageUri != null) {
            StorageReference storageRef = storage.getReference().child("profile_images/" + user.getUid() + ".jpg");
            storageRef.putFile(selectedImageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        Log.d("ImageUpload", "Image uploaded successfully");
                        storageRef.getDownloadUrl().addOnCompleteListener(uriTask -> {
                            if (uriTask.isSuccessful()) {
                                String imageUrl = uriTask.getResult().toString();
                                Log.d("ImageUpload", "Download URL: " + imageUrl);
                                saveImageUri(imageUrl);
                            } else {
                                Log.e("ImageUpload", "Failed to get download URL", uriTask.getException());
                                Toast.makeText(GreenCard.this, "Failed to retrieve image URL", Toast.LENGTH_SHORT).show();
                            }
                        });
                    })
                    .addOnFailureListener(exception -> {
                        Log.e("ImageUpload", "Upload failed", exception);
                        Toast.makeText(GreenCard.this, "Image upload failed: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            Log.e("ImageUpload", "User or image URI is null");
            Toast.makeText(GreenCard.this, "User or image URI is null", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveImageUri(String imageUrl) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            DocumentReference userRef = db.collection("users").document(user.getUid());
            userRef.update("profile_image", imageUrl)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d("ImageUpload", "Profile image URL saved to Firestore");
                            Toast.makeText(GreenCard.this, "Profile image updated", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e("ImageUpload", "Failed to save image URL to Firestore", task.getException());
                            Toast.makeText(GreenCard.this, "Failed to update profile image", Toast.LENGTH_SHORT).show();
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
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {
                        String telephone = document.getString("telephone");
                        String address = document.getString("address");
                        String location = document.getString("location");
                        String profileImageUrl = document.getString("profile_image");

                        // Set the data in the fields
                        telephoneEditText.setText(telephone != null ? telephone : defaultTelephone);
                        addressEditText.setText(address != null ? address : defaultAddress);
                        locationEditText.setText(location != null ? location : defaultLocation);

                        // Set the profile image if available
                        if (profileImageUrl != null) {
                            Glide.with(this).load(profileImageUrl).into(imageView);
                        }
                    }
                } else {
                    Log.e("Firestore", "Failed to load profile details", task.getException());
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
                        } else {
                            Log.e("Firestore", "Failed to update profile", task.getException());
                            Toast.makeText(GreenCard.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}