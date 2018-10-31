package com.udacity.lineker.cookingtime;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.udacity.lineker.cookingtime.model.Receipt;
import com.udacity.lineker.cookingtime.step.StepActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.core.IsNot.not;


@RunWith(AndroidJUnit4.class)
public class StepActivityScreenTest {

    @Rule
    public ActivityTestRule<StepActivity> mActivityTestRule = new ActivityTestRule<StepActivity>(StepActivity.class){
        @Override
        protected Intent getActivityIntent() {
            Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
            Intent result = new Intent(targetContext, StepActivity.class);

            result.putExtra(StepActivity.ARG_RECEIPT, UtilForTesting.getMockedReceipt());
            result.putExtra(StepActivity.ARG_STEP_POSITION, 0);
            result.putExtra(StepActivity.ARG_INSTRUMENTED_TEST, true);
            return result;
        }
    };

    @Test
    public void clickGridViewItem_OpensStepActivity() throws InterruptedException {
        mActivityTestRule.getActivity()
                .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Thread.sleep(5000);
        Receipt receipt = UtilForTesting.getMockedReceipt();
        int stepPosition = 0;

        // testing initial state
        onView(withId(R.id.description)).check(matches(withText(receipt.getSteps().get(stepPosition).getDescription())));
        onView(withId(R.id.previous)).check(matches(not(isEnabled())));
        onView(withId(R.id.next)).check(matches(isEnabled()));
        onView(allOf(withId(R.id.playerView), not(isDisplayed())));
        // click on next
        onView(withId(R.id.next)).perform(click());
        stepPosition = stepPosition + 1;
        onView(withId(R.id.description)).check(matches(withText(receipt.getSteps().get(stepPosition).getDescription())));
        onView(withId(R.id.previous)).check(matches(isEnabled()));
        onView(withId(R.id.next)).check(matches(isEnabled()));
        onView(allOf(withId(R.id.playerView), not(isDisplayed())));
        // click on previous
        onView(withId(R.id.previous)).perform(click());
        stepPosition = stepPosition - 1;
        onView(withId(R.id.description)).check(matches(withText(receipt.getSteps().get(stepPosition).getDescription())));
        onView(withId(R.id.previous)).check(matches(not(isEnabled())));
        onView(withId(R.id.next)).check(matches(isEnabled()));
        onView(allOf(withId(R.id.playerView), not(isDisplayed())));
        // go to last
        onView(withId(R.id.next)).perform(click());
        onView(withId(R.id.next)).perform(click());
        stepPosition = stepPosition + 2;
        onView(withId(R.id.description)).check(matches(withText(receipt.getSteps().get(stepPosition).getDescription())));
        onView(withId(R.id.previous)).check(matches(isEnabled()));
        onView(withId(R.id.next)).check(matches(not(isEnabled())));
        onView(allOf(withId(R.id.playerView), isDisplayed()));
    }

}
