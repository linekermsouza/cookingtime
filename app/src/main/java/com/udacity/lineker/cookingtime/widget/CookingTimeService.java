package com.udacity.lineker.cookingtime.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.udacity.lineker.cookingtime.CookingWidgetProvider;
import com.udacity.lineker.cookingtime.database.AppDatabase;
import com.udacity.lineker.cookingtime.database.IngredientEntry;
import com.udacity.lineker.cookingtime.database.ReceiptEntry;

import java.util.List;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 */
public class CookingTimeService extends IntentService {

    public static final String ACTION_UPDATE_COOKING_TIME_WIDGETS = "com.udacity.lineker.cookingtime.action.update_cooking_time_widgets";

    public CookingTimeService() {
        super("CookingTimeService");
    }

    public static void startActionUpdateCookingTimeWidgets(Context context) {
        Intent intent = new Intent(context, CookingTimeService.class);
        intent.setAction(ACTION_UPDATE_COOKING_TIME_WIDGETS);
        context.startService(intent);
    }

    /**
     * @param intent
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_UPDATE_COOKING_TIME_WIDGETS.equals(action)) {
                handleActionUpdateCookingTimeWidgets();
            }
        }
    }


    private void handleActionUpdateCookingTimeWidgets() {
        AppDatabase database = AppDatabase.getInstance(this.getApplicationContext());
        List<ReceiptEntry> receiptEntries = database.receiptDao().loadAllSync();
        ReceiptEntry receiptEntry = null;
        List<IngredientEntry> ingredients = null;
        if (receiptEntries != null && receiptEntries.size() > 0) {
            receiptEntry = receiptEntries.get(0);
            ingredients = database.ingredientDao().findIngredientsForReceipt(receiptEntry.getId());
        }
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(CookingTimeService.this.getApplicationContext());
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(CookingTimeService.this, CookingWidgetProvider.class));
        //Now update all widgets
        CookingWidgetProvider.updateCookingTimeWidgets(CookingTimeService.this, appWidgetManager, receiptEntry, ingredients, appWidgetIds);
    }
}
