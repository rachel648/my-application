package com.example.yogademoapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class accept extends AppCompatActivity {

    private static final int IMAGE_PICKER_REQUEST_CODE = 1;
    private ImageView profileImageView;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private String patientEmail, userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept);

        // Retrieve UI elements
        TextView consultantEmailTextView = findViewById(R.id.textviewConsultantEmail);
        TextView patientEmailTextView = findViewById(R.id.textviewPatientEmail);
        TextView clientTextView = findViewById(R.id.textviewclient);
        TextView scheduleTextView = findViewById(R.id.textviewschedule);
        TextView dayTextView = findViewById(R.id.textviewday);
        TextView feesTextView = findViewById(R.id.textviewfees);
        profileImageView = findViewById(R.id.imageView);
        Button acceptButton = findViewById(R.id.buttonAccept);
        Button rejectButton = findViewById(R.id.buttonReject);

        // Retrieve consultant details
        String consultantEmail = getIntent().getStringExtra("consultantEmail");
        if (consultantEmail == null) consultantEmail = "johndoe@gmail.com";

        // Retrieve image data from intent
        int imageId = getIntent().getIntExtra("imageid", R.drawable.babe3);
        String imageUrl = getIntent().getStringExtra("imageUrl");

        // Load the consultant image
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(this)
                    .load(imageUrl)
                    .placeholder(R.drawable.babe3)
                    .into(profileImageView);
        } else {
            profileImageView.setImageResource(imageId);
        }

        // Retrieve patient email from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserProfile", MODE_PRIVATE);
        patientEmail = sharedPreferences.getString("patientEmail", "Not Available");

        // Create a unique ID for Firebase (based on email)
        userId = patientEmail.replace(".", "_"); // Firebase does not allow dots in keys

        // Initialize Firebase Database and Storage references
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
        storageReference = FirebaseStorage.getInstance().getReference("profile_images/").child(userId + ".jpg");

        // Retrieve other details
        String patientName = sharedPreferences.getString("userName", "Unknown User");
        if (patientName.equals("Unknown User") && patientEmail.contains("@")) {
            patientName = patientEmail.split("@")[0]; // Extract username from email
        }
        String scheduledTime = sharedPreferences.getString("scheduledTime", "Not Scheduled");
        String dayOfWeek = sharedPreferences.getString("dayOfWeek", "Unknown");
        String trainFees = sharedPreferences.getString("trainFees", "0");

        // Display details
        consultantEmailTextView.setText(consultantEmail);
        patientEmailTextView.setText("Patient Email: " + patientEmail);
        clientTextView.setText("Client: " + patientName);
        scheduleTextView.setText("Scheduled Time: " + scheduledTime);
        dayTextView.setText("Day: " + dayOfWeek);
        feesTextView.setText("Fees Paid: " + trainFees);

        // ImageView Click Listener for picking image
        profileImageView.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, IMAGE_PICKER_REQUEST_CODE);
        });

        acceptButton.setOnClickListener(v -> Toast.makeText(accept.this, "Accepted", Toast.LENGTH_SHORT).show());

        rejectButton.setOnClickListener(v -> Toast.makeText(accept.this, "Reschedule message successfully sent to patient", Toast.LENGTH_SHORT).show());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_PICKER_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            uploadImageToFirebase(imageUri);
        }
    }

    private void uploadImageToFirebase(Uri imageUri) {
        storageReference.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> storageReference.getDownloadUrl()
                        .addOnSuccessListener(uri -> {
                            // Save image URL in Firebase Database
                            databaseReference.child("profileImage").setValue(uri.toString());

                            // Load image into ImageView
                            Picasso.get().load(uri).into(profileImageView);
                            Toast.makeText(accept.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                        }))
                .addOnFailureListener(e -> Toast.makeText(accept.this, "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void loadImageFromFirebase() {
        databaseReference.child("profileImage").get().addOnSuccessListener(snapshot -> {
            if (snapshot.exists()) {
                String imageUrl = snapshot.getValue(String.class);
                if (imageUrl != null && !imageUrl.isEmpty()) {
                    Picasso.get().load(imageUrl).into(profileImageView);
                }
            }
        });
    }
}