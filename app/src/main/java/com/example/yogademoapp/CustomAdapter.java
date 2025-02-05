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
    private Context context;
    private List<UserOne> userList;

    public CustomAdapter(@NonNull Context context, @NonNull List<UserOne> userList) {
        super(context, 0, userList);
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // Inflate custom list item layout if needed
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
        }

        // Get the current user object
        UserOne currentUser = userList.get(position);

        // Find views in the custom layout
        ImageView userImage = convertView.findViewById(R.id.userImage);
        TextView usernameText = convertView.findViewById(R.id.usernameText);
        TextView emailText = convertView.findViewById(R.id.emailText);

        // Set the views with data from the current user
        userImage.setImageResource(currentUser.getImageId());
        usernameText.setText(currentUser.getUsername());
        emailText.setText(currentUser.getEmail());

        return convertView;
    }
}
