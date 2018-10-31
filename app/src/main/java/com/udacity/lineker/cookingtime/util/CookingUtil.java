package com.udacity.lineker.cookingtime.util;

import android.content.Context;

import com.udacity.lineker.cookingtime.R;
import com.udacity.lineker.cookingtime.model.Ingredient;
import com.udacity.lineker.cookingtime.model.Step;

import java.util.List;

public class CookingUtil {
    public static String getFormattedIngredients(Context context, List<Ingredient> ingredientList) {
        String ingredients = context.getString(R.string.ingredients_list) + ":\n";
        for (Ingredient ingredient : ingredientList) {
            ingredients += String.format("- %s (%s %s)\n",
                    ingredient.getIngredient(), ingredient.getQuantity(), ingredient.getMeasure());
        }
        return ingredients;
    }

    public static String getFormattedStep(Step step) {
        String description = String.format("%s. %s",
                step.getId(), step.getShortDescription());
        return description;
    }
}
