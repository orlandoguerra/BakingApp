package com.savior.notes.bakingapp.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.savior.notes.bakingapp.R;

/**
 * Created by 700000075 on 7/26/2017.
 */
public class IngredientsHolder extends RecyclerView.ViewHolder{

    public TextView mIngredientTextView;
    public TextView mQuantityTextView;

    public IngredientsHolder(View itemView) {
        super(itemView);
        mIngredientTextView = (TextView) itemView.findViewById(R.id.tv_ingredient);
        mQuantityTextView = (TextView) itemView.findViewById(R.id.tv_quantity);
    }

}