// UploadProfileActivity.java
package com.example.yogademoapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UploadProfileActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView profileImageView;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_profile);

        profileImageView = findViewById(R.id.profileImageView);
        Button chooseImageButton = findViewById(R.id.chooseImageButton);

        chooseImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImageChooser();
            }
        });
    }

    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImageUri = data.getData();
            // You can display the selected image in your ImageView or upload it to Firebase Storage
            profileImageView.setImageURI(selectedImageUri);


            // Get the currently logged-in user
            FirebaseUser user = mAuth.getCurrentUser();

          /*     if (user != null) {
                // Save the image with the user's UID as the filename
                saveImageToStorage(selectedImageUri, user.getUid());
            }
        }
    }
 private void saveImageToStorage(Uri imageUri, String uid) {
        // Create a reference to the image in Firebase Storage
        StorageReference imageRef = storageReference.child(uid + ".jpg");

        // Upload the image to Firebase Storage
        imageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Image uploaded successfully
                    // You can handle success as needed (e.g., display a toast)
                })
                .addOnFailureListener(e -> {
                    // Handle unsuccessful upload (e.g., display an error message)
                });*/


            ImageView backButton = findViewById(R.id.backButton);

            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(UploadProfileActivity.this, ChooseActivity.class);
                    startActivity(intent);
                }
            });
        }
    }


}



