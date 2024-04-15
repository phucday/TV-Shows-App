package com.example.tvshowsappmvvm.network;

import com.example.tvshowsappmvvm.responses.TVShowDetailsResponse;
import com.example.tvshowsappmvvm.responses.TVShowsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APiService {

    @GET("most-popular")
    Call<TVShowsResponse> getMostPopularTVShows(@Query("page") int page);

    @GET("show-details")
    Call<TVShowDetailsResponse> getTVShowDetails(@Query("q") String tvShowId);

    @GET("search")
    Call<TVShowsResponse> searchTVShow(@Query("q") String query,
                                       @Query("page") int page);
}
