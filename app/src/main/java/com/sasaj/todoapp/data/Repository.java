package com.sasaj.todoapp.data;

import android.content.Context;
import android.content.Intent;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.sasaj.todoapp.domain.ToDo;
import com.sasaj.todoapp.domain.User;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Repository {

    private static Repository INSTANCE;

    private FirebaseDatabase firebaseDatabase;

    public static Repository INSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new Repository();
        }
        return INSTANCE;
    }

    private Repository() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabase.setPersistenceEnabled(true);
    }

    public FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public Intent getAuthUIIntent() {
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

    public void signOut(Context context, Intent intent) {
        AuthUI.getInstance().signOut(context).addOnCompleteListener(task -> context.startActivity(intent));
    }

    public void addUserToDatabase() {
        if (getCurrentUser() != null) {
            writeNewUser(getCurrentUser().getUid(), usernameFromEmail(getCurrentUser().getEmail()), getCurrentUser().getEmail());
        }
    }

    private void writeNewUser(String userId, String name, String email) {
        User user = new User(name, email);
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        db.child("users").child(userId).setValue(user);
    }


    public Query getQueryForUsers() {
        if (getCurrentUser() != null) {
            return firebaseDatabase.getReference().child("users").child(getCurrentUser().getUid());
        } else {
            return null;
        }
    }

    public Query getQueryForUserTodos() {
        if (getCurrentUser()!= null) {
            // Order by opposite property so completed todos go to the bottom of the list.
            return firebaseDatabase.getReference().child("user-todos").child(getCurrentUser().getUid()).orderByChild("opposite");
        } else {
            return null;
        }
    }

    public Query getQueryForSingleUserTodo(String todoKey) {
        if (getCurrentUser() != null) {
            return firebaseDatabase.getReference().child("user-todos").child(getCurrentUser().getUid()).child(todoKey);
        } else {
            return null;
        }
    }


    public void writeNewTodo(String title, String description, boolean completed, String todoKey) {
        if (getCurrentUser() != null) {
            String timestamp = Long.toString(System.currentTimeMillis());
            ToDo toDo = new ToDo(getCurrentUser().getUid(), title, description, completed, timestamp);

            if (todoKey == null) {
                todoKey = firebaseDatabase.getReference().child("todos").push().getKey();
            }
            Map<String, Object> postValues = toDo.toMap();

            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put("/todos/" + todoKey, postValues);
            childUpdates.put("/user-todos/" + getCurrentUser().getUid() + "/" + todoKey, postValues);

            firebaseDatabase.getReference().updateChildren(childUpdates);
        }
    }

    private String usernameFromEmail(String email) {
        if (email != null) {
            if (email.contains("@")) {
                return email.split("@")[0];
            } else {
                return email;
            }
        } else {
            return "";
        }
    }

    public FirebaseRecyclerOptions<ToDo> getToDoListOptions() {
        Query todosQuery = getQueryForUserTodos();

        return new FirebaseRecyclerOptions.Builder<ToDo>()
                .setQuery(todosQuery, ToDo.class)
                .build();
    }
}
