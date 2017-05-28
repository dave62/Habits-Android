package com.github.dave62.habits.model;


import com.github.dave62.habits.R;
import com.github.dave62.habits.application.HabitApplication;

public enum HabitPeriodicity {
    DAILY(0, R.string.dailyHabitPeriodicity),
    WEEKLY(1, R.string.weeklyHabitPeriodicity),
    MONTHLY(2, R.string.monthlyHabitPeriodicity);

    private int id;
    private int stringRessourceId;

    HabitPeriodicity(int id, int stringRessourceId) {
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

    public static HabitPeriodicity[] getSpinnerValues() {
        return new HabitPeriodicity[]{WEEKLY, MONTHLY};
    }


}
