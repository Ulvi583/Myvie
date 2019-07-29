package com.ulvijabbarli.myvie.api;

import com.ulvijabbarli.myvie.data.pojo.MovieResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Ulvi Jabbarli on 25/02/19.
 */
public interface ApiMethods {

    @GET("discover/movie")
        Observable<MovieResponse> getMovies(@Query("api_key") String api_key);

    @GET("search/movie")
    Observable<MovieResponse> getMoviesBasedOnQuery(@Query("api_key") String api_key, @Query("query") String q);

}

