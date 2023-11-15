package com.example.yogademoapp;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CartBook extends AppCompatActivity {
EditText edname, edaddress, edcontact, edpincode;
Button btnBooking;
  @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_book);

        edname = findViewById(R.id.editTextBookingName);
      edaddress = findViewById(R.id.editTextBookingAddress);
      edcontact = findViewById(R.id.editTextContactNumber);
      edpincode = findViewById(R.id.editTextBookingPincode);
      btnBooking = findViewById(R.id.ButtonBooking);

      Intent intent=getIntent();
      String priceData = intent.getStringExtra("price");
    String date = intent.getStringExtra("date");
    String time = intent.getStringExtra("time");

    btnBooking.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            SharedPreferences sharedPreferences = getSharedPreferences("Shared_prefs",Context.MODE_PRIVATE);
            String username = sharedPreferences.getString("username","");

            Database db = new Database(getApplicationContext(), "fitness", null, 1);

            try {
                int pincode = Integer.parseInt(edpincode.getText().toString());

        // db.addOrder(username, edname.getText().toString(), edaddress.getText().toString(), edcontact.getText().toString(), pincode, date, time, Float.parseFloat(price[1]), "cart");
                db.removeCart(username, "cart");
                Toast.makeText(getApplicationContext(), "Your booking is done successfully", Toast.LENGTH_LONG).show();
                startActivity(new Intent(CartBook.this, HomeActivity.class));
            } catch (NumberFormatException e) {
                Toast.makeText(getApplicationContext(), "Invalid PIN code", Toast.LENGTH_SHORT).show();
            }
        }
    });

   ImageView backButton = findViewById(R.id.backButton);
      backButton.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent intent = new Intent(CartBook.this, ChooseActivity.class);
              startActivity(intent);
          }
      });
  }
}