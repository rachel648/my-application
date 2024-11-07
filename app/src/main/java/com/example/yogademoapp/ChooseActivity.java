package com.example.yogademoapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;



public class ChooseActivity extends AppCompatActivity {

    Button  logoutButton, consultationButton, mentalStatusButton, physicalFitnessButton;

    ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose2);


        physicalFitnessButton = findViewById(R.id.physicalFitnessButton);
        mentalStatusButton= findViewById(R.id.mentalStatusButton);

      //  consultationButton = findViewById(R.id.consultationButton);
       // logoutButton = findViewById(R.id.logoutButton);



        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);



        physicalFitnessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChooseActivity.this, HomeActivity.class));

            }
        });
      /*  logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChooseActivity.this,LoginActivity.class));

            }
        }); */

        mentalStatusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChooseActivity.this,MedDoc.class));

            }
        });



        // Set a listener for item selection in BottomNavigationView
          bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
          public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_home:
                        startActivity(new Intent(ChooseActivity.this, ChooseActivity.class));
                        return true;
                    case R.id.menu_profile:
                        startActivity(new Intent(ChooseActivity.this, MembershipProfileActivity.class));
                        return true;
                    case R.id.Physical:
                        startActivity(new Intent(ChooseActivity.this, HomeActivity.class));
                        return true;
                    case R.id.Mental:
                        startActivity(new Intent(ChooseActivity.this, MedDoc.class));
                        return true;
                    case R.id.menu_cart: // Back (logout)
                        handleLogout();
                        return true;
                    default:
                        return false;
                }
            }
        });

    }

    private void handleLogout() {
        // Optionally show a toast message
        Toast.makeText(this, "Logging out...", Toast.LENGTH_SHORT).show();

        // Clear user session or any relevant data if needed

        // Navigate back to the LoginActivity
        Intent logoutIntent = new Intent(ChooseActivity.this, LoginActivity.class);
        logoutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clears the back stack
        startActivity(logoutIntent);
        finish(); // Finish the current activity
    }


}
