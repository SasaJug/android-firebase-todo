package com.sasaj.todoapp.data;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.sasaj.todoapp.domain.Repository;
import com.sasaj.todoapp.domain.entities.ToDo;
import com.sasaj.todoapp.domain.entities.User;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.annotations.Nullable;

public class RepositoryImpl implements Repository {

    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;

    public RepositoryImpl(FirebaseAuth firebaseAuth) {
        this.firebaseAuth = firebaseAuth;
        this.firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabase.setPersistenceEnabled(true);
    }

    public FirebaseUser getCurrentUser() {
        return firebaseAuth.getCurrentUser();
    }


    private void writeNewUser(String userId, String name, String email) {
        User user = new User(name, email);
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        db.child("users").child(userId).setValue(user);
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


    private void addUserToDatabase() {
        if (getCurrentUser() != null) {
            writeNewUser(getCurrentUser().getUid(), usernameFromEmail(getCurrentUser().getEmail()), getCurrentUser().getEmail());
        }
    }


    @Override
    public Query getQueryForUserTodos() {
        if (getCurrentUser() != null) {
            // Order by opposite property so completed todos go to the bottom of the list.
            return firebaseDatabase.getReference().child("user-todos").child(getCurrentUser().getUid()).orderByChild("opposite");
        } else {
            return null;
        }
    }

    @Override
    public Observable<User> getCurrUser() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            addUserToDatabase();
            User user = new User();
            user.name = firebaseUser.getDisplayName();
            user.email = firebaseUser.getEmail();
            return Observable.just(user);
        } else {
            return Observable.error(new IllegalStateException("User is null"));
        }

    }

    @Override
    public Observable<ToDo> getTodo(String todoKey) {
        if (getCurrentUser() != null) {
            return RxFirebase.getObservable(firebaseDatabase.getReference().child("user-todos").child(getCurrentUser().getUid()).child(todoKey), ToDo.class);
        } else {
            return Observable.error(new IllegalStateException("User is unexpectedly null."));
        }
    }

    @Override
    public Observable<Object> editTodo(@Nullable String todoKey, String title, String description, boolean completed) {
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

            return RxFirebase.getObservable(firebaseDatabase.getReference().updateChildren(childUpdates), true);
        } else {
            return Observable.error(new IllegalStateException("User is unexpectedly null."));
        }
    }
}
