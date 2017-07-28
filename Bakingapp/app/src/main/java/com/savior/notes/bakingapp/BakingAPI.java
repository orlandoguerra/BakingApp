package com.savior.notes.bakingapp;

import com.savior.notes.bakingapp.model.Baking;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Orlando on 7/17/2017.
 */

public interface BakingAPI {

    @GET("baking.json")
    Call<List<Baking>> loadChanges();
}
