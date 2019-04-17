package com.android.samplequiz;

import android.content.ComponentName;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.RadioButton;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

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

    @Test
    public void clickAnswerQuestion_WithoutSelectOption() {
        onView(withId(R.id.edit_text_user_name)).perform(typeText("User test"));
        closeSoftKeyboard();
        onView(withId(R.id.button_start)).perform(click());
        onView(withId(R.id.question_radio_group)).check(matches(isDisplayed()));

        onView(withId(R.id.button_action)).perform(click());

        onView(withText(R.string.radio_not_selected)).inRoot(withDecorView(not(is(activityIntentsTestRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
    }

    @Test
    public void clickAnswerQuestion_WithSelectedOption() {
        onView(withId(R.id.edit_text_user_name)).perform(typeText("User test"));
        closeSoftKeyboard();
        onView(withId(R.id.button_start)).perform(click());
        onView(withId(R.id.question_radio_group)).check(matches(isDisplayed()));

        RadioButton view = (RadioButton) activityIntentsTestRule.getActivity().questionRadioGroup.getChildAt(2);
        view.setChecked(true);

        onView(withId(R.id.button_action)).perform(click());

        onView(withId(view.getId())).check(matches(not(isEnabled())));
    }

    @Test
    public void clickNextQuestion() {
        onView(withId(R.id.edit_text_user_name)).perform(typeText("User test"));
        closeSoftKeyboard();
        onView(withId(R.id.button_start)).perform(click());
        onView(withId(R.id.question_radio_group)).check(matches(isDisplayed()));

        RadioButton view = (RadioButton) activityIntentsTestRule.getActivity().questionRadioGroup.getChildAt(2);
        view.setChecked(true);

        onView(withId(R.id.button_action)).perform(click());
        onView(withId(view.getId())).check(matches(not(isEnabled())));

        onView(withId(R.id.button_action)).perform(click());

        view = (RadioButton) activityIntentsTestRule.getActivity().questionRadioGroup.getChildAt(2);
        onView(withId(view.getId())).check(matches(isEnabled()));
    }

    @Test
    public void happyFlow(){

        onView(withId(R.id.edit_text_user_name)).perform(typeText("User test"));
        closeSoftKeyboard();
        onView(withId(R.id.button_start)).perform(click());

        for(int i=0; i<=9;i++) {
            RadioButton view = (RadioButton) activityIntentsTestRule.getActivity().questionRadioGroup.getChildAt(2);
            view.setChecked(true);
            onView(withId(R.id.button_action)).perform(click());
            onView(withId(R.id.button_action)).perform(click());
        }

        intended(hasComponent(new ComponentName(getTargetContext(), ResultActivity_.class)));
    }
}
