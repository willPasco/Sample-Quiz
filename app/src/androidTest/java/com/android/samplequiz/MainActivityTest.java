package com.android.samplequiz;

import android.support.test.runner.AndroidJUnit4;

import org.junit.runner.RunWith;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    private IdlingResource idlingResource;

    @Rule
    public IntentsTestRule<MainActivity_> activityIntentsTestRule = new IntentsTestRule<>(MainActivity_.class);

    @Before
    public void registerIdleResource() {
        idlingResource = activityIntentsTestRule.getActivity().getIdlingResource();
        IdlingRegistry.getInstance().register(idlingResource);
    }

    @After
    public void unregisterIdlingResource() {
        if (idlingResource != null) {
            IdlingRegistry.getInstance().unregister(idlingResource);
        }
    }

}