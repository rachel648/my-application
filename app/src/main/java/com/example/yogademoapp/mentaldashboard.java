package com.example.yogademoapp;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class mentaldashboard extends AppCompatActivity {

    private CardView cardView;
    private Spinner spinner;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mentaldashboard);

        cardView = findViewById(R.id.Box); // Corrected the ID here
        spinner = findViewById(R.id.spinner);
        textView = findViewById(R.id.textView2);


        // Define the list of options for the Spinner
        final String[] options = {"Option 1", "Option 2", "Option 3", "Option 4"};

        // Create an ArrayAdapter to populate the Spinner with options
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // Set an item selected listener for the Spinner
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedOption = options[position];
                // Handle the selected option here
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing
            }
        });

        // Set an onClickListener for the CardView to show/hide the Spinner
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spinner.getVisibility() == View.VISIBLE) {
                    spinner.setVisibility(View.GONE);
                } else {
                    spinner.setVisibility(View.VISIBLE);
                }
            }
        });

        // Set an onClickListener for the TextView to show/hide the Spinner
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleSpinnerVisibility();
            }
        });

    }
    private void toggleSpinnerVisibility() {
        if (spinner.getVisibility() == View.VISIBLE) {
            spinner.setVisibility(View.GONE);
        } else {
            spinner.setVisibility(View.VISIBLE);
        }



    }
}
