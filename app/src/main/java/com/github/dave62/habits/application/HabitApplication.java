package com.github.dave62.habits.application;

import android.app.Application;
import android.content.Context;

import io.realm.Realm;


public class HabitApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        context = this;
    }

    public static Context getContext() {
        return context;
    }
}
