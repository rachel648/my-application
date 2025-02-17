package com.example.yogademoapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class mentaldashboard extends AppCompatActivity {

    private Spinner spinner;
    private Spinner secondSpinner;
    private TextView textViewOnTop;
    private boolean isTextViewVisible = true;

    CardView Community;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mentaldashboard);

        CardView cardView = findViewById(R.id.Box);
        spinner = findViewById(R.id.spinner);
        TextView textView = findViewById(R.id.textView2);
        secondSpinner = findViewById(R.id.secondSpinner);
        textViewOnTop = findViewById(R.id.textViewOnTop);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        Community = findViewById(R.id.Community);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);

        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

       navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.menu_home:
                        Toast.makeText(mentaldashboard.this, "Home selected", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.profileImageView:
                        Toast.makeText(mentaldashboard.this, "Profile selected", Toast.LENGTH_SHORT).show();
                        // Start the GreenCard activity
                        Intent intent = new Intent(mentaldashboard.this, GreenCard.class);
                        startActivity(intent);
                        break;
                    case R.id.info:
                        Toast.makeText(mentaldashboard.this, "About selected", Toast.LENGTH_SHORT).show();
                        //start about actvity
                        Intent intent1 = new Intent(mentaldashboard.this, about.class);
                        startActivity(intent1);
                        break;
                    case R.id.share:
                        Toast.makeText(mentaldashboard.this, "Share selected", Toast.LENGTH_SHORT).show();

                        // Create an Intent to share text
                        Intent sendIntent = new Intent(Intent.ACTION_SEND);
                        sendIntent.setType("text/plain");
                        sendIntent.putExtra(Intent.EXTRA_TEXT, "Check out this awesome app!");

                        // Create the options for the dialog
                        String[] options = {"WhatsApp", "Instagram"};

                        // Show a dialog with WhatsApp and Instagram options
                        new AlertDialog.Builder(mentaldashboard.this)
                                .setTitle("Share via")
                                .setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Check which option was selected
                                        if (which == 0) { // WhatsApp
                                            sendIntent.setPackage("com.whatsapp");
                                            try {
                                                startActivity(sendIntent); // Try to start WhatsApp
                                            } catch (android.content.ActivityNotFoundException ex) {
                                                Toast.makeText(mentaldashboard.this, "WhatsApp not installed", Toast.LENGTH_SHORT).show();
                                            }
                                        } else if (which == 1) { // Instagram
                                            sendIntent.setPackage("com.instagram.android"); // Set Instagram package name
                                            try {
                                                startActivity(sendIntent); // Try to start Instagram
                                            } catch (android.content.ActivityNotFoundException ex) {
                                                Toast.makeText(mentaldashboard.this, "Instagram not installed", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                                })
                                .show();
                        break;

                    case R.id.call:
                        Toast.makeText(mentaldashboard.this, "Contacts selected", Toast.LENGTH_SHORT).show();
                        Intent intent3 = new Intent(mentaldashboard.this, contacts.class);
                        startActivity(intent3);
                        break;
                    case R.id.rate_us:
                        Toast.makeText(mentaldashboard.this, "Rate selected", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });

        // Set the first Spinner initially to GONE
        spinner.setVisibility(View.GONE);

        // Define the list of options for the Spinner
        final String[] options = {"Depression", "Anxiety disorder", "Eating disorder", "Low self-esteem", "Other"};

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
                toggleSpinnerVisibility();
            }
        });

        CardView Community = findViewById(R.id.Community);
        Community.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mentaldashboard.this, mario.class));
            }
        });

        CardView ongea = findViewById(R.id.Membership);
        ongea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mentaldashboard.this, ongea.class));
            }
        });

        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the back button click, e.g., go back to the previous activity
                onBackPressed();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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
            case 1:
            case 2:
            case 3:
            case 4:
                secondOptions = new String[]{"Default", "Online sessions", "Physical sessions"};
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
                String selectedOption = secondOptions[position];
                if ("Online sessions".equals(selectedOption) || "Physical sessions".equals(selectedOption)) {
                    Intent intent = new Intent(mentaldashboard.this, preference.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing
            }
        });

            Community.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mentaldashboard.this, mario.class);
                startActivity(intent);
            }
        });

    }
}
