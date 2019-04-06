package com.sasaj.todoapp.di;


import com.sasaj.todoapp.presentation.edit.EditToDoDetailFragment;
import com.sasaj.todoapp.presentation.list.ToDoListActivity;
import com.sasaj.todoapp.presentation.view.ToDoDetailFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent {
    void inject(EditToDoDetailFragment editToDoDetailFragment);

    void inject(ToDoDetailFragment toDoDetailFragment);

    void inject(ToDoListActivity toDoListActivity);
}
