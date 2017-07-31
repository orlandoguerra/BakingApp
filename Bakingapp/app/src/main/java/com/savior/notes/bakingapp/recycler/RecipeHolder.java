package com.savior.notes.bakingapp.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.savior.notes.bakingapp.ListItemClickListener;
import com.savior.notes.bakingapp.R;

/**
 * Created by Orlando on 7/17/2017.
 */

public class RecipeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private ListItemClickListener mOnClickListener;
    public TextView mShortDescriptionTextView;
    public ImageView mImageDescription;

    public RecipeHolder(View itemView, ListItemClickListener mOnClickListener) {
        super(itemView);
        this.mOnClickListener = mOnClickListener;
        mShortDescriptionTextView = (TextView) itemView.findViewById(R.id.tv_shortDescription);
        mImageDescription = (ImageView) itemView.findViewById(R.id.iv_imageDescription);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int clickedPosition = getAdapterPosition();
        this.mOnClickListener.onListItemClick(clickedPosition);
    }
}
