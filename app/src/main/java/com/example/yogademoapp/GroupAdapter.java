package com.example.yogademoapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class GroupAdapter extends ArrayAdapter<Group> {

    public GroupAdapter(Context context, ArrayList<Group> groups) {
        super(context, 0, groups);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.group_item, parent, false);
        }

        Group group = getItem(position);

        TextView groupNameTextView = convertView.findViewById(R.id.groupNameTextView);
        TextView groupDescTextView = convertView.findViewById(R.id.groupDescTextView);

        if (group != null) {
            groupNameTextView.setText(group.getGroupName());
            groupDescTextView.setText(group.getGroupDescription());
        }

        return convertView;
    }
}
