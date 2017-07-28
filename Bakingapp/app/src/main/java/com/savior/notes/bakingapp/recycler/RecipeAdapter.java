package com.savior.notes.bakingapp.recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.savior.notes.bakingapp.ListItemClickListener;
import com.savior.notes.bakingapp.R;
import com.savior.notes.bakingapp.model.Step;

import java.util.List;

/**
 * Created by Orlando on 7/17/2017.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeHolder> {

    private final ListItemClickListener mOnClickListener;
    private List<Step> steps;
    private int index;

    private static final String TAG = RecipeAdapter.class.getSimpleName();
    public RecipeAdapter(ListItemClickListener mOnClickListener, List<Step> steps, int index) {
        this.mOnClickListener = mOnClickListener;
        this.steps = steps;
        this.index = index;
    }
    public void swapCursor(List<Step> newSteps) {
        steps = newSteps;
        if (steps != null) {
            this.notifyDataSetChanged();
        }
    }
    public RecipeHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(viewType == 1?R.layout.recipe_item_selected:R.layout.recipe_item, viewGroup, false);
        RecipeHolder holder = new RecipeHolder(view,mOnClickListener);
        return holder;
    }

    @Override
    public int getItemCount() {
        if (steps == null) return 0;
        return steps.size();
    }
    public void onBindViewHolder(RecipeHolder holder, int position)  {
        Step step = steps.get(position);
        holder.mShortDescriptionTextView.setText(step.getShortDescription());
        holder.itemView.setTag(step.getId());
    }

    @Override
    public int getItemViewType(int position) {
        if(position==index)return 1;
        return 0;
    }

}
