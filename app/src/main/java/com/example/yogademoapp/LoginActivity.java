package com.example.yogademoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
                Database db = new Database(getApplicationContext(),"YogaDemo",null ,1);

                if (Username.length()==0 || Password.length()==0) {
                    Toast.makeText(getApplicationContext(),("Please fill all the details"),Toast.LENGTH_SHORT).show();
                }else {
          if (db.login(Username,Password)==1);
          Toast.makeText(getApplicationContext(), ("Login Success"), Toast.LENGTH_SHORT).show();
                    SharedPreferences sharedpreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("username", Username);
                    // to save our data with key and value.
                    editor.apply();
                 startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                    /*  }else{
                    Toast.makeText(getApplicationContext(), ("invalid username or password"), Toast.LENGTH_SHORT).show();
                    }   */
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