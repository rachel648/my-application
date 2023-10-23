package com.example.yogademoapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class mentaldashboard extends AppCompatActivity {

    private Spinner spinner;
    private Spinner secondSpinner;

    private TextView textViewOnTop;

    private boolean isTextViewVisible = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mentaldashboard);

        CardView cardView = findViewById(R.id.Box); // Corrected the ID here

        spinner = findViewById(R.id.spinner);
        TextView textView = findViewById(R.id.textView2);
        secondSpinner = findViewById(R.id.secondSpinner);
        textViewOnTop = findViewById(R.id.textViewOnTop);

// Set the first Spinner initially to GONE
        spinner.setVisibility(View.GONE);

        // Define the list of options for the Spinner
        final String[] options = {"Depression", "Anxiety disoider", "Eating disorder", "low-self-esteem", "other", "D"};

        // Create an ArrayAdapter to populate the Spinner with options
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // Set an item selected listener for the Spinner
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Handle the selected option here

                updateSecondSpinner(position);
                secondSpinner.setVisibility(View.VISIBLE); // Show the second Spinner
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
                // Toggle the visibility of the TextView
                if (isTextViewVisible) {
                    textViewOnTop.setVisibility(View.GONE);
                } else {
                    textViewOnTop.setVisibility(View.VISIBLE);
                }
                isTextViewVisible = !isTextViewVisible;

                // Toggle the visibility of the first Spinner
                if (spinner.getVisibility() == View.VISIBLE) {
                    spinner.setVisibility(View.GONE);
                } else {
                    spinner.setVisibility(View.VISIBLE);
                }

                // Ensure the second Spinner is hidden when the CardView is clicked
                secondSpinner.setVisibility(View.GONE);
            }
        });



        // Set an onClickListener for the TextView to show/hide the Spinner
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                secondSpinner.setVisibility(View.GONE);
              //  toggleSpinnerVisibility();
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

    private void updateSecondSpinner(int position) {
        String[] secondOptions;

        // Determine the options for the second Spinner based on the selected item in the first Spinner
        switch (position) {
            case 0:
                secondOptions = new String[]{"Health resources", "Online sessions", "Physical sessions"};
                break;
            case 1:
                secondOptions = new String[]{"Health resources", "Online sessions", "Physical sessions"};
                break;
            case 2:
                secondOptions = new String[]{"Health resources", "Online sessions", "Physical sessions"};
                break;
            case 3:
                secondOptions = new String[]{"Health resources", "Online sessions", "Physical sessions"};
                break;
            case 4:
                secondOptions = new String[]{"Health resources", "Online sessions", "Physical sessions"};
                break;
                case 5:
                secondOptions = new String[]{"Health resources", "Online sessions", "Physical sessions"};
                break;
            default:
                secondOptions = new String[0];
                break;
        }

        // Create an ArrayAdapter to populate the second Spinner with options
        ArrayAdapter<String> secondAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, secondOptions);
        secondAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        secondSpinner.setAdapter(secondAdapter);

        // Set an item selected listener for the second Spinner
        secondSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Handle the selected option here

                // Check the selected option and start the corresponding activity
                String selectedOption = secondOptions[position];
                if ("Online sessions".equals(selectedOption) || "Physical sessions".equals(selectedOption)) {
                    // Start the ConsultantsActivity
                    Intent intent = new Intent(mentaldashboard.this, ConsultantActivity.class);
                    startActivity(intent);
                } else {
                    // Handle other options if needed
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing
            }
        });

        CardView Community = findViewById(R.id.Community); //creation of another object
        Community.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mentaldashboard.this, CommunityActivity.class));
            }
        });

    }
}

