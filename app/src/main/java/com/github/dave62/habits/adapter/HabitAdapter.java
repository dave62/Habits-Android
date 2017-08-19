package com.github.dave62.habits.adapter;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.dave62.habits.R;
import com.github.dave62.habits.activity.DayRecordActivity_;
import com.github.dave62.habits.constants.Constants;
import com.github.dave62.habits.dialog.DeleteHabitDialog_;
import com.github.dave62.habits.model.Habit;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

public class HabitAdapter extends RealmRecyclerViewAdapter<Habit, HabitAdapter.ViewHolder> {

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView startingDate;

        public ViewHolder(View v) {
            super(v);
            name = (TextView) v.findViewById(R.id.habitName);
            startingDate = (TextView) v.findViewById(R.id.habitStartingDate);
        }
    }

    public HabitAdapter(@Nullable OrderedRealmCollection<Habit> data, boolean autoUpdate, boolean updateOnModification) {
        super(data, autoUpdate, updateOnModification);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_habits_list, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        final Habit habit = getData().get(position);
        viewHolder.name.setText(habit.getName());
        viewHolder.startingDate.setText("Started : " + Constants.DATE_FORMAT.format(habit.getStartingDate()));
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DayRecordActivity_.intent(v.getContext()).currentHabitId(habit.getId()).start();
            }
        });
        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                deletePreviousInstanceAndShowDialog(v.getContext(), habit.getId());
                return true;
            }
        });
    }

    private void deletePreviousInstanceAndShowDialog(Context context, String habitId) {
        //TODO : find a more elegant way to get the fragment manager ?
        FragmentManager manager = ((Activity) context).getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        Fragment previousInstance = manager.findFragmentByTag("dialog");
        if (previousInstance != null) {
            transaction.remove(previousInstance);
        }
        transaction.addToBackStack(null);
        DialogFragment newFragment = DeleteHabitDialog_.newInstance(habitId);
        newFragment.show(transaction, "dialog");
    }
}
