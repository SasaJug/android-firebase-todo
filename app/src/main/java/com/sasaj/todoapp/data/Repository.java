package com.sasaj.todoapp.data;

import com.google.firebase.database.FirebaseDatabase;

public class Repository{

    private static FirebaseDatabase mDatabase;

    public static FirebaseDatabase getDatabase() {
        if (mDatabase == null) {
            mDatabase = FirebaseDatabase.getInstance();
            mDatabase.setPersistenceEnabled(true);
        }
        return mDatabase;
    }
}
