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
        int imageId = getIntent().getIntExtra("imageid", R.drawable.man1);
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
        String scheduledTime = sharedPreferences.getString("scheduledTime", "Not Scheduled");
        String dayOfWeek = sharedPreferences.getString("dayOfWeek", "Unknown");
        String trainFees = sharedPreferences.getString("trainFees", "0");

        // Display details
        consultantEmailTextView.setText(consultantEmail);
        patientEmailTextView.setText("Patient Email: " + patientEmail);
        scheduleTextView.setText("Scheduled Time: " + scheduledTime);
        dayTextView.setText("Day: " + dayOfWeek);
        feesTextView.setText("Fees Paid: " + trainFees);

        // ImageView Click Listener for picking image
        profileImageView.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, IMAGE_PICKER_REQUEST_CODE);
        });

        acceptButton.setOnClickListener(v -> {
            sendBookingConfirmationEmail(patientEmail, scheduledTime, dayOfWeek);
            Toast.makeText(accept.this, "Accepted", Toast.LENGTH_SHORT).show();
        });

        rejectButton.setOnClickListener(v -> {
            sendRescheduleRequestEmail(patientEmail, scheduledTime, dayOfWeek);
            Toast.makeText(accept.this, "Reschedule message successfully sent to patient", Toast.LENGTH_SHORT).show();
        });
    }

    private void sendBookingConfirmationEmail(String email, String time, String day) {
        String subject = "Your Booking Confirmation";
        String body = "Your booking has been successfully scheduled!\n\n" +
                "Details:\n" +
                "Day: " + day + "\n" +
                "Time: " + time + "\n\n" +
                "Thank you for choosing our service.";

        sendEmail(email, subject, body);
    }

    private void sendRescheduleRequestEmail(String email, String time, String day) {
        String subject = "Booking Reschedule Request";
        String body = "We regret to inform you that your scheduled booking needs to be rescheduled.\n\n" +
                "Original Booking Details:\n" +
                "Day: " + day + "\n" +
                "Time: " + time + "\n\n" +
                "Please contact us to arrange a new appointment time.\n" +
                "We apologize for any inconvenience caused.";

        sendEmail(email, subject, body);
    }

    private void sendEmail(String email, String subject, String body) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("message/rfc822");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, body);

        try {
            startActivity(Intent.createChooser(emailIntent, "Send email using..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "No email clients installed.", Toast.LENGTH_SHORT).show();
        }
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
                            databaseReference.child("profileImage").setValue(uri.toString());
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