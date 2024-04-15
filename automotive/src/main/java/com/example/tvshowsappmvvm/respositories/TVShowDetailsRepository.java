package com.example.tvshowsappmvvm.respositories;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.tvshowsappmvvm.network.APiService;
import com.example.tvshowsappmvvm.network.ApiClient;
import com.example.tvshowsappmvvm.responses.TVShowDetailsResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TVShowDetailsRepository {

    private APiService aPiService;

    public TVShowDetailsRepository() {
        aPiService = ApiClient.getRetrofit().create(APiService.class);
    }

    public LiveData<TVShowDetailsResponse> getTVShowDetails(String tvShowId){
        MutableLiveData<TVShowDetailsResponse> data = new MutableLiveData<>();
        aPiService.getTVShowDetails(tvShowId).enqueue(new Callback<TVShowDetailsResponse>() {
            @Override
            public void onResponse(Call<TVShowDetailsResponse> call, Response<TVShowDetailsResponse> response) {
                data.setValue(response.body());
                Log.d("CHECKCALL",response.body().getTvShowDetails().getUrl());
            }

            @Override
            public void onFailure(Call<TVShowDetailsResponse> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }
}
