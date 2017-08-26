package com.github.dave62.habits.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.github.dave62.habits.R;
import com.github.dave62.habits.adapter.HabitAdapter;
import com.github.dave62.habits.dialog.CreateHabitDialog_;
import com.github.dave62.habits.model.Habit;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

@EActivity(R.layout.activity_habits_list)
public class HabitsListActivity extends AppCompatActivity {

    @ViewById(R.id.toolbar)
    protected Toolbar toolbar;
    @ViewById(R.id.fab)
    protected FloatingActionButton fab;
    @ViewById(R.id.recyclerView)
    protected RecyclerView recyclerView;

    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(toolbar);
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
        recyclerView.setAdapter(new HabitAdapter(habits, true, true));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_habits_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Click(R.id.fab)
    void onFabClick() {
        CreateHabitDialog_ createHabitDialog = new CreateHabitDialog_();
        createHabitDialog.show(getFragmentManager(), "createHabitDialog");
    }
}
