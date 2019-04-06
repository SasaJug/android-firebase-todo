package com.sasaj.todoapp;

import android.app.Application;

import com.sasaj.todoapp.di.ApplicationComponent;
import com.sasaj.todoapp.di.ApplicationModule;
import com.sasaj.todoapp.di.DaggerApplicationComponent;

public class TodoApplication extends Application {

    public ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(getApplicationContext()))
                .build();
    }
}
