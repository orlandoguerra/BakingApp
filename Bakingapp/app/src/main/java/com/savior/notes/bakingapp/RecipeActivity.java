package com.savior.notes.bakingapp;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.savior.notes.bakingapp.model.Baking;
import com.savior.notes.bakingapp.recycler.Constants;
import com.savior.notes.bakingapp.util.NetworkUtil;
import com.savior.notes.bakingapp.util.NoConnectivityException;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RecipeActivity extends AppCompatActivity  implements Callback<List<Baking>>, ListItemClickListener{

    private static final String SAVED_RECIPE_ID = "SAVED_RECIPE_ID";
    private static final String SAVED_STEP_INDEX = "SAVED_STEP_INDEX";
    private static final String TAG = RecipeActivity.class.getSimpleName();
    private int receipeId;
    private Integer stepIndex;
    private VideoRecipeFragment fragmentVideo;
    private MasterRecipeFragment fragment;
    private StepFragment fragmentStep;
    private boolean isFirstLoad;
    private Baking bak;
    private FragmentManager fragManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        Intent sendIntent = getIntent();
        if (savedInstanceState == null) {

            receipeId = sendIntent.getIntExtra(Constants.RECIPE_ID,0);
            stepIndex = sendIntent.getIntExtra(Constants.STEP_INDEX,0);
            isFirstLoad = true;
        }else{
            receipeId = savedInstanceState.getInt(SAVED_RECIPE_ID);
            stepIndex = savedInstanceState.getInt(SAVED_STEP_INDEX);
        }
        fragManager = getFragmentManager();
        Call<List<Baking>> call = NetworkUtil.getBakingCall(this);
        call.enqueue(this);


    }




    @Override
    public void onListItemClick(int clickedItem) {

        if(findViewById(R.id.detail_container)== null){
            Intent intent =  new Intent(RecipeActivity.this,StepActivity.class);
            intent.putExtra(Constants.RECIPE_ID, receipeId);
            intent.putExtra(Constants.STEP_INDEX, clickedItem);
            startActivity(intent);
        }else{
            FragmentTransaction fm = fragManager.beginTransaction();
            Log.i(TAG,"Log number 54380");
            stepIndex = clickedItem;

            fragment = new MasterRecipeFragment();
            fragment.setStepIndex(stepIndex);
            fragment.setReceipeId(bak.getId());
            fragment.setClickAction(this);
            fragManager.beginTransaction().replace(R.id.master_container, fragment).commit();

            fragmentStep = new StepFragment();
            fragmentStep.setDescription(bak.getSteps().get(clickedItem).getDescription());

            fm.replace(R.id.step_container, fragmentStep);
            fm.commit();

            fragmentVideo = new VideoRecipeFragment();
            fragmentVideo.setUri(bak.getSteps().get(clickedItem).getVideoURL());
            fragManager.beginTransaction().
                    replace(R.id.detail_container,fragmentVideo ).commitAllowingStateLoss();
        }

    }


    @Override
    public void onResponse(Call<List<Baking>> call, Response<List<Baking>> response) {
        if(response.isSuccessful()) {
            bak = getBakingReceipt(response.body());
            if(bak != null){
                setTitle(bak.getName());
                if(findViewById(R.id.master_container) != null && isFirstLoad){
                    loadMaster(bak,stepIndex);
                }
                if(findViewById(R.id.detail_container) != null && isFirstLoad){
                    loadVideo(bak,stepIndex);
                }
                if(findViewById(R.id.step_container) != null && isFirstLoad){
                    loadStep(bak,stepIndex);
                }
            }
        } else {
            Toast.makeText(this, getString(R.string.error_results), Toast.LENGTH_SHORT).show();
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

    private void loadMaster(Baking bak, int stepIndex){
        if(bak.getSteps().get(stepIndex)==null)return;

        fragment = new MasterRecipeFragment();
        fragment.setStepIndex(stepIndex);
        fragment.setReceipeId(bak.getId());
        fragment.setClickAction(this);
        fragManager.beginTransaction().add(R.id.master_container,fragment ).commit();
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
