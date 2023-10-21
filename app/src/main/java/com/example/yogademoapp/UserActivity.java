package com.example.yogademoapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.yogademoapp.databinding.ActivityUserBinding;

public class UserActivity extends AppCompatActivity {

   Button buttonBooking;

    ActivityUserBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

     //   buttonBooking = findViewById(R.id.buttonBooking);

        Intent intent = this.getIntent();

        if (intent != null) {

            String name = intent.getStringExtra("name");
            String phone = intent.getStringExtra("phone");
            String Experience = intent.getStringExtra("Experience");
            String fees = intent.getStringExtra("fees");
            String GymNumber = intent.getStringExtra("GymNumber");
            int imageid = intent.getIntExtra("imageid",R.drawable.babe3);

            binding.nameProfile.setText(name);
            binding.phoneProfile.setText(phone);
            binding.Experience.setText(Experience);
            binding.fees.setText(fees);
            binding.GymNumber.setText(GymNumber);
            binding.ProfileImage.setImageResource(imageid);

    }

        buttonBooking = findViewById(R.id.buttonBooking);

        buttonBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start the BookAppointmentActivity when the button is clicked
                Intent bookAppointmentIntent = new Intent(UserActivity.this, Payment.class);

// Pass the "fees" value to the Payment activity
                String fees = binding.fees.getText().toString();
                bookAppointmentIntent.putExtra("TrainFees", fees);
                startActivity(bookAppointmentIntent);
            }
        });
}
}