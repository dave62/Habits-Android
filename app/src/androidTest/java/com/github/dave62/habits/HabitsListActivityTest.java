package com.github.dave62.habits;

import android.support.test.espresso.contrib.PickerActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.DatePicker;

import com.github.dave62.habits.activity.HabitsListActivity_;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class HabitsListActivityTest {

    public static final String ESPRESSO_HABIT_NAME = "EspressoHabit";


    @Rule
    public ActivityTestRule<HabitsListActivity_> mActivityRule = new ActivityTestRule(HabitsListActivity_.class);

    @Test
    public void createAndDeleteHabit() {
        createHabit();
        deleteHabit();
    }

    private void createHabit() {
        //Click on Floating Action Button
        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.nameLabel)).check(matches(isDisplayed()));

        //Fill the form
        onView(withId(R.id.nameInput)).perform(typeText(ESPRESSO_HABIT_NAME));
        onView(withId(R.id.startDateInput)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2017, 1, 1));
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.startDateInput)).check(matches(withText("Jan 1, 2017")));
        onView(withId(R.id.timeThresholdInput)).perform(typeText("15"));
        onView(withId(android.R.id.button1)).perform(click());

        //The new habit is now in the list
        onView(withId(R.id.recyclerView)).check(matches(hasDescendant(withText(ESPRESSO_HABIT_NAME))));
    }

    private void deleteHabit() {
        onView(withId(R.id.recyclerView)).perform(RecyclerViewActions.actionOnItem(hasDescendant(withText(ESPRESSO_HABIT_NAME)), longClick()));
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.recyclerView)).check(matches(not(hasDescendant(withText(ESPRESSO_HABIT_NAME)))));
    }
}
