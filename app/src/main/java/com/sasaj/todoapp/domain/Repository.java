package com.sasaj.todoapp.domain;

import com.google.firebase.database.Query;
import com.sasaj.todoapp.domain.entities.ToDo;
import com.sasaj.todoapp.domain.entities.User;

import io.reactivex.Observable;

public interface Repository {

    Observable<User> getCurrUser();
    Observable<ToDo> getTodo(String todoKey);
    Observable<Object> editTodo(String todoKey, String title, String description, boolean completed);

    Query getQueryForUserTodos();
}
