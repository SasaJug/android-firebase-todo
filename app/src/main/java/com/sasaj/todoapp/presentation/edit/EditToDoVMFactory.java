package com.sasaj.todoapp.presentation.edit;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.sasaj.todoapp.domain.usecases.EditToDoUseCase;
import com.sasaj.todoapp.domain.usecases.GetToDoUseCase;

@SuppressWarnings("unchecked")
public class EditToDoVMFactory implements ViewModelProvider.Factory {

    private EditToDoUseCase editToDoUseCase;
    private GetToDoUseCase getToDoUseCase;

    public EditToDoVMFactory(GetToDoUseCase getToDoUseCase, EditToDoUseCase editToDoUseCase) {
        this.editToDoUseCase = editToDoUseCase;
        this.getToDoUseCase = getToDoUseCase;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new EditToDoViewModel(getToDoUseCase, editToDoUseCase);
    }
}
