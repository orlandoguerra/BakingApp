package com.savior.notes.bakingapp;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import android.widget.Toast;

import com.savior.notes.bakingapp.model.Baking;
import com.savior.notes.bakingapp.model.Ingredient;
import com.savior.notes.bakingapp.recycler.BakeAdapter;
import com.savior.notes.bakingapp.recycler.Constants;
import com.savior.notes.bakingapp.util.NetworkUtil;
import com.savior.notes.bakingapp.util.NoConnectivityException;

import java.util.List;



public class BakeGridWidgetService extends RemoteViewsService{


    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        List<String> ingredients = intent.getStringArrayListExtra(Constants.INGRED_INFO);
        return new BakeRemoteViewsFactory(this.getApplicationContext(),ingredients );
    }


    class BakeRemoteViewsFactory  implements RemoteViewsService.RemoteViewsFactory{
        Context mContext;
        List<String> ingredients;

        public BakeRemoteViewsFactory(Context appContext, List<String> ingredients ){
            mContext = appContext;
            this.ingredients = ingredients;
        }

        @Override
        public void onCreate() { }

        @Override
        public void onDataSetChanged() {

        }

        @Override
        public int getViewTypeCount() { return 1; }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public void onDestroy() {}

        @Override
        public RemoteViews getViewAt(int position) {
            if(ingredients == null) return null;
            String ingr = ingredients.get(position);
            RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.bake_widget_provider);
            views.setTextViewText(R.id.widget_id, ingr);
            Bundle extras = new Bundle();
            extras.putInt(Constants.RECIPE_ID, 0);
            Intent intent = new Intent();
            intent.putExtras(extras);
            views.setOnClickFillInIntent(R.id.widget_id, intent);
            return views;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getCount() {
            return ingredients == null?0:ingredients.size();
        }



    }


}