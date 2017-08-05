package com.savior.notes.bakingapp;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.savior.notes.bakingapp.model.Baking;
import com.savior.notes.bakingapp.recycler.Constants;
import com.savior.notes.bakingapp.recycler.RecipeAdapter;
import com.savior.notes.bakingapp.util.NetworkUtil;
import com.savior.notes.bakingapp.util.NoConnectivityException;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by Orlando on 7/17/2017.
 */

public class MasterRecipeFragment extends Fragment  implements Callback<List<Baking>> {

    @BindView(R.id.rv_recipe) RecyclerView mRecyclerView;
    @BindView(R.id.pb_loading_indicator) ProgressBar mLoadingIndicator;
    @BindView(R.id.tv_ingredients) TextView ingredients;
    private static final String SAVED_RECIPE_ID = "SAVED_RECIPE_ID";
    private static final String SAVED_STEP_INDEX = "SAVED_STEP_INDEX";

    public RecipeAdapter mAdapter;
    private int receipeId;
    private int stepIndex;

    private ListItemClickListener clickAction;

    public void setStepIndex(int stepIndex){
        this.stepIndex = stepIndex;
    }

    public void setReceipeId(int receipeId){
        this.receipeId = receipeId;
    }

    public MasterRecipeFragment(){}

    public void setClickAction(ListItemClickListener clickAction){
        this.clickAction = clickAction;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ListItemClickListener) {
            clickAction = (ListItemClickListener) context;
        } else {
            throw new ClassCastException(context.toString()  + " must implement ListItemClickListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance){
        View rootView = inflater.inflate(R.layout.fragment_recipe_master, container, false);
        if (savedInstance != null) {
            receipeId = savedInstance.getInt(SAVED_RECIPE_ID);
            stepIndex = savedInstance.getInt(SAVED_STEP_INDEX);
        }
        ButterKnife.bind(this, rootView);
        mLoadingIndicator.setVisibility(View.VISIBLE);
        Call<List<Baking>> call = NetworkUtil.getBakingCall(getActivity());
        call.enqueue(this);
        return rootView;
    }

    @Override
    public void onResponse(Call<List<Baking>> call, Response<List<Baking>> response) {
        if(response.isSuccessful()) {
            final Baking bak = getBakingReceipt(response.body());
            if(bak != null){

                ingredients.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent ingredIntent = new Intent(getActivity(), IngredientsActivity.class);
                        ingredIntent.putExtra(Constants.RECIPE_ID, bak.getId());
                        startActivity(ingredIntent);
                    }
                });

                LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                mRecyclerView.setLayoutManager(layoutManager);
                mAdapter = new RecipeAdapter(clickAction, bak.getSteps(),stepIndex);
                mRecyclerView.setAdapter(mAdapter);
            }
        } else {
            Toast.makeText(getActivity(), getString(R.string.error_results), Toast.LENGTH_SHORT).show();
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

    @Override
    public void onFailure(Call<List<Baking>> call, Throwable t) {
        Toast.makeText(getActivity(), t instanceof NoConnectivityException ?
                getString(R.string.error_internet) : getString(R.string.error_results), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVED_RECIPE_ID, receipeId);
        outState.putInt(SAVED_STEP_INDEX, stepIndex);
    }

}
