package com.savior.notes.bakingapp;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Orlando on 7/18/2017.
 */

public class StepFragment  extends Fragment {

    private String description;
    private static final String STEP_INSTANCE = "STEP_INSTANCE";

    public void setDescription(String description){
        this.description = description;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance){
        View rootView = inflater.inflate(R.layout.fragment_step, container, false);
    //    if (savedInstance != null) {
    //        description  = savedInstance.getString(STEP_INSTANCE);
    //    }

        TextView etShortDesc = (TextView) rootView.findViewById(R.id.tv_description);
        etShortDesc.setText(description);
        setRetainInstance(true);
        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
       // super.onSaveInstanceState(outState);
       // outState.putString(STEP_INSTANCE,description);
    }
}