package com.skitelDev.taskmanager;

import androidx.test.rule.ActivityTestRule;

import com.skitelDev.taskmanager.activities.MainActivity;
import com.skitelDev.taskmanager.util.Helpers;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

public class CreateTask {

    private String taskText = Helpers.randomStringGenerator();
    private String descriptionText = Helpers.randomStringGenerator();

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    @After
    public void tearDown() {
        onView(withId(R.id.deleteTask))
                .perform(click());
    }

    @Test
    public void createNewTask() {
        onView(withId(R.id.addbutton))
                .perform(click());

        onView(withId(R.id.newTaskTextField))
                .perform(replaceText(taskText));
        onView(withId(R.id.savebutton))
                .perform(click());

        onView(withId(R.id.recycler_view))
                .perform(actionOnItemAtPosition(Helpers.getLengthRecyclerView() - 1, click()));

        onView(withId(R.id.taskField))
                .check(matches(withText(taskText)));
    }

    @Test
    public void createNewTaskWithDescription() {
        onView(withId(R.id.addbutton))
                .perform(click());

        onView(withId(R.id.newTaskTextField))
                .perform(replaceText(taskText));
        onView(withId(R.id.addDescription))
                .perform(click());
        onView(withId(R.id.desc))
                .perform(replaceText(descriptionText));
        onView(withId(R.id.savebutton))
                .perform(click());

        onView(withId(R.id.recycler_view))
                .perform(actionOnItemAtPosition(Helpers.getLengthRecyclerView() - 1, click()));

        onView(withId(R.id.taskField))
                .check(matches(withText(taskText)));
        onView(withId(R.id.taskDescription))
                .check(matches(withText(descriptionText)));
    }
}
