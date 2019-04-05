package com.sasaj.todoapp.presentation.view;

import android.arch.lifecycle.MutableLiveData;

import com.sasaj.todoapp.domain.usecases.GetToDoUseCase;
import com.sasaj.todoapp.presentation.common.BaseViewModel;

import static com.sasaj.todoapp.presentation.view.GetToDoViewState.*;

public class ToDoDetailViewModel extends BaseViewModel {

    private GetToDoUseCase getToDoUseCase;
    public MutableLiveData<GetToDoViewState> getTodoLiveData = new MutableLiveData<>();

    public ToDoDetailViewModel(GetToDoUseCase getToDoUseCase) {
        this.getToDoUseCase = getToDoUseCase;
    }

    public void getToDo(String todoKey) {
        getTodoLiveData.setValue(new GetToDoViewState(LOADING));
        addDisposable(getToDoUseCase.getTodo(todoKey)
                .subscribe(
                        todo -> {
                            GetToDoViewState getToDoViewState = new GetToDoViewState(SUCCESS);
                            getToDoViewState.todo = todo;
                            getTodoLiveData.setValue(getToDoViewState);
                        },
                        error -> {
                            GetToDoViewState getToDoViewState = new GetToDoViewState(ERROR);
                            getToDoViewState.throwable = error;
                            getTodoLiveData.setValue(getToDoViewState);
                        },
                        () -> getTodoLiveData.setValue(new GetToDoViewState(COMPLETED))
                )
        );
    }
}
