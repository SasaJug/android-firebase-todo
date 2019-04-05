package com.sasaj.todoapp.presentation.edit;

import android.arch.lifecycle.MutableLiveData;

import com.sasaj.todoapp.domain.usecases.EditToDoUseCase;
import com.sasaj.todoapp.presentation.common.BaseViewModel;

import io.reactivex.annotations.Nullable;

public class EditTodoViewModel extends BaseViewModel {

    private EditToDoUseCase editToDoUseCase;
    public MutableLiveData<EditTodoViewState> editTodoLiveData = new MutableLiveData<>();

    public EditTodoViewModel(EditToDoUseCase editToDoUseCase) {
        this.editToDoUseCase = editToDoUseCase;
    }

    public void editToDo(@Nullable String todoKey, String title, String description, boolean completed) {
        editTodoLiveData.setValue(new EditTodoViewState(EditTodoViewState.LOADING));
        addDisposable(editToDoUseCase.editTodo(todoKey, title, description, completed)
                .subscribe(
                        nextEvent -> editTodoLiveData.setValue(new EditTodoViewState(EditTodoViewState.SUCCESS)),
                        error -> {
                            EditTodoViewState editTodoViewState = new EditTodoViewState(EditTodoViewState.ERROR);
                            editTodoViewState.throwable = error;
                            editTodoLiveData.setValue(editTodoViewState);
                            },
                        () -> editTodoLiveData.setValue(new EditTodoViewState(EditTodoViewState.COMPLETED))));
    }


}
