package com.udacity.lineker.cookingtime.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.udacity.lineker.cookingtime.CookingWidgetProvider;
import com.udacity.lineker.cookingtime.R;

import java.util.ArrayList;

public class IngredientViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    public static final String EXTRA_ITEMS = "EXTRA_ITEMS";
    public static final String EXTRA_APPWIDGET_ID_COOKING = "EXTRA_APPWIDGET_ID_COOKING";
    private final ArrayList<String> items;

    private Context ctxt=null;
    private int appWidgetId;

    public IngredientViewsFactory(Context ctxt, Intent intent) {
        this.ctxt=ctxt;
        appWidgetId=intent.getIntExtra(EXTRA_APPWIDGET_ID_COOKING,
                AppWidgetManager.INVALID_APPWIDGET_ID);
        items = intent.getStringArrayListExtra(EXTRA_ITEMS);
    }

    @Override
    public void onCreate() {
        // no-op
    }

    @Override
    public void onDestroy() {
        // no-op
    }

    @Override
    public int getCount() {
        return(items.size());
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews row=new RemoteViews(ctxt.getPackageName(),
                R.layout.widget_row);

        row.setTextViewText(android.R.id.text1, items.get(position));

        Intent i=new Intent();
        Bundle extras=new Bundle();

        extras.putString(CookingWidgetProvider.EXTRA_WORD, items.get(position));
        i.putExtras(extras);
        row.setOnClickFillInIntent(android.R.id.text1, i);

        return(row);
    }

    @Override
    public RemoteViews getLoadingView() {
        return(null);
    }

    @Override
    public int getViewTypeCount() {
        return(1);
    }

    @Override
    public long getItemId(int position) {
        return(items.get(position).hashCode());
    }

    @Override
    public boolean hasStableIds() {
        return(true);
    }

    @Override
    public void onDataSetChanged() {
        // no-op
    }
}
