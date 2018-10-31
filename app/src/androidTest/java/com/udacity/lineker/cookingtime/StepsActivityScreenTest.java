package com.udacity.lineker.cookingtime;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.udacity.lineker.cookingtime.model.Receipt;
import com.udacity.lineker.cookingtime.step.StepActivity;
import com.udacity.lineker.cookingtime.steps.StepsActivity;
import com.udacity.lineker.cookingtime.util.CookingUtil;

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
import static com.udacity.lineker.cookingtime.UtilForTesting.withRecyclerView;
import static org.hamcrest.core.IsNot.not;


@RunWith(AndroidJUnit4.class)
public class StepsActivityScreenTest {

    @Rule
    public ActivityTestRule<StepsActivity> mActivityTestRule = new ActivityTestRule<StepsActivity>(StepsActivity.class){
        @Override
        protected Intent getActivityIntent() {
            Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
            Intent result = new Intent(targetContext, StepsActivity.class);

            result.putExtra(StepsActivity.ARG_RECEIPT, UtilForTesting.getMockedReceipt());
            return result;
        }
    };

    @Test
    public void clickGridViewItem_OpensStepsActivity() {
        mActivityTestRule.getActivity()
                .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Intents.init();
        intending(not(isInternal())).respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, null));
        // click on step
        onView(withId(R.id.recyclerview)).perform(
                RecyclerViewActions.actionOnItemAtPosition(1, UtilForTesting.clickChildViewWithId(R.id.item_container)));
        // check if receipt detail is open
        intended(hasComponent(StepActivity.class.getName()));
        Intents.release();

    }

    @Test
    public void clickGridViewItem_showListOfIngredients() {
        Receipt receipt = UtilForTesting.getMockedReceipt();
        String ingredients = CookingUtil.getFormattedIngredients(InstrumentationRegistry.getTargetContext(), receipt.getIngredients());
        // testing initial state
        onView(withRecyclerView(R.id.recyclerview)
                .atPositionOnView(0, R.id.title))
                .check(matches(withText(ingredients)));

    }

    @Test
    public void clickGridViewItem_showFirstStep() {
        Receipt receipt = UtilForTesting.getMockedReceipt();
        String step = CookingUtil.getFormattedStep(receipt.getSteps().get(0));
        // testing initial state
        onView(withRecyclerView(R.id.recyclerview)
                .atPositionOnView(1, R.id.title))
                .check(matches(withText(step)));

    }

}
