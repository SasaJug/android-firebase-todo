package com.sasaj.todoapp.domain;

import com.sasaj.todoapp.domain.entities.ToDo;

import io.reactivex.Observable;

public interface Repository {

    Observable<ToDo> getTodo(String todoKey);
    Observable<Object> editTodo(String todoKey, String title, String description, boolean completed);
}
