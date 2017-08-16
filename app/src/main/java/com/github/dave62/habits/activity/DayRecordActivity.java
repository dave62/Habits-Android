package com.github.dave62.habits.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CalendarView;

import com.github.dave62.habits.R;
import com.github.dave62.habits.model.Habit;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import io.realm.Realm;

@EActivity(R.layout.activity_day_record)
public class DayRecordActivity extends AppCompatActivity {

    @ViewById
    protected CalendarView calendarView;

    @Extra
    protected String currentHabitId;

    protected Habit currentHabit;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance();
    }

    @AfterViews
    protected void afterViews() {
        currentHabit = realm.where(Habit.class).equalTo("id", currentHabitId).findFirst();
        calendarView.setMinDate(currentHabit.getStartingDate().getTime());
    }
}
