package com.udacity.gradle.builditbigger;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;


import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class AsyncTaskMainActivityFragmentTest {
    private IdlingResource mIdlingResource;
    private static final Intent MY_ACTIVITY_INTENT = new Intent(InstrumentationRegistry.getTargetContext(), MainActivity.class);


    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule = new
            ActivityTestRule<>(MainActivity.class);

    @Before
    public void registerIdlingResource() {
        ///launch activity
        mainActivityActivityTestRule.launchActivity(MY_ACTIVITY_INTENT);
        mIdlingResource = mainActivityActivityTestRule.getActivity().getIdlingResource();
        // To prove that the test fails, omit this call:
        Espresso.registerIdlingResources(mIdlingResource);
    }
    @Test
    public void recipesRecyclerClickTest() {
        onView(withId(R.id.btn_jokes)).perform(click());

        onView(withId(R.id.tv_joke)).check(matches(not(withText(""))));
    }
    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            Espresso.unregisterIdlingResources(mIdlingResource);
        }
    }
}
