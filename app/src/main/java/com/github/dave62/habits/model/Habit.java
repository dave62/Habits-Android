package com.github.dave62.habits.model;

import java.io.Serializable;
import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Habit extends RealmObject implements Serializable {

    @PrimaryKey
    private String id;

    private String name;
    private Date startingDate;
    private int timeThresholdInMin;
    private RealmList<DayRecord> records = new RealmList<>();

    public String getId() {
        return id;
    }

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

    public RealmList<DayRecord> getRecords() {
        return records;
    }

    public void setRecords(RealmList<DayRecord> records) {
        this.records = records;
    }
}
