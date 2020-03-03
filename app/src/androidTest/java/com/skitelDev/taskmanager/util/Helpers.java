package com.skitelDev.taskmanager.util;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.skitelDev.taskmanager.R;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

public class Helpers {

    // Generates random string with default length
    public static String randomStringGenerator() {
        int length = 5;
        return randomStringGenerator(5);
    }

    // Generates random string with indicated length (only ENG locale, lower register)
    public static String randomStringGenerator(int length) {
        String result = "";
        for (int i = 0; i < length; i++) {
            result += (char) (Math.random() * 26 + 97);
        }
        return result;
    }

    // Get length of recycler view
    public static int getLengthRecyclerView() {
        final int[] numberOfItems = new int[1];
        onView(withId(R.id.recycler_view))
                .check(matches(new TypeSafeMatcher<View>() {
                    @Override
                    protected boolean matchesSafely(View item) {
                        RecyclerView listView = (RecyclerView) item;
                        numberOfItems[0] = listView.getAdapter().getItemCount();
                        return true;
                    }

                    @Override
                    public void describeTo(Description description) {
                    }
                }));
        return numberOfItems[0];
    }

    public static int getLengthSubTask() {
        final int[] numberOfItems = new int[1];
        onView(withId(R.id.subTask))
                .check(matches(new TypeSafeMatcher<View>() {
                    @Override
                    protected boolean matchesSafely(View item) {
                        RecyclerView listView = (RecyclerView) item;
                        numberOfItems[0] = listView.getAdapter().getItemCount();
                        return true;
                    }

                    @Override
                    public void describeTo(Description description) {
                    }
                }));
        return numberOfItems[0];
    }

    // Waiter
    public static void sleep(long millisec) {
        try {
            Thread.sleep(millisec);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
