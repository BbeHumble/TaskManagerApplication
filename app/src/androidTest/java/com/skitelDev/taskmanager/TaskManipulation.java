package com.skitelDev.taskmanager;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.rule.ActivityTestRule;

import com.skitelDev.taskmanager.activities.MainActivity;
import com.skitelDev.taskmanager.util.Helpers;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.AllOf.allOf;

public class TaskManipulation {

    private String taskText = Helpers.randomStringGenerator();
    private String descriptionText = Helpers.randomStringGenerator();

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

        onView(withId(R.id.recycler_view))
                .perform(actionOnItemAtPosition(Helpers.getLengthRecyclerView() - 1, click()));
    }

    @After
    public void tearDown() {
        onView(withId(R.id.deleteTask))
                .perform(click());
    }

    @Test
    public void changeDescription() {
        onView(withId(R.id.taskDescription))
                .perform(replaceText(descriptionText));
        onView(withId(R.id.exit))
                .perform(click());
        onView(withId(R.id.recycler_view))
                .check(matches(withText(descriptionText)));
//        onView(withId(R.id.))
    }

    @Test
    public void addSubTask() {
        String subTaskText = Helpers.randomStringGenerator();

        onView(withId(R.id.addSubTask))
                .perform(click());
        onView(withId(R.id.sub_task_text))
                .perform(replaceText(subTaskText));

        onView(withId(R.id.exit))
                .perform(click());

        onView(withId(R.id.recycler_view))
                .perform(actionOnItemAtPosition(Helpers.getLengthRecyclerView() - 1, click()));

        onView(withText(subTaskText))
                .check(matches(isDisplayed()));
    }
}
