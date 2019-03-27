package com.sasaj.todoapp.data;

import android.content.Intent;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.sasaj.todoapp.entity.ToDo;
import com.sasaj.todoapp.entity.User;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Repository{

    private static Repository INSTANCE;


    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;

    public static Repository INSTANCE (){
        if(INSTANCE == null){
            INSTANCE = new Repository();
        }
        return INSTANCE;
    }

    private Repository (){
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabase.setPersistenceEnabled(true);
    }

    public FirebaseUser getCurrentUser(){
        return firebaseAuth.getCurrentUser();
    }

    public Intent getAuthUIIntent(){
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.FacebookBuilder().build());

        // Create sign-in intent
        return AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setAlwaysShowSignInMethodScreen(true)
                .build();
    }

    public void addUserToDatabase(){
        FirebaseUser user = firebaseAuth.getCurrentUser();
        writeNewUser(user.getUid(), usernameFromEmail(user.getEmail()), user.getEmail() );
    }

    private void writeNewUser(String userId, String name, String email) {
        User user = new User(name, email);
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        db.child("users").child(userId).setValue(user);
    }

    private String usernameFromEmail(String email) {
        if(email != null){
            if (email.contains("@")) {
                return email.split("@")[0];
            } else {
                return email;
            }
        } else {
            return "";
        }
    }

    public FirebaseRecyclerOptions getToDoListOptions(){
        Query todosQuery = getQueryForUserTodos();

        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<ToDo>()
                .setQuery(todosQuery, ToDo.class)
                .build();
        return options;
    }


    public Query getQueryForUsers() {
        return firebaseDatabase.getReference().child("users").child(firebaseAuth.getCurrentUser().getUid());
    }

    public Query getQueryForUserTodos() {
        return firebaseDatabase.getReference().child("user-todos").child(firebaseAuth.getCurrentUser().getUid());
    }

    public Query getQueryForSingleUserTodo(String todoKey) {
        return firebaseDatabase.getReference().child("user-todos").child(firebaseAuth.getCurrentUser().getUid()).child(todoKey);
    }

    public void writeNewTodo(String title, String description, String todoKey) {
        String timestamp = Long.toString(System.currentTimeMillis());
        ToDo toDo = new ToDo(getCurrentUser().getUid(), title, description, timestamp);

        if(todoKey == null){
            todoKey = firebaseDatabase.getReference().child("todos").push().getKey();
        }
        Map<String, Object> postValues = toDo.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/todos/" + todoKey, postValues);
        childUpdates.put("/user-todos/" + getCurrentUser().getUid() + "/" + todoKey, postValues);

        firebaseDatabase.getReference().updateChildren(childUpdates);
    }
}
