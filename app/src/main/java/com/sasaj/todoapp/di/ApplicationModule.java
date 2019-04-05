package com.sasaj.todoapp.di;

import android.content.Context;

import com.sasaj.todoapp.data.RepositoryImpl;
import com.sasaj.todoapp.domain.Repository;
import com.sasaj.todoapp.domain.common.AsyncTransformer;
import com.sasaj.todoapp.domain.usecases.EditToDoUseCase;
import com.sasaj.todoapp.presentation.edit.EditTodoVMFactory;

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
    EditTodoVMFactory provideEditTodoViewModelFactory(EditToDoUseCase editToDoUseCase) {
        return new EditTodoVMFactory(editToDoUseCase);
    }
}
