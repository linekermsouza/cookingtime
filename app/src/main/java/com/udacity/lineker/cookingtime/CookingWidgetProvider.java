package com.udacity.lineker.cookingtime;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.udacity.lineker.cookingtime.database.IngredientEntry;
import com.udacity.lineker.cookingtime.database.ReceiptEntry;
import com.udacity.lineker.cookingtime.home.HomeActivity;
import com.udacity.lineker.cookingtime.widget.CookingTimeService;
import com.udacity.lineker.cookingtime.widget.IngredientViewsFactory;
import com.udacity.lineker.cookingtime.widget.WidgetService;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of App Widget functionality.
 */
public class CookingWidgetProvider extends AppWidgetProvider {

    public static String EXTRA_WORD=
            "com.udacity.lineker.cookingtime.WORD";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                ReceiptEntry receiptEntry, List<IngredientEntry> ingredients, int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.cooking_widget_provider);

        int randomNumber=(int)(Math.random()*1000);
        Intent svcIntent=new Intent(context, WidgetService.class);
        svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId+randomNumber);
        svcIntent.putExtra(IngredientViewsFactory.EXTRA_ITEMS, getItems(ingredients));
        svcIntent.putExtra(IngredientViewsFactory.EXTRA_APPWIDGET_ID_COOKING, appWidgetId);
        svcIntent.setData(Uri.parse(svcIntent.toUri(Intent.URI_INTENT_SCHEME)));

        views.setRemoteAdapter(appWidgetId, R.id.words, svcIntent);

        Intent i = new Intent(context, HomeActivity.class);
        PendingIntent pi = PendingIntent.getActivity(context,0, i,0);
        if (receiptEntry == null) {
            views.setTextViewText(R.id.select_receipt, context.getString(R.string.change_receipt));
        } else {
            views.setTextViewText(R.id.select_receipt, receiptEntry.getName());
        }
        views.setOnClickPendingIntent(R.id.select_receipt,pi);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    private static ArrayList<String> getItems(List<IngredientEntry> ingredients) {
        ArrayList<String> items = new ArrayList<String>();
        for (IngredientEntry ingredientEntry : ingredients) {
            String item = String.format("%s (%s %s)",
                    ingredientEntry.getIngredient(), ingredientEntry.getQuantity(), ingredientEntry.getMeasure());
            items.add(item);
        }
        return items;
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        CookingTimeService.startActionUpdateCookingTimeWidgets(context);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    public static void updateCookingTimeWidgets(Context context, AppWidgetManager appWidgetManager, ReceiptEntry receiptEntry, List<IngredientEntry> ingredients, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, receiptEntry, ingredients, appWidgetId);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.words);
        }
    }
}

