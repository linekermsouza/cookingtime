package com.udacity.lineker.cookingtime;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.udacity.lineker.cookingtime.home.HomeActivity;
import com.udacity.lineker.cookingtime.steps.StepsActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.IsNot.not;


@RunWith(AndroidJUnit4.class)
public class HomeActivityScreenTest {

    @Rule
    public ActivityTestRule<HomeActivity> mActivityTestRule = new ActivityTestRule<>(HomeActivity.class);

    @Test
    public void clickGridViewItem_OpensStepsActivity() throws InterruptedException {
        Intents.init();
        intending(not(isInternal())).respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, null));
        // testing initial state
        onView(withId(R.id.infoText)).check(matches(withText("Carregando receitas")));
        // loading receipts
        Thread.sleep(5000);
        // click on receipt
        onView(withId(R.id.recyclerview)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, UtilForTesting.clickChildViewWithId(R.id.ll_container)));
        // check if receipt detail is open
        intended(hasComponent(StepsActivity.class.getName()));
        Intents.release();

    }

}
