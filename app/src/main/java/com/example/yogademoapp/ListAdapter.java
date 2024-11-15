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

        // Views for the list item
        ImageView imageView = convertView.findViewById(R.id.profile_pic);
        TextView userName = convertView.findViewById(R.id.personname);
        TextView lastMsg = convertView.findViewById(R.id.lastmessage);
        TextView time = convertView.findViewById(R.id.msgtime);
        TextView ratingTextView = convertView.findViewById(R.id.ratingTextView);  // TextView for the rating stars

        // Set data to views
        imageView.setImageResource(user.getImageId());
        userName.setText(user.getName());
        lastMsg.setText(user.getLastMessage());
        time.setText(user.getLastMsgTime());

        // Set the rating as stars
        ratingTextView.setText(generateStars(user.getRating()));

        return convertView;
    }

    // Helper method to convert rating into stars
    private String generateStars(int rating) {
        return new String(new char[rating]).replace("\0", "⭐");
    }
}
