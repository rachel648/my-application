package com.example.yogademoapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.yogademoapp.databinding.ActivityConsultantBinding;

import java.util.ArrayList;

public class ConsultantActivity extends AppCompatActivity {
    ActivityConsultantBinding binding;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConsultantBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        btn= findViewById(R.id.buttonConsultantDetailback);


        int[] imageId = {R.drawable.man1,R.drawable.man2,R.drawable.man3,R.drawable.lady2,R.drawable.lady3,R.drawable.lady4,R.drawable.babe3,R.drawable.man4,R.drawable.lady1};

        String [] name = {"Chris\nBones","Craig\nOmolo","Mike\nKimathi","Ray\nMellissa","Shelmith Nelina","Zaga llo","Caroline Odinga","Dennis chipchip","Agnes\nBenson"};

        String [] lastMessage = {"Hi","Let's talk","How can I help you?","Hey","ssup","Confidential","Cool","Need help?","Friendly"};

       String [] lastMsgTime = {"5:00 pm","3:00 pm","7:00 am","2:00 pm","12:00 noon","8:30 pm","10:00 pm","11:00 am","8:00 am","9:00 pm","4:00 pm","5:30 pm"};

        String [] phoneNo = {"0712671173","0112671077","0782641193","0799671773","0782677173","0767671183","0782671479","0752671178","0110677170"};

        String [] Experience = {"10yrs","7yrs","7yrs","6yrs","5yrs","3yrs","2yrs","1yrs","3yrs"};

        String [] fees = {"7000","6000","6000","5500","5000","4000","4700","3500","2000"};

        String [] GymNumber = {"ConsultantNo: 07","ConsultantNo: 03","ConsultantNo: 10","ConsultantNo: 06","ConsultantNo: 05","ConsultantNo: 16","ConsultantNo :3","ConsultantNo 14:","ConsultantNo: 14"};

        ArrayList<User> userArrayList = new ArrayList<>();

        for (int i = 0;i< imageId.length;i++){

            User user = new User(name[i],lastMessage[i],lastMsgTime[i],phoneNo[i],GymNumber[i],Experience[i],fees[i],imageId[i]);

     userArrayList.add(user);

     ListAdapter listAdapter = new ListAdapter(ConsultantActivity.this,userArrayList);

            binding.myListView.setAdapter(listAdapter);
            binding.myListView.setClickable(true);
            binding.myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                    Intent i = new Intent(ConsultantActivity.this,UserActivity.class);
          i.putExtra("name",name[position]);
          i.putExtra("phone",phoneNo [position]);
          i.putExtra("Experience", Experience[position]);
          i.putExtra("imageid",imageId[position]);
          i.putExtra("fees",fees[position]);
          i.putExtra("GymNumber",GymNumber[position]);
          startActivity(i);

                }
            });

btn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {

        startActivity(new Intent(ConsultantActivity.this,mentaldashboard.class));
    }
});

            ImageView backButton = findViewById(R.id.backButton);
            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ConsultantActivity.this, ChooseActivity.class);
                    startActivity(intent);
                }
            });


        }

    }
}

