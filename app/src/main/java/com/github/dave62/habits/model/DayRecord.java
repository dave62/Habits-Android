package com.github.dave62.habits.model;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.LinkingObjects;
import io.realm.annotations.PrimaryKey;

public class DayRecord extends RealmObject {

    @PrimaryKey
    private String id;
    private Date dayOfRecord;
    private int timeSpentInMin;
    @LinkingObjects("records")
    private final RealmResults<Habit> habit = null;

    public String getId() {
        return id;
    }

    public Date getDayOfRecord() {
        return dayOfRecord;
    }

    public void setDayOfRecord(Date dayOfRecord) {
        this.dayOfRecord = dayOfRecord;
    }

    public int getTimeSpentInMin() {
        return timeSpentInMin;
    }

    public void setTimeSpentInMin(int timeSpentInMin) {
        this.timeSpentInMin = timeSpentInMin;
    }
}
