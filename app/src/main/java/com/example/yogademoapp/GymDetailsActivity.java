package com.example.yogademoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GymDetailsActivity extends AppCompatActivity {
    Button btn;
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gym_details);

        tv = findViewById(R.id.TextviewGymDetailsTitle);
        btn = findViewById(R.id.buttonGymDetailsback);
        Intent it = getIntent();//variable "it" obtained by calling getIntent().The intent is stored in "it"
        String title = it.getStringExtra("title");//The retrieved value which is expected to be a string is assigned to the "title"
        tv.setText(title);//Value of the title is set as the text of a textview
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
startActivity(new Intent(GymDetailsActivity.this,BoxActivity.class));
            }
        });
    }
}