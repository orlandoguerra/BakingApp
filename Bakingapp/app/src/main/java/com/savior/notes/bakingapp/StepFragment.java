package com.savior.notes.bakingapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Orlando on 7/18/2017.
 */

public class StepFragment  extends Fragment {

    private String description;

    public void setDescription(String description){
        this.description = description;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance){
        View rootView = inflater.inflate(R.layout.fragment_step, container, false);
        TextView etShortDesc = (TextView) rootView.findViewById(R.id.tv_description);
        etShortDesc.setText(description);
        setRetainInstance(true);
        return rootView;
    }
}