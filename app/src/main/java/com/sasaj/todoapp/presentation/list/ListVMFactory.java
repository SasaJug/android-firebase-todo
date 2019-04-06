package com.sasaj.todoapp.presentation.list;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.sasaj.todoapp.domain.usecases.GetCurrentUserUseCase;

@SuppressWarnings("unchecked")
public class ListVMFactory  implements ViewModelProvider.Factory {

    private GetCurrentUserUseCase getCurrentUserUseCase;

    public ListVMFactory(GetCurrentUserUseCase getCurrentUserUseCase) {
        this.getCurrentUserUseCase = getCurrentUserUseCase;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ListViewModel(getCurrentUserUseCase);
    }
}
