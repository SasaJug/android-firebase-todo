package com.sasaj.todoapp;

import android.app.Application;

import com.google.firebase.auth.FirebaseUser;

public class ToDoApplication extends Application {

    public FirebaseUser user;

    public FirebaseUser getUser() {
        return user;
    }

    public void setUser(FirebaseUser user) {
        this.user = user;
    }
}
