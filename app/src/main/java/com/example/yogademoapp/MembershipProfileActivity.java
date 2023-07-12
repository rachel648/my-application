package com.example.yogademoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class MembershipProfileActivity extends AppCompatActivity {

    Button btn;
    EditText ed;
    Spinner sp;

    private EditText editTextName, editTextEmail, editTextPassword;
    private Spinner spinnerMembershipType;
    private Button buttonSubmit;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_membership_profile);
    }
}