package com.github.dave62.habits.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.CalendarView;
import android.widget.TabHost;

import com.github.dave62.habits.R;
import com.github.dave62.habits.dialog.DayRecordDialog;
import com.github.dave62.habits.dialog.DayRecordDialog_;
import com.github.dave62.habits.model.Habit;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;

@EActivity(R.layout.activity_day_record)
public class DayRecordActivity extends AppCompatActivity {

    @ViewById
    protected CalendarView calendarView;
    @ViewById
    protected TabHost tabHost;

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

        initializeTabs();

        //TODO : Handle async request ?
        currentHabit = realm.where(Habit.class).equalTo("id", currentHabitId).findFirst();
        calendarView.setMinDate(currentHabit.getStartingDate().getTime());
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Date selectedDate = getDate(year, month, dayOfMonth);

                //TODO : same code as in the habit adapter, find a way to factorize
                FragmentManager manager = DayRecordActivity.this.getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                Fragment previousInstance = manager.findFragmentByTag("dayRecordActivity");
                if (previousInstance != null) {
                    transaction.remove(previousInstance);
                }
                transaction.addToBackStack(null);
                DayRecordDialog newFragment = DayRecordDialog_.newInstance(currentHabitId, selectedDate);
                newFragment.show(transaction, "dayRecordActivity");
            }
        });
    }

    private void initializeTabs() {
        tabHost.setup();

        //Calendar Tab
        TabHost.TabSpec spec = tabHost.newTabSpec("Calendar");
        spec.setContent(R.id.calendarTab);
        spec.setIndicator("Calendar");
        tabHost.addTab(spec);

        //Statistics Tab
        spec = tabHost.newTabSpec("Stats");
        spec.setContent(R.id.statisticsTab);
        spec.setIndicator("Statistics");
        tabHost.addTab(spec);
    }

    private Date getDate(int year, int month, int dayOfMonth) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DATE, dayOfMonth);
        return cal.getTime();
    }
}
