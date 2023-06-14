package com.example.yogademoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    EditText edUsername, edPassword;
    Button btn;
    TextView tv;

    /*  The following are methods:
        onCreate(Bundle savedInstanceState)
        onClick(View view) (anonymous inner class inside onCreate)
        onClick(View view) (anonymous inner class inside tv.setOnClickListener)*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //objects creation
        edUsername = findViewById(R.id.RegistarPassword);/* edUsername,edPassword,btn,tv is a Member variable i.e the data defined by the class*/
        edPassword = findViewById(R.id.RegistarPassword);
        btn = findViewById(R.id.buttonLogin); /*Btn for button*/
        tv= findViewById(R.id.textViewExistingUser);/*tv for text view*/
        //classes creation
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String Username = edUsername.getText().toString();
                String Password = edPassword.getText().toString();
                if (Username.length()==0 || Password.length()==0) {
                    Toast.makeText(getApplicationContext(),("Please fill all the details"),Toast.LENGTH_SHORT).show();
                }else {


                    Toast.makeText(getApplicationContext(), ("Login Success"), Toast.LENGTH_SHORT).show();
                }
            }
        });
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });
        }

}