package com.example.yogademoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GymDetailsActivity extends AppCompatActivity {
    private String[] []  Instructor_Deatils1 =
            {
                    {"Instuctor Name : Rachel Murithi", "Gym Number : 1DOD ", "Exp : 5yrs","Mobile No:0712671172","600"},
                    {"Instuctor Name : Shelmith Nelima", "Gym Number : 1DOD ", "Exp : 5yrs","Mobile No:0723297740","600"},
                    {"Instuctor Name : Benson Gelas", "Gym Number : 1DOD ", "Exp : 5yrs","Mobile No:0750467930","600"},
                    {"Instuctor Name : Henry  Chumba", "Gym Number : 1DOD ", "Exp : 5yrs","Mobile No:0714752682","600"},
                    {"Instuctor Name : Marcus Justin", "Gym Number : 1DOD ", "Exp : 5yrs","Mobile No:0715588485","600"},
            };
    private String[] []  Instructor_Deatils2 =
    {
        {"Instuctor Name : Rachel Murithi", "Gym Number : 1DOD ", "Exp : 5yrs","Mobile No:0712671172","600"},
        {"Instuctor Name : Shelmith Nelima", "Gym Number : 1DOD ", "Exp : 5yrs","Mobile No:0723297740","600"},
        {"Instuctor Name : Benson Gelas", "Gym Number : 1DOD ", "Exp : 5yrs","Mobile No:0750467930","600"},
        {"Instuctor Name : Henry  Chumba", "Gym Number : 1DOD ", "Exp : 5yrs","Mobile No:0714752682","600"},
        {"Instuctor Name : Marcus Justin", "Gym Number : 1DOD ", "Exp : 5yrs","Mobile No:0715588485","600"},
    };
    private String[] []  Instructor_Deatils3 =
    {
        {"Instuctor Name : Rachel Murithi", "Gym Number : 1DOD ", "Exp : 5yrs","Mobile No:0712671172","600"},
        {"Instuctor Name : Shelmith Nelima", "Gym Number : 1DOD ", "Exp : 5yrs","Mobile No:0723297740","600"},
        {"Instuctor Name : Benson Gelas", "Gym Number : 1DOD ", "Exp : 5yrs","Mobile No:0750467930","600"},
        {"Instuctor Name : Henry  Chumba", "Gym Number : 1DOD ", "Exp : 5yrs","Mobile No:0714752682","600"},
        {"Instuctor Name : Marcus Justin", "Gym Number : 1DOD ", "Exp : 5yrs","Mobile No:0715588485","600"},
    };

    private String[] []  Instructor_Deatils4 =
            {
                    {"Instuctor Name : Rachel Murithi", "Gym Number : 1DOD ", "Exp : 5yrs","Mobile No:0712671172","600"},
                    {"Instuctor Name : Shelmith Nelima", "Gym Number : 1DOD ", "Exp : 5yrs","Mobile No:0723297740","600"},
                    {"Instuctor Name : Benson Gelas", "Gym Number : 1DOD ", "Exp : 5yrs","Mobile No:0750467930","600"},
                    {"Instuctor Name : Henry  Chumba", "Gym Number : 1DOD ", "Exp : 5yrs","Mobile No:0714752682","600"},
                    {"Instuctor Name : Marcus Justin", "Gym Number : 1DOD ", "Exp : 5yrs","Mobile No:0715588485","600"},
            };
    private String[] []  Instructor_Deatils5 =
            {
                    {"Instuctor Name : Rachel Murithi", "Gym Number : 1DOD ", "Exp : 5yrs","Mobile No:0712671172","600"},
                    {"Instuctor Name : Shelmith Nelima", "Gym Number : 1DOD ", "Exp : 5yrs","Mobile No:0723297740","600"},
                    {"Instuctor Name : Benson Gelas", "Gym Number : 1DOD ", "Exp : 5yrs","Mobile No:0750467930","600"},
                    {"Instuctor Name : Henry  Chumba", "Gym Number : 1DOD ", "Exp : 5yrs","Mobile No:0714752682","600"},
                    {"Instuctor Name : Marcus Justin", "Gym Number : 1DOD ", "Exp : 5yrs","Mobile No:0715588485","600"},
            };
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

