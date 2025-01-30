package com.example.yogademoapp;

public class Group {
    private String groupId;
    private String groupName;
    private String groupDescription;

    public Group() {
        // Default constructor required for Firebase
    }

    public Group(String groupId, String groupName, String groupDescription) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.groupDescription = groupDescription;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getGroupDescription() {
        return groupDescription;
    }
}
