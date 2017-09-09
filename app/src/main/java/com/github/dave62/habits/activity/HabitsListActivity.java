package com.github.dave62.habits.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.TextView;

import com.github.dave62.habits.R;
import com.github.dave62.habits.adapter.HabitAdapter;
import com.github.dave62.habits.dialog.CreateHabitDialog_;
import com.github.dave62.habits.model.Habit;
import com.github.dave62.habits.ui.EmptyRecyclerView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

@EActivity(R.layout.activity_habits_list)
public class HabitsListActivity extends AppCompatActivity {

    @ViewById(R.id.recyclerView)
    protected EmptyRecyclerView recyclerView;
    @ViewById(R.id.fab)
    protected FloatingActionButton fab;
    @ViewById(R.id.emptyListLabel)
    protected TextView emptyListLabel;


    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance();
    }

    @AfterViews
    protected void afterViews() {
        initializeHabitsList();
    }

    private void initializeHabitsList() {
        RealmResults<Habit> habits = realm.where(Habit.class).findAllSorted("startingDate", Sort.DESCENDING);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new HabitAdapter(this, habits, true, true));
        recyclerView.setEmptyView(emptyListLabel);
    }

    @Click(R.id.fab)
    void onFabClick() {
        CreateHabitDialog_ createHabitDialog = new CreateHabitDialog_();
        createHabitDialog.show(getFragmentManager(), "createHabitDialog");
    }
}
