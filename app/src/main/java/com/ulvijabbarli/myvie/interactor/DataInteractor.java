package com.ulvijabbarli.myvie.interactor;

import android.content.Context;

import com.ulvijabbarli.myvie.api.ApiInitHelper;
import com.ulvijabbarli.myvie.data.pojo.MovieResponse;
import com.ulvijabbarli.myvie.util.Constants;

import io.reactivex.Observer;

public class DataInteractor extends BaseInteractor {

    private Context context;
    private final ApiInitHelper apiInitHelper;

    public DataInteractor(Context context) {
        this.context = context;
        apiInitHelper = new ApiInitHelper(context).initDefaultApi(Constants.BASE_URL).createService();
    }

    public void getMovies(Observer<MovieResponse> movieObserver) {
        apiInitHelper
                .getService()
                .getMovies(Constants.API_KEY_FOR_MOVIES)
                .compose(this.<MovieResponse>baseCall())
                .subscribe(movieObserver);
    }

    public void getMoviesByQuery(String query, Observer<MovieResponse> observer) {
        apiInitHelper
                .getService()
                .getMoviesBasedOnQuery(Constants.API_KEY_FOR_MOVIES, query)
                .compose(this.<MovieResponse>baseCall())
                .subscribe(observer);
    }

}
