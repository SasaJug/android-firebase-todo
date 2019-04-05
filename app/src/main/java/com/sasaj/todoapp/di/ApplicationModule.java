package com.sasaj.todoapp.di;

import android.content.Context;

import com.sasaj.todoapp.data.RepositoryImpl;
import com.sasaj.todoapp.domain.Repository;
import com.sasaj.todoapp.domain.common.AsyncTransformer;
import com.sasaj.todoapp.domain.usecases.EditToDoUseCase;
import com.sasaj.todoapp.domain.usecases.GetToDoUseCase;
import com.sasaj.todoapp.presentation.edit.EditToDoVMFactory;
import com.sasaj.todoapp.presentation.view.ToDoDetailVMFactory;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {

    Context context;

    public ApplicationModule(Context context) {
        this.context = context;
    }

    @Provides
    @Singleton
    Repository provideRepository() {
        return new RepositoryImpl();
    }

    @Provides
    @Singleton
    EditToDoUseCase provideEditTodoUseCase(Repository repository) {
        return new EditToDoUseCase(repository, new AsyncTransformer());
    }

    @Provides
    @Singleton
    GetToDoUseCase provideGetTodoUseCase(Repository repository) {
        return new GetToDoUseCase(repository, new AsyncTransformer());
    }

    @Provides
    @Singleton
    EditToDoVMFactory provideEditTodoViewModelFactory(GetToDoUseCase getToDoUseCase, EditToDoUseCase editToDoUseCase) {
        return new EditToDoVMFactory(getToDoUseCase, editToDoUseCase);
    }

    @Provides
    @Singleton
    ToDoDetailVMFactory provideTodoDetailViewModelFactory(GetToDoUseCase getToDoUseCase) {
        return new ToDoDetailVMFactory(getToDoUseCase);
    }
}
