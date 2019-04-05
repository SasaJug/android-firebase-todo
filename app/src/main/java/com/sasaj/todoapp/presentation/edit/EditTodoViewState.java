package com.sasaj.todoapp.presentation.edit;

public class EditTodoViewState {
    public static final int LOADING = 0;
    public static final int SUCCESS = 1;
    public static final int ERROR = 2;
    public static final int COMPLETED = 3;

    public int state;
    public Throwable throwable;

    public EditTodoViewState(int state) {
        this.state = state;
    }
}
