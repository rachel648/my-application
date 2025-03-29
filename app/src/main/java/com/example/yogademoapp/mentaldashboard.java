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
import android.widget.RatingBar;
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
                        Intent intent = new Intent(mentaldashboard.this, GreenCard.class);
                        startActivity(intent);
                        break;
                    case R.id.info:
                        Toast.makeText(mentaldashboard.this, "About selected", Toast.LENGTH_SHORT).show();
                        Intent intent1 = new Intent(mentaldashboard.this, about.class);
                        startActivity(intent1);
                        break;
                    case R.id.share:
                        Toast.makeText(mentaldashboard.this, "Share selected", Toast.LENGTH_SHORT).show();
                        Intent sendIntent = new Intent(Intent.ACTION_SEND);
                        sendIntent.setType("text/plain");
                        sendIntent.putExtra(Intent.EXTRA_TEXT, "Check out this awesome app!");

                        String[] options = {"WhatsApp", "Instagram"};
                        new AlertDialog.Builder(mentaldashboard.this)
                                .setTitle("Share via")
                                .setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (which == 0) {
                                            sendIntent.setPackage("com.whatsapp");
                                            try {
                                                startActivity(sendIntent);
                                            } catch (android.content.ActivityNotFoundException ex) {
                                                Toast.makeText(mentaldashboard.this, "WhatsApp not installed", Toast.LENGTH_SHORT).show();
                                            }
                                        } else if (which == 1) {
                                            sendIntent.setPackage("com.instagram.android");
                                            try {
                                                startActivity(sendIntent);
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
                        new AlertDialog.Builder(mentaldashboard.this)
                                .setTitle("Rate Us")
                                .setMessage("Would you like to rate the app?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        showRatingDialog();
                                    }
                                })
                                .setNegativeButton("No", null)
                                .show();
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });

        spinner.setVisibility(View.GONE);
        final String[] options = {"Depression", "Anxiety disorder", "Eating disorder", "Low self-esteem","drug abuse", "Other"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                updateSecondSpinner(position);
                secondSpinner.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {}
        });

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isTextViewVisible) {
                    textViewOnTop.setVisibility(View.GONE);
                } else {
                    textViewOnTop.setVisibility(View.VISIBLE);
                }
                isTextViewVisible = !isTextViewVisible;
                spinner.setVisibility(spinner.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                secondSpinner.setVisibility(View.GONE);
            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                secondSpinner.setVisibility(View.GONE);
                toggleSpinnerVisibility();
            }
        });

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

        ArrayAdapter<String> secondAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, secondOptions);
        secondAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        secondSpinner.setAdapter(secondAdapter);

        secondSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedOption = secondOptions[position];
                if ("Online sessions".equals(selectedOption) || "Physical sessions".equals(selectedOption)) {
                    Intent intent = new Intent(mentaldashboard.this, preference.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {}
        });
    }

    private void showRatingDialog() {
        final RatingBar ratingBar = new RatingBar(mentaldashboard.this);
        ratingBar.setNumStars(5);
        ratingBar.setStepSize(1);
        ratingBar.setRating(0);
        ratingBar.setIsIndicator(false);

        new AlertDialog.Builder(mentaldashboard.this)
                .setTitle("Rate the app")
                .setMessage("Please rate us!")
                .setView(ratingBar)
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        float rating = ratingBar.getRating();
                        if (rating > 0) {
                            Toast.makeText(mentaldashboard.this, "Thank you for your rating: " + rating + " stars!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(mentaldashboard.this, "Please select a rating before submitting.", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
