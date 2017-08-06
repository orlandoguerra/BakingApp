package com.savior.notes.bakingapp;

import android.content.Intent;
import android.app.FragmentManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.savior.notes.bakingapp.model.Baking;
import com.savior.notes.bakingapp.recycler.Constants;
import com.savior.notes.bakingapp.util.NetworkUtil;
import com.savior.notes.bakingapp.util.NoConnectivityException;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StepActivity extends AppCompatActivity  implements Callback<List<Baking>> {

    private static final String SAVED_RECIPE_ID = "SAVED_RECIPE_ID";
    private static final String SAVED_STEP_INDEX = "SAVED_STEP_INDEX";
    private static final String TAG = RecipeActivity.class.getSimpleName();
    private FragmentManager fragManager;
    private int receipeId;
    private int stepIndex;
    private VideoRecipeFragment fragmentVideo;
    private StepFragment fragmentStep;
    private boolean isFirstLoad;

    @Nullable
    @BindView(R.id.pagination_before)ImageView  pagBefore;
    @Nullable
    @BindView(R.id.pagination_after) ImageView  pagAfter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);
        ButterKnife.bind(this);

        Call<List<Baking>> call = NetworkUtil.getBakingCall(this);
        call.enqueue(this);

        fragManager = getFragmentManager();

        if (savedInstanceState == null) {
            Intent sendIntent = getIntent();
            receipeId = sendIntent.getIntExtra(Constants.RECIPE_ID,0);
            stepIndex = sendIntent.getIntExtra(Constants.STEP_INDEX, 0);
            isFirstLoad = true;
        }else{
            receipeId = savedInstanceState.getInt(SAVED_RECIPE_ID);
            stepIndex = savedInstanceState.getInt(SAVED_STEP_INDEX);
        }

    }

    public void pagination(View v) {
        Intent intent =  new Intent(StepActivity.this,StepActivity.class);
        intent.putExtra(Constants.RECIPE_ID, receipeId);
        int value = v.getId() ==  R.id.pagination_before?stepIndex-1:stepIndex+1;
        intent.putExtra(Constants.STEP_INDEX, value);
        finish();
        startActivity(intent);
    }

    @Override
    public void onResponse(Call<List<Baking>> call, Response<List<Baking>> response) {
        if(response.isSuccessful()) {
            Baking bak = getBakingReceipt(response.body());
            if(bak != null){
                setTitle(bak.getName());
                if(findViewById(R.id.detail_container) != null && isFirstLoad){
                    loadVideo(bak, stepIndex);
                }
                if(findViewById(R.id.step_container) != null  && isFirstLoad){
                    loadStep(bak, stepIndex);
                }
                setPaginationStatus(bak);
            }
        } else {
            Toast.makeText(this, getString(R.string.error_results), Toast.LENGTH_SHORT).show();
        }
    }

    private void setPaginationStatus(Baking bak){
        if(findViewById(R.id.pagination_before) != null){
            pagBefore.setVisibility(stepIndex == 0 ? View.INVISIBLE : View.VISIBLE);
            pagAfter.setVisibility(stepIndex < (bak.getSteps().size()-1)?View.VISIBLE:View.GONE);
        }
    }

    private Baking getBakingReceipt(List<Baking> bakings){
        for(Baking bak:bakings){
            if(bak.getId()==receipeId){
                return  bak;
            }
        }
        return null;
    }

    private void loadVideo(Baking bak, int stepIndex){
        if(bak.getSteps().get(stepIndex)==null)return;
        fragmentVideo = new VideoRecipeFragment();
        fragmentVideo.setUri(bak.getSteps().get(stepIndex).getVideoURL());
        fragManager.beginTransaction().add(R.id.detail_container,fragmentVideo ).commit();
    }


    private void loadStep(Baking bak, int stepIndex){
        if(bak.getSteps().get(stepIndex)==null)return;
        fragmentStep = new StepFragment();
        fragmentStep.setDescription(bak.getSteps().get(stepIndex).getDescription());
        fragManager.beginTransaction().add(R.id.step_container,fragmentStep ).commit();
    }

    @Override
    public void onFailure(Call<List<Baking>> call, Throwable t) {
        Toast.makeText(this, t instanceof NoConnectivityException ?
                getString(R.string.error_internet) : getString(R.string.error_results), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt(SAVED_RECIPE_ID, receipeId);
        savedInstanceState.putInt(SAVED_STEP_INDEX, stepIndex);
    }
}
