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

    private LayoutInflater inflater;
    private List<UserOne> users;

    public CustomAdapter(Context context, List<UserOne> users) {
        super(context, 0, users);
        inflater = LayoutInflater.from(context);
        this.users = users;
    }

    // ViewHolder pattern to optimize performance
    static class ViewHolder {
        ImageView userImage;
        TextView usernameText;
        TextView emailText;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            // Inflate the layout (using recommended layout name)
            convertView = inflater.inflate(R.layout.item_user, parent, false);
            holder = new ViewHolder();
            // Initialize views using the recommended IDs
            holder.userImage = convertView.findViewById(R.id.userImage);
            holder.usernameText = convertView.findViewById(R.id.usernameText);
            holder.emailText = convertView.findViewById(R.id.emailText);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Get the current UserOne object
        UserOne currentUser = getItem(position);
        if (currentUser != null) {
            // Set the image, username, and email
            holder.userImage.setImageResource(currentUser.getImageId());
            holder.usernameText.setText(currentUser.getUsername());
            holder.emailText.setText(currentUser.getEmail());
        }

        return convertView;
    }
}
