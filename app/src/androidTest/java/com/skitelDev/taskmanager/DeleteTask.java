package com.skitelDev.taskmanager;

import androidx.test.rule.ActivityTestRule;

import com.skitelDev.taskmanager.activities.MainActivity;
import com.skitelDev.taskmanager.util.Helpers;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.swipeRight;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

public class DeleteTask {

    private String taskText = Helpers.randomStringGenerator();

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    @Before
    public void init() {
        onView(withId(R.id.addbutton))
                .perform(click());

        onView(withId(R.id.newTaskTextField))
                .perform(replaceText(taskText));
        onView(withId(R.id.savebutton))
                .perform(click());
    }

    @Test
    public void deleteTaskWithSwipe() {
        onView(withId(R.id.recycler_view))
                .perform(actionOnItemAtPosition(Helpers.getLengthRecyclerView() - 1, swipeRight()));
    }

    @Test
    public void deleteTaskInsideTask() {
        onView(withId(R.id.recycler_view))
                .perform(actionOnItemAtPosition(Helpers.getLengthRecyclerView() - 1, click()));

        onView(withId(R.id.deleteTask))
                .perform(click());
    }
}
