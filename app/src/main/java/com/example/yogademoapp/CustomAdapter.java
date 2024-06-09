package com.example.yogademoapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class CustomAdapter extends ArrayAdapter<UserOne> {

    private LayoutInflater inflater;

    public CustomAdapter(Context context, List<UserOne> users) {
        super(context, 0, users);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_user, parent, false);
            holder = new ViewHolder();
            holder.userEmailTextView = convertView.findViewById(R.id.userEmailTextView);
            holder.userImageView = convertView.findViewById(R.id.userImageView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        UserOne user = getItem(position);
        if (user != null) {
            holder.userEmailTextView.setText(user.getEmail());
            holder.userImageView.setImageResource(user.getImageResId());
        }

        return convertView;
    }

    static class ViewHolder {
        TextView userEmailTextView;
        ImageView userImageView;
    }
}
