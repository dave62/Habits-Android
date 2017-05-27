package com.github.dave62.habits.model;

import java.util.Date;

public class Habit {

    private String name;
    private Date startingDate;
    private HabitType type;
    private int goalPerPeriod;
    private HabitPeriodicity periodicity;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStartingDate() {
        return startingDate;
    }

    public void setStartingDate(Date startingDate) {
        this.startingDate = startingDate;
    }

    public HabitType getType() {
        return type;
    }

    public void setType(HabitType type) {
        this.type = type;
    }

    public int getGoalPerPeriod() {
        return goalPerPeriod;
    }

    public void setGoalPerPeriod(int goalPerPeriod) {
        this.goalPerPeriod = goalPerPeriod;
    }

    public HabitPeriodicity getPeriodicity() {
        return periodicity;
    }

    public void setPeriodicity(HabitPeriodicity periodicity) {
        this.periodicity = periodicity;
    }
}
