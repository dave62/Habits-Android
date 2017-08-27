package com.github.dave62.habits.application;

import android.app.Application;

import io.realm.Realm;


public class HabitApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
    }
}
