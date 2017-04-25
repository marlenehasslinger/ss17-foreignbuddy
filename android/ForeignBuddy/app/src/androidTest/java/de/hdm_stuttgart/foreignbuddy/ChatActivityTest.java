package de.hdm_stuttgart.foreignbuddy;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.hdm_stuttgart.foreignbuddy.Activities.ChatActivity;
import de.hdm_stuttgart.foreignbuddy.R;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.*;

/**
 * Created by Jan-Niklas on 25.04.17.
 */

@RunWith(AndroidJUnit4.class)
public class ChatActivityTest {



    @Rule
    public final ActivityTestRule<ChatActivity> main = new ActivityTestRule<>(ChatActivity.class);

    @Test
    public void sendMessageTest(){
        onView(withId(R.id.EnterText)).perform(typeText("Hello test 123"));
        onView(withId(R.id.SendButton)).perform(click());

        onData(hasToString(startsWith("Hello"))).check(matches(isDisplayed()));
    }

}

