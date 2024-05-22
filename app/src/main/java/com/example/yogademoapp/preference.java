package com.example.yogademoapp;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class preference extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference);

        final Animation scaleUpAnimation = AnimationUtils.loadAnimation(this, R.anim.popup);

        // Define an array of CardView IDs and their corresponding messages
        int[] cardViewIds = {R.id.cardview1, R.id.cardview2, R.id.cardview3, R.id.cardview4, R.id.cardview5, R.id.cardview6};
        String[] messages = {
                "You selected Gender",
                "You selected location",
                "You selected Age",
                "You selected Religion",
                "Message for CardView 5",
                "Message for CardView 6"
        };

        // Loop through each CardView and set OnClickListener
        for (int i = 0; i < cardViewIds.length; i++) {
            final int cardViewId = cardViewIds[i];
            final String message = messages[i];
            CardView cardView = findViewById(cardViewId);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cardView.startAnimation(scaleUpAnimation);
                    showCustomDialog(message);
                }
            });
        }
    }

    private void showCustomDialog(String message) {
        Dialog dialog = new Dialog(this);
        View view = LayoutInflater.from(this).inflate(R.layout.activity_dialog_custom, null);
        dialog.setContentView(view);

        TextView dialogTitle = view.findViewById(R.id.dialog_title);
        TextView dialogMessage = view.findViewById(R.id.dialog_message);
        Button dialogButton = view.findViewById(R.id.dialog_button);

        dialogTitle.setText("Custom Dialog");
        dialogMessage.setText(message);

        // dialogButton.setOnClickListener(v -> dialog.dismiss());

        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dismiss the dialog
                dialog.dismiss();

                // Start the next activity
                startActivity(new Intent(preference.this, ConsultantActivity.class));
            }
        });

        dialog.show();


        //   tv.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //   public void onClick(View view) {
        //     startActivity(new Intent(MainActivity.this,pageTwo.class));

//  });
    }
}