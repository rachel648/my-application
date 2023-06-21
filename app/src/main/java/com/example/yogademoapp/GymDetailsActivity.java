package com.example.yogademoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class GymDetailsActivity extends AppCompatActivity {
    private String[] []  Instructor_Details1 =
            {
                    {"Instuctor Name : Rachel Murithi", "Gym Number : 1DOD ", "Exp : 5yrs","Mobile No:0712671172","600"},
                    {"Instuctor Name : Shelmith Nelima", "Gym Number : 1DOD ", "Exp : 5yrs","Mobile No:0723297740","600"},
                    {"Instuctor Name : Benson Gelas", "Gym Number : 1DOD ", "Exp : 5yrs","Mobile No:0750467930","600"},
                    {"Instuctor Name : Henry  Chumba", "Gym Number : 1DOD ", "Exp : 5yrs","Mobile No:0714752682","600"},
                    {"Instuctor Name : Marcus Justin", "Gym Number : 1DOD ", "Exp : 5yrs","Mobile No:0715588485","600"},
            };
    private String[] []  Instructor_Details2 =
    {
        {"Instuctor Name : Rachel Murithi", "Gym Number : 1DOD ", "Exp : 5yrs","Mobile No:0712671172","600"},
        {"Instuctor Name : Shelmith Nelima", "Gym Number : 1DOD ", "Exp : 5yrs","Mobile No:0723297740","600"},
        {"Instuctor Name : Benson Gelas", "Gym Number : 1DOD ", "Exp : 5yrs","Mobile No:0750467930","600"},
        {"Instuctor Name : Henry  Chumba", "Gym Number : 1DOD ", "Exp : 5yrs","Mobile No:0714752682","600"},
        {"Instuctor Name : Marcus Justin", "Gym Number : 1DOD ", "Exp : 5yrs","Mobile No:0715588485","600"},
    };
    private String[] []  Instructor_Details3 =
    {
        {"Instuctor Name : Rachel Murithi", "Gym Number : 1DOD ", "Exp : 5yrs","Mobile No:0712671172","600"},
        {"Instuctor Name : Shelmith Nelima", "Gym Number : 1DOD ", "Exp : 5yrs","Mobile No:0723297740","600"},
        {"Instuctor Name : Benson Gelas", "Gym Number : 1DOD ", "Exp : 5yrs","Mobile No:0750467930","600"},
        {"Instuctor Name : Henry  Chumba", "Gym Number : 1DOD ", "Exp : 5yrs","Mobile No:0714752682","600"},
        {"Instuctor Name : Marcus Justin", "Gym Number : 1DOD ", "Exp : 5yrs","Mobile No:0715588485","600"},
    };

    private String[] []  Instructor_Details4 =
            {
                    {"Instuctor Name : Rachel Murithi", "Gym Number : 1DOD ", "Exp : 5yrs","Mobile No:0712671172","600"},
                    {"Instuctor Name : Shelmith Nelima", "Gym Number : 1DOD ", "Exp : 5yrs","Mobile No:0723297740","600"},
                    {"Instuctor Name : Benson Gelas", "Gym Number : 1DOD ", "Exp : 5yrs","Mobile No:0750467930","600"},
                    {"Instuctor Name : Henry  Chumba", "Gym Number : 1DOD ", "Exp : 5yrs","Mobile No:0714752682","600"},
                    {"Instuctor Name : Marcus Justin", "Gym Number : 1DOD ", "Exp : 5yrs","Mobile No:0715588485","600"},
            };
    private String[] []  Instructor_Details5 =
            {
                    {"Instuctor Name : Rachel Murithi", "Gym Number : 1DOD ", "Exp : 5yrs","Mobile No:0712671172","600"},
                    {"Instuctor Name : Shelmith Nelima", "Gym Number : 1DOD ", "Exp : 5yrs","Mobile No:0723297740","600"},
                    {"Instuctor Name : Benson Gelas", "Gym Number : 1DOD ", "Exp : 5yrs","Mobile No:0750467930","600"},
                    {"Instuctor Name : Henry  Chumba", "Gym Number : 1DOD ", "Exp : 5yrs","Mobile No:0714752682","600"},
                    {"Instuctor Name : Marcus Justin", "Gym Number : 1DOD ", "Exp : 5yrs","Mobile No:0715588485","600"},
            };
    Button btn;
    TextView tv;
    String[] [] Gym_Details = {};
    HashMap<String,String> item;
    ArrayList List;
    SimpleAdapter sa;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gym_details);

        tv = findViewById(R.id.TextviewGymDetailsTitle);
        btn = findViewById(R.id.buttonGymDetailsback);
        Intent it = getIntent();//variable "it" obtained by calling getIntent().The intent is stored in "it"
        String title = it. getStringExtra("title");//The retrieved value which is expected to be a string is assigned to the "title"
        tv.setText(title);//Value of the title is set as the text of a textview

        if(title.compareTo("Get_An_Instructor")==0)
            Gym_Details = Instructor_Details1;
        else
        if(title.compareTo("Explote_Outside")==0)
            Gym_Details = Instructor_Details2;
        else
        if(title.compareTo("Boxing_Training_Available")==0)
            Gym_Details = Instructor_Details3;
        else
        if(title.compareTo("Weight_Lift_With_me")==0)
            Gym_Details = Instructor_Details4;
        else
        if(title.compareTo("Aerobics")==0)
            Gym_Details = Instructor_Details5;
        else

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
startActivity(new Intent(GymDetailsActivity.this,BoxActivity.class));
            }
        });

       List = new ArrayList<>();
       for (int i=0; i<Gym_Details.length;i++){
           item = new HashMap<String,String>();
           item.put("line1",Gym_Details [i] [0]);
           item.put("line2",Gym_Details [i] [1]);
           item.put("line3",Gym_Details [i] [2]);
           item.put("line4",Gym_Details [i] [3]);
           item.put("line5","Instruct Fees:" +Gym_Details[i][4]+"ksh");
            List.add(item);
    }
       sa = new SimpleAdapter(this,List,
               R.layout.multi_lines,
               new String[]{"line1", "line2", "line3", "line4", "line5"},
               new int [] {R.id.line_a,R.id.line_b,R.id.line_c,R.id.line_d,R.id.line_e}
       );
        ListView lst = findViewById(R.id.listviewGymDetails);
        lst.setAdapter(sa);

}
}

