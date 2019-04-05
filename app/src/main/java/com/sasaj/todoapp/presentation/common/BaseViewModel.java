package com.sasaj.todoapp.presentation.common;

import android.arch.lifecycle.ViewModel;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class BaseViewModel extends ViewModel {

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    protected void addDisposable(Disposable disposable) {
        compositeDisposable.add(disposable);
    }

    private void clearDisposables() {
        compositeDisposable.clear();
    }

    @Override
    protected void onCleared() {
        clearDisposables();
        super.onCleared();
    }
}
