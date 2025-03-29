package com.example.yogademoapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
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
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;

public class GreenCard extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private boolean isEditing = false; // Track if the user is in edit mode or not
    private String defaultTelephone = "0712671173";
    private String defaultAddress = "Nairobi 220022";
    private String defaultLocation = "Nairobi";

    private EditText telephoneEditText;
    private EditText addressEditText;
    private EditText locationEditText;

    private static final int PICK_IMAGE_REQUEST = 1;

    private ImageView profileImage;
    private SharedPreferences sharedPreferences;
    private FirebaseFirestore db;
    private StorageReference storageRef;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_green_card);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference("profile_images");
        currentUser = mAuth.getCurrentUser();
        sharedPreferences = getSharedPreferences("UserProfile", MODE_PRIVATE);
        profileImage = findViewById(R.id.imageView);

        // Load saved image
        loadProfileImage();

        // Open gallery when image is clicked
        profileImage.setOnClickListener(v -> openGallery());

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
        TextView emailTextView = findViewById(R.id.textviewemail);

        // Store the email in SharedPreferences
        if (currentUser != null) {
            String userEmail = currentUser.getEmail();
            if (userEmail != null) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("userEmail", userEmail); // Save email
                editor.apply();
                emailTextView.setText(userEmail); // Display the email
                Toast.makeText(this, "Email saved: " + userEmail, Toast.LENGTH_SHORT).show(); // Debugging
            }
        }

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

        // Navigate to the notifications page
        CardView settingsCardView = findViewById(R.id.notifications);
        settingsCardView.setOnClickListener(v -> {
            Intent intent = new Intent(GreenCard.this, Not.class);

            // Retrieve email from TextView
            String email = emailTextView.getText().toString();
            intent.putExtra("EMAIL", email);

            // Retrieve profile image resource
            ImageView profileImageView = findViewById(R.id.imageView);
            profileImageView.setDrawingCacheEnabled(true);
            profileImageView.buildDrawingCache();
            Bitmap bitmap = profileImageView.getDrawingCache();

            // Convert Bitmap to ByteArray
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] imageBytes = byteArrayOutputStream.toByteArray();
            intent.putExtra("PROFILE_IMAGE", imageBytes);

            startActivity(intent);
        });


        CardView settingprofpayment = findViewById(R.id.profpayment);
        settingprofpayment.setOnClickListener(v -> {
            Intent intent = new Intent(GreenCard.this, profpayment.class);

            // Retrieve email from TextView
            String email = emailTextView.getText().toString();
            intent.putExtra("EMAIL", email);

            // Retrieve profile image resource
            ImageView profileImageView = findViewById(R.id.imageView);
            profileImageView.setDrawingCacheEnabled(true);
            profileImageView.buildDrawingCache();
            Bitmap bitmap = profileImageView.getDrawingCache();

            // Convert Bitmap to ByteArray
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] imageBytes = byteArrayOutputStream.toByteArray();
            intent.putExtra("PROFILE_IMAGE", imageBytes);

            startActivity(intent);
        });
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            profileImage.setImageURI(imageUri);

            // Save the image in Firebase Storage & Firestore
            uploadImageToFirebase(imageUri);
        }
    }

    private void uploadImageToFirebase(Uri imageUri) {
        if (currentUser == null) return;

        StorageReference fileRef = storageRef.child(currentUser.getUid() + ".jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(taskSnapshot ->
                        fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            String imageUrl = uri.toString();

                            // Save to Firestore
                            saveToFirestore(imageUrl);

                            // Save to SharedPreferences
                            saveProfileImage(imageUrl);

                            // Load with Glide
                            Glide.with(this).load(imageUrl).into(profileImage);

                            Toast.makeText(this, "Profile image updated!", Toast.LENGTH_SHORT).show();
                        }))
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }

    private void saveToFirestore(String imageUrl) {
        if (currentUser == null) return;

        DocumentReference userRef = db.collection("users").document(currentUser.getUid());
        userRef.update("profileImageUrl", imageUrl)
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed to save image in Firestore", Toast.LENGTH_SHORT).show()
                );
    }

    private void saveProfileImage(String imageUri) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("profileImageUri", imageUri);
        editor.apply();
    }

    private void loadProfileImage() {
        String savedImageUri = sharedPreferences.getString("profileImageUri", null);
        if (savedImageUri != null) {
            // Load from SharedPreferences
            Glide.with(this).load(savedImageUri).into(profileImage);
        } else if (currentUser != null) {
            // If not found, load from Firestore
            db.collection("users").document(currentUser.getUid())
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String imageUrl = documentSnapshot.getString("profileImageUrl");
                            if (imageUrl != null) {
                                saveProfileImage(imageUrl);
                                Glide.with(this).load(imageUrl).into(profileImage);
                            }
                        }
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(this, "Failed to load image from Firestore", Toast.LENGTH_SHORT).show()
                    );
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
 //