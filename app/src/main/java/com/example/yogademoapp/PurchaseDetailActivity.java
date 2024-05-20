package com.example.yogademoapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class PurchaseDetailActivity extends AppCompatActivity {

    TextView textviewBuyTitleDetail1,textViewTotalCost;
    EditText edPurchaseDetailMultiLine;
    Button buttonPurchaseDetailback, buttonPurchaseDetailCart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_detail);

        textviewBuyTitleDetail1 = findViewById(R.id.textviewBuyTitleDetail1);
        textViewTotalCost = findViewById(R.id.textViewTotalCost);
        edPurchaseDetailMultiLine = findViewById(R.id.edPurchaseDetailMultiLine);
        buttonPurchaseDetailback = findViewById(R.id.buttonConsultantDetailback);
        buttonPurchaseDetailCart = findViewById(R.id.buttonPurchaseDetailCart);


        edPurchaseDetailMultiLine.setKeyListener(null);

        Intent intent = getIntent();
        textviewBuyTitleDetail1.setText(intent.getStringExtra("text1"));
        edPurchaseDetailMultiLine.setText(intent.getStringExtra("text2"));
        textViewTotalCost.setText("Total Cost :"+intent.getStringExtra("text3")+"ksh");

        buttonPurchaseDetailback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PurchaseDetailActivity.this, PurchaseActivity.class));
            }
        });


      /*  buttonPurchaseDetailCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent paymentIntent = new Intent(PurchaseDetailActivity.this, Payment.class);

                // Assuming "text3" contains the total cost as a String
                String totalCost = getIntent().getStringExtra("text3");

                // Pass the total cost to Payment activity
                paymentIntent.putExtra("TrainFees", totalCost);

                startActivity(paymentIntent);
            }
        });*/


        buttonPurchaseDetailCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              SharedPreferences sharedPreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
                String username = sharedPreferences.getString("username", "");
                String product =textviewBuyTitleDetail1.getText().toString();
                float price = Float.parseFloat(intent.getStringExtra("text3"));

                Database db = new Database(getApplicationContext(),"fitness",null,1);
         if (db.checkCart(username,product)==1){
             Toast.makeText(getApplicationContext(),"product Already Added", Toast.LENGTH_SHORT).show();
         }else {
db.addCart(username,product,price,"cart");
Toast.makeText(getApplicationContext(),"Record Inserted to Purchase",Toast.LENGTH_SHORT).show();
//to cart
       startActivity(new Intent(PurchaseDetailActivity.this,CartActivity.class));
         }
            }
        });

        ImageView backButton = findViewById(R.id.backButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PurchaseDetailActivity.this, ChooseActivity.class);
                startActivity(intent);
            }
        });

    }

}