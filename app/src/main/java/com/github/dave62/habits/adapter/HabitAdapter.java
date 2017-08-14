package com.github.dave62.habits.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.dave62.habits.model.Habit;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

public class HabitAdapter extends RealmRecyclerViewAdapter<Habit, HabitAdapter.ViewHolder> {

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView value;

        public ViewHolder(View v) {
            super(v);
            value = (TextView) v.findViewById(android.R.id.text1);
        }
    }

    public HabitAdapter(@Nullable OrderedRealmCollection<Habit> data, boolean autoUpdate, boolean updateOnModification) {
        super(data, autoUpdate, updateOnModification);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder view, int position) {
        Habit habit = getData().get(position);
        view.value.setText(habit.getName());
    }
}
