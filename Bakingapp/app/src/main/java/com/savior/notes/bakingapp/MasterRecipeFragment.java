package com.savior.notes.bakingapp;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.savior.notes.bakingapp.model.Baking;
import com.savior.notes.bakingapp.recycler.Constants;
import com.savior.notes.bakingapp.recycler.RecipeAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Orlando on 7/17/2017.
 */

public class MasterRecipeFragment extends Fragment {

    @BindView(R.id.rv_recipe) RecyclerView mRecyclerView;
    @BindView(R.id.pb_loading_indicator) ProgressBar mLoadingIndicator;
    @BindView(R.id.tv_ingredients) TextView ingredients;

    public RecipeAdapter mAdapter;
    private Baking bak;
    private int stepIndex;

    private ListItemClickListener clickAction;

    public void setStepIndex(int stepIndex){
        this.stepIndex = stepIndex;
    }

    public void setReceipe(Baking bak){
        this.bak = bak;
    }

    public MasterRecipeFragment(){}

    public void setClickAction(ListItemClickListener clickAction){
        this.clickAction = clickAction;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance){
        View rootView = inflater.inflate(R.layout.fragment_recipe_master, container, false);
        ButterKnife.bind(this,rootView);

        ingredients.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent ingredIntent = new Intent(getActivity(),IngredientsActivity.class);
                ingredIntent.putExtra(Constants.RECIPE_ID,bak.getId());
                startActivity(ingredIntent);
            }
        });

        mLoadingIndicator.setVisibility(View.VISIBLE);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new RecipeAdapter(clickAction, bak.getSteps(),stepIndex);
        mRecyclerView.setAdapter(mAdapter);
        setRetainInstance(true);
        return rootView;
    }

}
