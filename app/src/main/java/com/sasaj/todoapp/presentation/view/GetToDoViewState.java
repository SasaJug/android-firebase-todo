package com.sasaj.todoapp.presentation.view;

import com.sasaj.todoapp.domain.entities.ToDo;

public class GetToDoViewState {
    public static final int LOADING = 0;
    public static final int SUCCESS = 1;
    public static final int ERROR = 2;
    public static final int COMPLETED = 3;

    public int state;
    public ToDo todo;
    public Throwable throwable;

    public GetToDoViewState(int state) {
        this.state = state;
    }
}
