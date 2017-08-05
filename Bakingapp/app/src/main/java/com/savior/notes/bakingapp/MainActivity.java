package com.savior.notes.bakingapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.savior.notes.bakingapp.recycler.BakeAdapter;
import com.savior.notes.bakingapp.recycler.Constants;
import com.savior.notes.bakingapp.model.Baking;
import com.savior.notes.bakingapp.util.NetworkUtil;
import com.savior.notes.bakingapp.util.NoConnectivityException;
import com.savior.notes.bakingapp.util.SimpleIdlingResource;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity  implements Callback<List<Baking>> {

    private static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView mRecyclerView;
    @BindView(R.id.pb_loading_indicator)ProgressBar mLoadingIndicator;
    private BakeAdapter mAdapter;
    private List<Baking> listBak;
    @Nullable
    private SimpleIdlingResource mIdlingResource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getIdlingResource();
        Log.i("xxxxxxxxxxx",""+mIdlingResource.isIdleNow());
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mLoadingIndicator.setVisibility(View.VISIBLE);
        if(findViewById(R.id.rv_recipe_600)==null){
            mRecyclerView = (RecyclerView) findViewById(R.id.rv_recipe);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            mRecyclerView.setLayoutManager(layoutManager);
        }else{
            mRecyclerView = (RecyclerView) findViewById(R.id.rv_recipe_600);
            GridLayoutManager layoutManager = new GridLayoutManager(this,3);
            mRecyclerView.setLayoutManager(layoutManager);
        }

        Call<List<Baking>> call = NetworkUtil.getBakingCall(this);
        call.enqueue(this);
    }

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }


    class ClickActivity implements ListItemClickListener{
        @Override
        public void onListItemClick(int clickedItem) {
            Baking bak = listBak.get(clickedItem);
            if(bak == null) return;
            Intent intent =  new Intent(MainActivity.this,RecipeActivity.class);
            intent.putExtra(Constants.RECIPE_ID, bak.getId());
            startActivity(intent);
        }
    }

    @Override
    public void onResponse(Call<List<Baking>> call, Response<List<Baking>> response) {
        if(response.isSuccessful()) {
            listBak = response.body();
            mAdapter = new BakeAdapter(new ClickActivity(),listBak);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            Toast.makeText(this, getString(R.string.error_results), Toast.LENGTH_SHORT).show();
        }
        mIdlingResource.setIdleState(true);
        mLoadingIndicator.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onFailure(Call<List<Baking>> call, Throwable t) {
        Toast.makeText(this, t instanceof NoConnectivityException ?
                getString(R.string.error_internet) : getString(R.string.error_results), Toast.LENGTH_SHORT).show();
        mLoadingIndicator.setVisibility(View.INVISIBLE);
    }

}
