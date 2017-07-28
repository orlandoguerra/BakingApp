package com.savior.notes.bakingapp.recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.savior.notes.bakingapp.ListItemClickListener;
import com.savior.notes.bakingapp.R;
import com.savior.notes.bakingapp.model.Baking;

import java.util.List;

/**
 * Created by Orlando on 7/17/2017.
 */

public class BakeAdapter extends RecyclerView.Adapter<BakeHolder> {

    private final ListItemClickListener mOnClickListener;
    private List<Baking> listBak;

    private static final String TAG = BakeAdapter.class.getSimpleName();

    public BakeAdapter(ListItemClickListener mOnClickListener, List<Baking> listBak) {
        this.mOnClickListener = mOnClickListener;
        this.listBak = listBak;
    }
    public void swapCursor(List<Baking> newListBak) {
        if (newListBak != null) {
            this.notifyDataSetChanged();
        }
    }
    public BakeHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.bake_item, viewGroup, false);
        BakeHolder holder = new BakeHolder(view,mOnClickListener);
        return holder;
    }
    @Override
    public int getItemCount() {
        if (listBak == null) return 0;
        return listBak.size();
    }

    public void onBindViewHolder(BakeHolder holder, int position)  {
        Baking bak = listBak.get(position);
        if(bak == null) return;

        holder.mNameTextView.setText(bak.getName());
        holder.itemView.setTag(bak.getId());
        holder.mServingsTextView.setText(String.valueOf(bak.getServings()));
    }

}