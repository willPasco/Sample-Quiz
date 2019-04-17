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

    @Test
    public void clickStartQuizButton_WithUserName() {
        onView(withId(R.id.edit_text_user_name)).perform(typeText("User test"));
        closeSoftKeyboard();
        onView(withId(R.id.button_start)).perform(click());
        onView(withId(R.id.question_radio_group)).check(matches(isDisplayed()));
    }


    @Test
    public void clickStartQuizButton_WithoutUserName() {
        onView(withId(R.id.button_start)).perform(click());
        onView(withText(R.string.user_empty)).inRoot(withDecorView(not(is(activityIntentsTestRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
    }