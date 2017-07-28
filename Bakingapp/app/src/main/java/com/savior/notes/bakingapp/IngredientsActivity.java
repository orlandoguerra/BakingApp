package com.savior.notes.bakingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.savior.notes.bakingapp.model.Baking;
import com.savior.notes.bakingapp.recycler.Constants;
import com.savior.notes.bakingapp.recycler.IngredientsAdapter;
import com.savior.notes.bakingapp.util.NetworkUtil;
import com.savior.notes.bakingapp.util.NoConnectivityException;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IngredientsActivity extends AppCompatActivity implements Callback<List<Baking>> {

    private RecyclerView mRecyclerView;
    private ProgressBar mLoadingIndicator;
    private IngredientsAdapter mAdapter;
    private int receipeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients);
        setTitle(getString(R.string.app_ingredients));
        Intent sendIntent = getIntent();
        receipeId = sendIntent.getIntExtra(Constants.RECIPE_ID,0);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_ingredients);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        mLoadingIndicator.setVisibility(View.VISIBLE);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        Call<List<Baking>> call = NetworkUtil.getBakingCall(this);
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<List<Baking>> call, Response<List<Baking>> response) {
        List<Baking> listBak = response.body();
        if(response.isSuccessful()) {
            for(Baking bak:listBak){
                if(bak.getId()==receipeId){
                    mAdapter = new IngredientsAdapter(bak.getIngredients());
                    mRecyclerView.setAdapter(mAdapter);
                    break;
                }
            }
        } else {
            Toast.makeText(this, getString(R.string.error_results), Toast.LENGTH_SHORT).show();
        }
        mLoadingIndicator.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onFailure(Call<List<Baking>> call, Throwable t) {
        Toast.makeText(this, t instanceof NoConnectivityException ?
                getString(R.string.error_internet) : getString(R.string.error_results), Toast.LENGTH_SHORT).show();
        mLoadingIndicator.setVisibility(View.INVISIBLE);
    }
}
