package com.ulvijabbarli.myvie.ui.home;

import com.ulvijabbarli.myvie.BaseView;
import com.ulvijabbarli.myvie.data.pojo.MovieResponse;

public interface MainContractor {


    interface View extends BaseView<Presenter> {

        void showToast(String s);

        void displayMovies(MovieResponse movieResponse);

        void displayError(String s);
    }


    interface Presenter {
        void getMovies();

        void getResultsBasedOnQuery(String query);


    }
}
