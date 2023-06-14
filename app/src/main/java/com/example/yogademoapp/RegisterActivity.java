package com.example.yogademoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {
    EditText edUsername, edEmail, edPassword, edConfirmPassword;
    Button btn;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edUsername = findViewById(R.id.UsernameRegistar);
        edPassword = findViewById(R.id.RegistarPassword);
        edEmail = findViewById(R.id.emailReg);
        edConfirmPassword = findViewById(R.id.ConfirmPassword);
        btn = findViewById(R.id.Registarbutton);
        tv= findViewById(R.id.textViewExistingUser);

      /*  tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
            }
        });W*/
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Username = edUsername.getText().toString();
                String Password = edPassword.getText().toString();
                String email = edEmail.getText().toString();
                String ConfirmPassword = edConfirmPassword.getText().toString();
                Database db = new Database(getApplicationContext(),"YogaDemo",null ,1);

                if (Username.length() == 0 || email.length() == 0 || Password.length() == 0 || ConfirmPassword.length() == 0) {
                    Toast.makeText(getApplicationContext(), ("Please fill all the details"), Toast.LENGTH_SHORT).show();
                }
                else {
                    if (Password.compareTo(ConfirmPassword) == 0) {
                        if (isValid(Password)) {    /*if both passwords are same then check whether it's a valid password containing all specifications*/
                 db.registar(Username,email,Password);
                      Toast.makeText(getApplicationContext(),"Record Inserted",Toast.LENGTH_SHORT).show();
                      startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        }
                        else {
                    Toast.makeText(getApplicationContext(),("Password must contain at least 8 characters,having letter,digit and alphabet"), Toast.LENGTH_SHORT).show();
                }
                    } else {
                        Toast.makeText(getApplicationContext(), ("Password and Confirm password do not match"), Toast.LENGTH_SHORT).show();

                    }

                }

            }

        });

    }
    /*  We check if password is valid by checking the length eg 8 characters,whether it has a digit, a letter,an alphabet, a special character
    * To do so we use a built in function as used below
    * if less eight return false
    * the one character must be between oscillators as below on the third "for"
    * if all flags are set we return true*/

    public static boolean isValid(String passwordhere) {
        int f1=0,f2=0,f3=0;
        if (passwordhere.length() < 8) {
            return false;
        } else {
            for(int p=0; p< passwordhere.length(); p++) {
                if (Character.isLetter(passwordhere.charAt(p))) {
                    f1 = 1;
                }
            }
            for (int r=0; r < passwordhere.length(); r++) {
                if (Character.isDigit(passwordhere.charAt(r))) {
                    f2 = 1;
                }
            }
            for (int s=0; s < passwordhere.length(); s++) {
                char c = passwordhere.charAt(s);
                if(c>=33&&c<=46||c==64){          /*oscillators*/
                      f3 = 1 ;
                }
            }
            if(f1==1 && f2==1 && f3==1)         /*flags*/
                return true;

                return false;
                }
            }
}

