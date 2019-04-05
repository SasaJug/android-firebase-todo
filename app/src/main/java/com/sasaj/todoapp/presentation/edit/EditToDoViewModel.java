package com.sasaj.todoapp.presentation.edit;

import android.arch.lifecycle.MutableLiveData;

import com.sasaj.todoapp.domain.usecases.EditToDoUseCase;
import com.sasaj.todoapp.domain.usecases.GetToDoUseCase;
import com.sasaj.todoapp.presentation.common.BaseViewModel;

import io.reactivex.annotations.Nullable;

import static com.sasaj.todoapp.presentation.edit.EditToDoViewState.*;

public class EditToDoViewModel extends BaseViewModel {

    private EditToDoUseCase editToDoUseCase;
    private GetToDoUseCase getToDoUseCase;
    public MutableLiveData<EditToDoViewState> editTodoLiveData = new MutableLiveData<>();
    public MutableLiveData<EditToDoViewState> getTodoLiveData = new MutableLiveData<>();

    public EditToDoViewModel(GetToDoUseCase getToDoUseCase, EditToDoUseCase editToDoUseCase) {
        this.editToDoUseCase = editToDoUseCase;
        this.getToDoUseCase = getToDoUseCase;
    }

    public void editToDo(@Nullable String todoKey, String title, String description, boolean completed) {
        editTodoLiveData.setValue(new EditToDoViewState(LOADING));
        addDisposable(editToDoUseCase.editTodo(todoKey, title, description, completed)
                .subscribe(
                        nextEvent -> editTodoLiveData.setValue(new EditToDoViewState(SUCCESS)),
                        error -> {
                            EditToDoViewState editToDoViewState = new EditToDoViewState(ERROR);
                            editToDoViewState.throwable = error;
                            editTodoLiveData.setValue(editToDoViewState);
                        },
                        () -> editTodoLiveData.setValue(new EditToDoViewState(COMPLETED))));
    }


    public void getToDo(String todoKey) {
        getTodoLiveData.setValue(new EditToDoViewState(LOADING));
        addDisposable(getToDoUseCase.getTodo(todoKey)
                .subscribe(
                        todo -> {
                            EditToDoViewState editToDoViewState = new EditToDoViewState(SUCCESS_LOADING_TODO);
                            editToDoViewState.todo = todo;
                            getTodoLiveData.setValue(editToDoViewState);
                        },
                        error -> {
                            EditToDoViewState editToDoViewState = new EditToDoViewState(ERROR);
                            editToDoViewState.throwable = error;
                            editTodoLiveData.setValue(editToDoViewState);
                        },
                        () -> editTodoLiveData.setValue(new EditToDoViewState(COMPLETED))
                )
        );
    }


}
