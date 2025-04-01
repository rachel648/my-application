package com.example.yogademoapp;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class RegisterActivity extends AppCompatActivity {
    EditText edUsername, edEmail, edPassword, edConfirmPassword;
    Button btn, consultantBtn;
    TextView tv;
    FloatingActionButton fabImageUpload;

    FirebaseAuth mAuth;
    StorageReference storageReference;
    Uri imageUri;
    String uploadedImageUrl = "";
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int STORAGE_PERMISSION_CODE = 100;

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // User is already signed in
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("profile_images");

        edUsername = findViewById(R.id.editTextBookingName);
        edPassword = findViewById(R.id.editTextBookingPincode);
        edEmail = findViewById(R.id.editTextBookingAddress);
        edConfirmPassword = findViewById(R.id.editTextContactNumber);
        btn = findViewById(R.id.ButtonBooking);
        consultantBtn = findViewById(R.id.ButtonConsultant);
        tv = findViewById(R.id.textViewBooking);
        fabImageUpload = findViewById(R.id.fabImageUpload);

        fabImageUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkStoragePermissionAndPickImage();
            }
        });

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser("patient");
            }
        });

        consultantBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser("consultant");
            }
        });
    }

    private void checkStoragePermissionAndPickImage() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    STORAGE_PERMISSION_CODE);
        } else {
            openImageChooser();
        }
    }

    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            imageUri = data.getData();
            uploadImageToFirebase();
        }
    }

    private void uploadImageToFirebase() {
        if (imageUri != null) {
            StorageReference fileReference = storageReference.child(System.currentTimeMillis()
                    + "." + getFileExtension(imageUri));

            fileReference.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    uploadedImageUrl = uri.toString();
                                    Toast.makeText(RegisterActivity.this,
                                            "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(RegisterActivity.this,
                                    "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void registerUser(String userType) {
        String Username = edUsername.getText().toString();
        String Password = edPassword.getText().toString();
        String email = edEmail.getText().toString();
        String ConfirmPassword = edConfirmPassword.getText().toString();

        if (Username.isEmpty() || email.isEmpty() || Password.isEmpty() || ConfirmPassword.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please fill all the details", Toast.LENGTH_SHORT).show();
        } else {
            if (Password.equals(ConfirmPassword)) {
                if (isValid(Password)) {
                    mAuth.createUserWithEmailAndPassword(email, Password)
                            .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        if (user != null) {
                                            user.updateProfile(new UserProfileChangeRequest.Builder()
                                                    .setDisplayName(Username)
                                                    .build());

                                            String userId = user.getUid();
                                            FirebaseDatabase.getInstance().getReference("Users")
                                                    .child(userId)
                                                    .setValue(userType);

                                            if ("consultant".equals(userType)) {
                                                String phoneNo = "0712671173";
                                                String experience = "5yrs";
                                                String fees = "5000";
                                                String gymNumber = "ConsultantNo: 01";
                                                int imageId = R.drawable.updatedprofile;
                                                int rating = 4;

                                                Consultant consultant = new Consultant(
                                                        Username, phoneNo, experience, fees, gymNumber, imageId, rating, uploadedImageUrl);

                                                FirebaseDatabase.getInstance().getReference("Consultants")
                                                        .child(userId)
                                                        .setValue(consultant);
                                            }

                                            Toast.makeText(getApplicationContext(),
                                                    "Registration Successful", Toast.LENGTH_SHORT).show();

                                            if ("consultant".equals(userType)) {
                                                Intent intent = new Intent(RegisterActivity.this, Agent.class);
                                                intent.putExtra("username", Username);
                                                intent.putExtra("userEmail", email);
                                                startActivity(intent);
                                            } else if ("patient".equals(userType)) {
                                                startActivity(new Intent(RegisterActivity.this, MedDoc.class));
                                            }
                                        }
                                    } else {
                                        Toast.makeText(getApplicationContext(),
                                                "Registration failed: " + task.getException().getMessage(),
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Password must contain at least 8 characters, a letter, a digit, and a special character",
                            Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getApplicationContext(),
                        "Password and Confirm password do not match",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static boolean isValid(String passwordhere) {
        int f1 = 0, f2 = 0, f3 = 0;
        if (passwordhere.length() < 8) {
            return false;
        } else {
            for (int p = 0; p < passwordhere.length(); p++) {
                if (Character.isLetter(passwordhere.charAt(p))) {
                    f1 = 1;
                }
            }
            for (int r = 0; r < passwordhere.length(); r++) {
                if (Character.isDigit(passwordhere.charAt(r))) {
                    f2 = 1;
                }
            }
            for (int s = 0; s < passwordhere.length(); s++) {
                char c = passwordhere.charAt(s);
                if (c >= 33 && c <= 46 || c == 64) {
                    f3 = 1;
                }
            }
            return f1 == 1 && f2 == 1 && f3 == 1;
        }
    }
}