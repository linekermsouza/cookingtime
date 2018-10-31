package com.udacity.lineker.cookingtime;

import android.support.annotation.IdRes;
import android.support.test.espresso.PerformException;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.espresso.util.HumanReadables;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.udacity.lineker.cookingtime.model.Ingredient;
import com.udacity.lineker.cookingtime.model.Receipt;
import com.udacity.lineker.cookingtime.model.Step;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

import java.util.ArrayList;
import java.util.List;

public class UtilForTesting {
    public static ViewAction clickChildViewWithId(final int id) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return null;
            }

            @Override
            public String getDescription() {
                return "Click on a child view with specified id.";
            }

            @Override
            public void perform(UiController uiController, View view) {
                View v = view.findViewById(id);
                v.performClick();
            }
        };
    }

    public static Receipt getMockedReceipt() {
        Receipt receipt = new Receipt();
        receipt.setId(1);
        receipt.setImage("");
        receipt.setName("Nutella");
        receipt.setServings(1);

        List<Step> steps = new ArrayList<>();
        steps.add(getMockedStep(1, ""));
        steps.add(getMockedStep(2, ""));
        steps.add(getMockedStep(3, "https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffd974_-intro-creampie/-intro-creampie.mp4"));
        receipt.setSteps(steps);

        List<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(getMockedIngredient());
        receipt.setIngredients(ingredients);

        return receipt;
    }

    public static <VH extends RecyclerView.ViewHolder> ViewAction actionOnItemViewAtPosition(int position,
                                                                                             @IdRes
                                                                                                     int viewId,
                                                                                             ViewAction viewAction) {
        return new ActionOnItemViewAtPositionViewAction(position, viewId, viewAction);
    }

    private static final class ActionOnItemViewAtPositionViewAction<VH extends RecyclerView
            .ViewHolder>
            implements

            ViewAction {
        private final int position;
        private final ViewAction viewAction;
        private final int viewId;

        private ActionOnItemViewAtPositionViewAction(int position,
                                                     @IdRes int viewId,
                                                     ViewAction viewAction) {
            this.position = position;
            this.viewAction = viewAction;
            this.viewId = viewId;
        }

        public Matcher<View> getConstraints() {
            return Matchers.allOf(new Matcher[] {
                    ViewMatchers.isAssignableFrom(RecyclerView.class), ViewMatchers.isDisplayed()
            });
        }

        public String getDescription() {
            return "actionOnItemAtPosition performing ViewAction: "
                    + this.viewAction.getDescription()
                    + " on item at position: "
                    + this.position;
        }

        public void perform(UiController uiController, View view) {
            RecyclerView recyclerView = (RecyclerView) view;
            (new ScrollToPositionViewAction(this.position)).perform(uiController, view);
            uiController.loopMainThreadUntilIdle();

            View targetView = recyclerView.getChildAt(this.position).findViewById(this.viewId);

            if (targetView == null) {
                throw (new PerformException.Builder()).withActionDescription(this.toString())
                        .withViewDescription(

                                HumanReadables.describe(view))
                        .withCause(new IllegalStateException(
                                "No view with id "
                                        + this.viewId
                                        + " found at position: "
                                        + this.position))
                        .build();
            } else {
                this.viewAction.perform(uiController, targetView);
            }
        }
    }

    private static final class ScrollToPositionViewAction implements ViewAction {
        private final int position;

        private ScrollToPositionViewAction(int position) {
            this.position = position;
        }

        public Matcher<View> getConstraints() {
            return Matchers.allOf(new Matcher[] {
                    ViewMatchers.isAssignableFrom(RecyclerView.class), ViewMatchers.isDisplayed()
            });
        }

        public String getDescription() {
            return "scroll RecyclerView to position: " + this.position;
        }

        public void perform(UiController uiController, View view) {
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.scrollToPosition(this.position);
        }
    }


    public static RecyclerViewMatcher withRecyclerView(final int recyclerViewId) {

        return new RecyclerViewMatcher(recyclerViewId);
    }

    public static Step getMockedStep(int id, String viderURL) {
        Step step = new Step();
        step.setDescription("step x que faz mais alguma coisa");
        step.setId(id);
        step.setShortDescription("step x");
        step.setThumbnailURL("");
        step.setVideoURL(viderURL);

        return step;
    }

    public static Ingredient getMockedIngredient() {
        Ingredient ingredient = new Ingredient();
        ingredient.setIngredient("chocolate");
        ingredient.setMeasure("kg");
        ingredient.setQuantity(1);

        return ingredient;
    }
}
