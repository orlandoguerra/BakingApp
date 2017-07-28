package com.savior.notes.bakingapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
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

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Implementation of App Widget functionality.
 */
public class BakeWidgetProvider extends AppWidgetProvider  {

    private static final String TAG = BakeWidgetProvider.class.getSimpleName();

    static void updateAppWidget(final Context context, final AppWidgetManager appWidgetManager, final int appWidgetId) {
        Call<List<Baking>> call = NetworkUtil.getBakingCall(context);
        Log.i(TAG, "Step 1");
        call.enqueue(new Callback<List<Baking>>() {
            public void onResponse(Call<List<Baking>> call, Response<List<Baking>> response) {
                Log.i(TAG, "Step 2");
                if (response.isSuccessful()) {
                    Log.i(TAG, "Step 3");
                    Baking bak = getBakingReceipt(response.body());
                    RemoteViews views = getSingleRemoteView(context,bak);
                    appWidgetManager.updateAppWidget(appWidgetId, views);
                }
            }

            @Override
            public void onFailure(Call<List<Baking>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }


    private static Baking getBakingReceipt(List<Baking> bakings){
        for(Baking bak:bakings){
            return  bak;
        }
        return null;
    }

    public static RemoteViews getSingleRemoteView(Context context,Baking bak) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.bake_widget_provider);
        Intent intent =  new Intent(context,RecipeActivity.class);
        intent.putExtra(Constants.RECIPE_ID, bak.getId());
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.widget_icon, pendingIntent);
        views.setTextViewText(R.id.widget_id, bak.getName());
        return views;
    }
/*
    public static RemoteViews getSingleRemoteView(Context context) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.bake_widget_provider);
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.widget_id, pendingIntent);
        return views;
    }
*/
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {}

    @Override
    public void onDisabled(Context context) {}


}

