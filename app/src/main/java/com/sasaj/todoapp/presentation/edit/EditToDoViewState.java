package com.sasaj.todoapp.presentation.edit;

import com.sasaj.todoapp.domain.entities.ToDo;

public class EditToDoViewState {
    public static final int LOADING = 0;
    public static final int SUCCESS = 1;
    public static final int ERROR = 2;
    public static final int COMPLETED = 3;
    public static final int SUCCESS_LOADING_TODO = 4;

    public int state;
    public ToDo todo;
    public Throwable throwable;

    public EditToDoViewState(int state) {
        this.state = state;
    }
}
