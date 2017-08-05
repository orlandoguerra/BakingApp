package com.savior.notes.bakingapp;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

import com.savior.notes.bakingapp.model.Baking;
import com.savior.notes.bakingapp.model.Ingredient;
import com.savior.notes.bakingapp.util.NetworkUtil;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BakeService extends IntentService implements Callback<List<Baking>> {

    public static final String REFRESH_ACTION_BAKE = "com.example.generic.testproof.action.bake.refresh";

    public BakeService() {
        super("BakeService");
    }

    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if(REFRESH_ACTION_BAKE.equals(action)) {
                handleAction();
            }
        }
    }

    public static void startActionUpdateWidgets(Context context) {
        Intent intent = new Intent(context, BakeService.class);
        intent.setAction(REFRESH_ACTION_BAKE);
        context.startService(intent);
    }

    private void handleAction() {
        Call<List<Baking>> call = NetworkUtil.getBakingCall(this);
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<List<Baking>> call, Response<List<Baking>> response) {
        if(response.isSuccessful()) {
            List<Baking> bakings = response.body();
            if(bakings == null || bakings.get(0) == null) return;
            List<Ingredient> ingredients = bakings.get(0).getIngredients();
            ArrayList<String> ingred = new ArrayList<String>();
            ingred.add(bakings.get(0).getName());
            for(Ingredient ing:ingredients){
                ingred.add(ing.getIngredient());
            }
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
            int [] appWidgetsIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, BakeWidgetProvider.class));
            BakeWidgetProvider.onUpdateStatic(this, ingred);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetsIds, R.id.widget_grid_view);
        }
    }

    @Override
    public void onFailure(Call<List<Baking>> call, Throwable t) {
        t.printStackTrace();
    }
}
