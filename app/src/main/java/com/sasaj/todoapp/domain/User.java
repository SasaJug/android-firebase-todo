package com.sasaj.todoapp.domain;

public class User {
    public String name;
    public String email;

    // Default constructor required for calls to DataSnapshot.getValue(User.class)
    public User() {
    }

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
