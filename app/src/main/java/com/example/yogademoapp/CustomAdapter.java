package com.example.yogademoapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class CustomAdapter extends ArrayAdapter<UserOne> {

    public CustomAdapter(@NonNull Context context, @NonNull List<UserOne> userList) {
        super(context, 0, userList);  // The third parameter is the list of users.
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // ViewHolder pattern to improve performance
        ViewHolder viewHolder;

        // Reuse convertView or create a new one if it's null
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_user, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.userImage = convertView.findViewById(R.id.userImage);
            viewHolder.usernameText = convertView.findViewById(R.id.usernameText);
            viewHolder.emailText = convertView.findViewById(R.id.emailText);
            convertView.setTag(viewHolder);  // Tag the view for future reference
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Get the current user object
        UserOne currentUser = getItem(position);  // You can use getItem() as it is already passed by ArrayAdapter

        if (currentUser != null) {
            // Set the views with data from the current user
            viewHolder.userImage.setImageResource(currentUser.getImageId());
            viewHolder.usernameText.setText(currentUser.getUsername());
            viewHolder.emailText.setText(currentUser.getEmail());
        }

        return convertView;
    }

    // ViewHolder class to hold references to avoid unnecessary calls to findViewById
    private static class ViewHolder {
        ImageView userImage;
        TextView usernameText;
        TextView emailText;
    }
}
