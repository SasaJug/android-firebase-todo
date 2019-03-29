package com.sasaj.todoapp.entity;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class ToDo {
    public String userId;
    public String title;
    public String description;
    public boolean completed;
    public String timestamp;

    // Default constructor required for calls to DataSnapshot.getValue(ToDo.class)
    public ToDo() {
    }

    public ToDo(String userId, String title, String description, boolean completed, String timestamp) {
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.completed = completed;
        this.timestamp = timestamp;
    }

    // [START post_to_map]
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("userId", userId);
        result.put("title", title);
        result.put("description", description);
        result.put("completed", completed);
        result.put("timestamp", timestamp);
        return result;
    }
    // [END post_to_map]
}
