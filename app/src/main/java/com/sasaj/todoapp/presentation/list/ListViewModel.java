package com.sasaj.todoapp.presentation.list;

import android.arch.lifecycle.MutableLiveData;

import com.sasaj.todoapp.domain.usecases.GetCurrentUserUseCase;
import com.sasaj.todoapp.presentation.common.BaseViewModel;

import static com.sasaj.todoapp.presentation.list.ListViewState.*;

public class ListViewModel extends BaseViewModel {

    private GetCurrentUserUseCase getCurrentUserUseCase;
    public MutableLiveData<ListViewState> listLiveData = new MutableLiveData<>();

    public ListViewModel(GetCurrentUserUseCase getCurrentUserUseCase) {
        this.getCurrentUserUseCase = getCurrentUserUseCase;
    }


    public void getCurrentUser() {
        listLiveData.setValue(new ListViewState(LOADING));
        addDisposable(getCurrentUserUseCase.getCurrentUser()
                .subscribe(
                        user -> {
                            ListViewState listViewState = new ListViewState(SUCCESS);
                            listLiveData.setValue(listViewState);
                        },
                        error -> {
                            ListViewState listViewState = new ListViewState(ERROR);
                            listViewState.throwable = error;
                            listLiveData.setValue(listViewState);
                        },
                        () -> listLiveData.setValue(new ListViewState(COMPLETED))
                )
        );
    }
}
