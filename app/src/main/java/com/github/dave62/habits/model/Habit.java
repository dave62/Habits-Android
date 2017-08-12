package com.github.dave62.habits.model;

import java.util.Date;

public class Habit {

    private String name;
    private Date startingDate;
    private int timeThresholdInMin;

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

    public int getTimeThresholdInMin() {
        return timeThresholdInMin;
    }

    public void setTimeThresholdInMin(int timeThresholdInMin) {
        this.timeThresholdInMin = timeThresholdInMin;
    }

}
