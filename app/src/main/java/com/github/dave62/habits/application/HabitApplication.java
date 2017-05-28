package com.github.dave62.habits.application;

import android.app.Application;
import android.content.Context;


public class HabitApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }

    public static Context getContext(){
        return context;
    }
}
