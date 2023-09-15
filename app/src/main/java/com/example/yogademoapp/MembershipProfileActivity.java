package com.example.yogademoapp;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class MembershipProfileActivity extends AppCompatActivity {

    Button btn;
    EditText ed;
    Spinner sp;
    FirebaseAuth mAuth;

    private EditText editTextName, editTextEmail, editTextPassword, editTextUsername;
    private Spinner spinnerMembershipType;
    private Button buttonSubmit, buttonSignup,buttonForgotPassword, btnback;

  //  private ImageView imageView;

    private FirebaseAuth authProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_membership_profile);
        mAuth = FirebaseAuth.getInstance();
        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        spinnerMembershipType = findViewById(R.id.spinnerMembershipType);
        buttonSubmit = findViewById(R.id.buttonSubmit);
        buttonForgotPassword = findViewById(R.id.buttonForgotPassword);
        // editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextPassword.setHintTextColor(ColorStateList.valueOf(getResources().getColor(android.R.color.white)));
        btnback = findViewById(R.id.buttonMembershipback);
        buttonSignup = findViewById(R.id.buttonSignup);

        //Set OnClickListener on ImageView to Open Uploadloadprofile on MembershipProfileActivity
        //  imageView = findViewById(R.id.imageview_profile_dp);
      //  imageView.setOnClickListener(new View.OnClickListener() {
          //  @Override
          //  public void onClick(View view) {
            //    Intent intent = new Intent(MembershipProfileActivity.this, UploadProfileActivity.class);
           //     startActivity(intent);
        //    }
   //     });
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextName.getText().toString();
                String email = editTextEmail.getText().toString();
                String membershipType = spinnerMembershipType.getSelectedItem().toString();
                String username = editTextName.getText().toString();
                String password = editTextPassword.getText().toString();
                // Do something with the entered data, such as storing it in a database
                // Display a toast message to indicate successful submission

                if (email.length() == 0 || password.length() == 0) {
                    Toast.makeText(getApplicationContext(), ("Please fill all the details"), Toast.LENGTH_SHORT).show();
                } else {
                    // Use Firebase Authentication to sign in
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(MembershipProfileActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(
                                        MembershipProfileActivity.this,
                                        "Profile submitted: Name = " + name + ", Email = " + email + ", Membership Type = " + membershipType,
                                        Toast.LENGTH_SHORT).show();
                                // Perform login authentication logic
                                if (username.equals("admin") && password.equals("password")) {
                                    Toast.makeText(MembershipProfileActivity.this, "Login successful", Toast.LENGTH_SHORT).show();

                                    startActivity(new Intent(MembershipProfileActivity.this, HomeActivity.class));
                                    // Perform any additional actions after successful login

                                } else {
                                    Toast.makeText(MembershipProfileActivity.this, "Login Successz", Toast.LENGTH_SHORT).show();

                                    startActivity(new Intent(MembershipProfileActivity.this, HomeActivity.class));
                                }
                                // Start the Login activity
                                // Intent intent = new Intent(MembershipProfileActivity.this, HomeActivity.class);
                                //  startActivity(intent);

                            }
                        }

                    });

                }
            }
        });

        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the Register New User activity
                Intent intent = new Intent(MembershipProfileActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        buttonForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implement the logic for forgot password functionality
                Toast.makeText(MembershipProfileActivity.this, "Forgot Password Clicked", Toast.LENGTH_SHORT).show();
                // Add your forgot password logic here
            }
        });


        // Add four types of membership to the spinner
        String[] membershipTypes = {"OneDayMembership", "WeekMembership", "MonthMembership", "YearMembership"};
        // ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, membershipTypes);
        //  spinnerMembershipType.setAdapter(adapter);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, membershipTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMembershipType.setAdapter(adapter);

    }

}



