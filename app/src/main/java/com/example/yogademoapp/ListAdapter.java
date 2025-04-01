package com.example.yogademoapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

public class ListAdapter extends ArrayAdapter<User> {

    public ListAdapter(Context context, ArrayList<User> userArrayList) {
        super(context, R.layout.list_item, userArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        User user = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        // Initialize views
        ImageView imageView = convertView.findViewById(R.id.profile_pic);
        TextView userName = convertView.findViewById(R.id.personname);
        TextView lastMsg = convertView.findViewById(R.id.lastmessage);
        TextView time = convertView.findViewById(R.id.msgtime);
        TextView ratingTextView = convertView.findViewById(R.id.ratingTextView);

        // Set text data
        userName.setText(user.getName());
        lastMsg.setText(user.getLastMessage());
        time.setText(user.getLastMsgTime());
        ratingTextView.setText(generateStars(user.getRating()));

        // Handle image loading - THIS IS THE CRUCIAL PART
        if (user.getImageUrl() != null && !user.getImageUrl().isEmpty()) {
            // Load from URL using Glide
            Glide.with(getContext())
                    .load(user.getImageUrl())
                    .diskCacheStrategy(DiskCacheStrategy.ALL) // Cache image
                    .placeholder(R.drawable.updatedprofile) // Show while loading
                    .error(R.drawable.updatedprofile) // Show if error
                    .into(imageView);
        } else {
            // Fall back to local image resource
            imageView.setImageResource(user.getImageId());
        }

        return convertView;
    }

    private String generateStars(int rating) {
        StringBuilder stars = new StringBuilder();
        int maxRating = 5; // Assuming 5-star rating system

        for (int i = 0; i < maxRating; i++) {
            if (i < rating) {
                stars.append("⭐");
            } else {
                stars.append("☆");
            }
        }
        return stars.toString();
    }
}