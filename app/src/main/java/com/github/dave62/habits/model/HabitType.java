package com.github.dave62.habits.model;


import com.github.dave62.habits.R;
import com.github.dave62.habits.application.HabitApplication;

import org.androidannotations.annotations.App;

public enum HabitType {
    BASIC(0, R.string.basicHabitType),
    CUSTOM(1, R.string.customHabitType);

    private int id;
    private int stringRessourceId;

    HabitType(int id, int stringRessourceId) {
        this.id = id;
        this.stringRessourceId = stringRessourceId;
    }

    @Override
    public String toString() {
        return HabitApplication.getContext().getString(stringRessourceId);
    }

    public int getId() {
        return id;
    }
}
