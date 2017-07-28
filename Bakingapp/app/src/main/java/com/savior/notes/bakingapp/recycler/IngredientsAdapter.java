package com.savior.notes.bakingapp.recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.savior.notes.bakingapp.R;
import com.savior.notes.bakingapp.model.Ingredient;

import java.util.List;

/**
 * Created by 700000075 on 7/26/2017.
 */
public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsHolder> {

    private List<Ingredient> ingredients;
    private static final String TAG = IngredientsAdapter.class.getSimpleName();

    public IngredientsAdapter(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }
    public void swapCursor(List<Ingredient> newIngredients) {
        if (newIngredients != null) {
            this.ingredients = newIngredients;
        }
    }
    public IngredientsHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.ingredients_item, viewGroup, false);
        IngredientsHolder holder = new IngredientsHolder(view);
        return holder;
    }
    @Override
    public int getItemCount() {
        if (ingredients == null) return 0;
        return ingredients.size();
    }
    public void onBindViewHolder(IngredientsHolder holder, int position)  {
        Ingredient ingre = ingredients.get(position);
        if(ingre == null)return;
        holder.mIngredientTextView.setText(ingre.getIngredient());
        holder.mQuantityTextView.setText(String.valueOf(ingre.getQuantity())+" "+ingre.getMeasure());
        holder.itemView.setTag(ingre.getIngredient());
    }
}
