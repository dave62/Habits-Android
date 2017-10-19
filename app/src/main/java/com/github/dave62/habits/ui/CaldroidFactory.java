package com.github.dave62.habits.ui;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;

import com.github.dave62.habits.R;
import com.github.dave62.habits.dialog.DayRecordDialog;
import com.github.dave62.habits.model.Habit;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.util.Calendar;
import java.util.Date;

public class CaldroidFactory {

    public static CaldroidFragment getCaldroidFragment(final Activity parentActivity, final Habit currentHabit) {
        CaldroidFragment caldroidFragment = new CaldroidFragment();
        Bundle args = new Bundle();
        Calendar cal = Calendar.getInstance();
        args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
        args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
        args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);
        args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, true);
        args.putInt(CaldroidFragment.START_DAY_OF_WEEK, CaldroidFragment.MONDAY);
        caldroidFragment.setArguments(args);
        args.putInt(CaldroidFragment.THEME_RESOURCE, R.style.CaldroidHabits);

        caldroidFragment.setMinDate(currentHabit.getStartingDate());
        caldroidFragment.setMaxDate(new Date());

        createAndAddClickListener(parentActivity, currentHabit, caldroidFragment);

        return caldroidFragment;
    }

    private static void createAndAddClickListener(final Activity parentActivity, final Habit currentHabit, CaldroidFragment caldroidFragment) {
        final CaldroidListener listener = new CaldroidListener() {
            @Override
            public void onSelectDate(Date selectedDate, View view) {
                //TODO : same code as in the habit adapter, find a way to factorize ?
                FragmentManager manager = parentActivity.getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                Fragment previousInstance = manager.findFragmentByTag("dayRecordActivity");
                if (previousInstance != null) {
                    transaction.remove(previousInstance);
                }
                transaction.addToBackStack(null);
                DayRecordDialog newFragment = DayRecordDialog.newInstance(currentHabit.getId(), selectedDate);
                newFragment.show(transaction, "dayRecordActivity");

            }
        };
        caldroidFragment.setCaldroidListener(listener);
    }


}
