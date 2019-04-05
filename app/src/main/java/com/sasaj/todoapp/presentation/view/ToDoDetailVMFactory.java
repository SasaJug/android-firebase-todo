package com.sasaj.todoapp.presentation.view;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.sasaj.todoapp.domain.usecases.GetToDoUseCase;

@SuppressWarnings("unchecked")
public class ToDoDetailVMFactory implements ViewModelProvider.Factory {

    private GetToDoUseCase getToDoUseCase;

    public ToDoDetailVMFactory(GetToDoUseCase getToDoUseCase) {
        this.getToDoUseCase = getToDoUseCase;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ToDoDetailViewModel(getToDoUseCase);
    }
}