package com.skitelDev.taskmanager;

import androidx.test.rule.ActivityTestRule;

import com.skitelDev.taskmanager.activities.MainActivity;
import com.skitelDev.taskmanager.util.Helpers;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.swipeRight;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class FirstTests {

    private String taskText = Helpers.randomStringGenerator();

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    // Check open of the application
    @Test
    public void appOpen() {
        onView(withId(R.id.textView2))
                .check(matches(withText(R.string.tasks)));
    }


}
