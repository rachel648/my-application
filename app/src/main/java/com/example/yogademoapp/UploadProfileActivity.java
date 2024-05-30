// UploadProfileActivity.java
package com.example.yogademoapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class UploadProfileActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView profileImageView;
    Button chooseImageButton,UploadImageButton;

    private Uri selectedImageUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_profile);

        profileImageView = findViewById(R.id.profileImageView);
         chooseImageButton = findViewById(R.id.chooseImageButton);
        UploadImageButton = findViewById(R.id.UploadImageButton);

        chooseImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImageChooser();
            }
        });

        // Check if there's a previously selected image URI
        if (selectedImageUri != null) {
            profileImageView.setImageURI(selectedImageUri);
        }
    }

    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private boolean imageIsSelected() {
        // Check if selectedImageUri is not null
        return selectedImageUri != null;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImageUri = data.getData();
            // You can display the selected image in your ImageView or upload it to Firebase Storage
            profileImageView.setImageURI(selectedImageUri);


            ImageView backButton = findViewById(R.id.backButton);

            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(UploadProfileActivity.this, ChooseActivity.class);
                    startActivity(intent);
                }
            });


            UploadImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Check if an image is selected
                    if (imageIsSelected()) {
                        // Display a toast message indicating that the image is uploaded
                        Toast.makeText(UploadProfileActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        // Perform upload operation here
                    } else {
                        // Display a toast message indicating that an image should be selected first
                        Toast.makeText(UploadProfileActivity.this, "Select an image first", Toast.LENGTH_SHORT).show();
                    }
                }
            });


        }
    }


}



