package com.sasaj.todoapp.presentation.edit;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.sasaj.todoapp.domain.usecases.EditToDoUseCase;

public class EditTodoVMFactory implements ViewModelProvider.Factory {

    private EditToDoUseCase editToDoUseCase;

    public EditTodoVMFactory(EditToDoUseCase editToDoUseCase) {
        this.editToDoUseCase = editToDoUseCase;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new EditTodoViewModel(editToDoUseCase);
    }
}
