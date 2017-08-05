package com.savior.notes.bakingapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.savior.notes.bakingapp.model.Baking;
import com.savior.notes.bakingapp.recycler.BakeAdapter;
import com.savior.notes.bakingapp.recycler.Constants;
import com.savior.notes.bakingapp.util.NetworkUtil;
import com.savior.notes.bakingapp.util.NoConnectivityException;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Implementation of App Widget functionality.
 */
public class BakeWidgetProvider extends AppWidgetProvider  {

    private static final String TAG = BakeWidgetProvider.class.getSimpleName();

    static void updateAppWidget(final Context context, final AppWidgetManager appWidgetManager, final int appWidgetId, ArrayList<String> ingred) {
        RemoteViews views;
        views = getMultipleRemoteView(context,ingred);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }


    public static RemoteViews getMultipleRemoteView(Context context, ArrayList<String> ingred){
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.bake_grid_widget_provider);
        Intent intent = new Intent(context, BakeGridWidgetService.class);
        intent.putStringArrayListExtra(Constants.INGRED_INFO, ingred);
        views.setRemoteAdapter(R.id.widget_grid_view, intent);
        Intent appIntent = new Intent(context, IngredientsActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,appIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.widget_grid_view, pendingIntent);
        views.setEmptyView(R.id.widget_grid_view, R.id.empty_view);
        return views;
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager,
                                          int appWidgetId,  Bundle newOptions){
        BakeService.startActionUpdateWidgets(context);
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds){
        BakeService.startActionUpdateWidgets(context);
    }

    public static void onUpdateStatic(Context context, ArrayList<String> ingred){
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int [] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, BakeWidgetProvider.class ));


        for( int appWidgetId: appWidgetIds){
            updateAppWidget(context, appWidgetManager,appWidgetId, ingred);
        }
    }

    @Override
    public void onEnabled(Context context) {}

    @Override
    public void onDisabled(Context context) {}


}

