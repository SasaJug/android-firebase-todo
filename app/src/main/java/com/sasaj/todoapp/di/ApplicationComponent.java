package com.sasaj.todoapp.di;


import com.sasaj.todoapp.presentation.edit.EditToDoDetailFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent {
    void inject(EditToDoDetailFragment editToDoDetailFragment);
}
