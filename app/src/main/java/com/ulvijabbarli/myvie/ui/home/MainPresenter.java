package com.ulvijabbarli.myvie.ui.home;

import com.ulvijabbarli.myvie.api.BaseSubscriber;
import com.ulvijabbarli.myvie.data.pojo.MovieResponse;
import com.ulvijabbarli.myvie.interactor.DataInteractor;

import io.reactivex.disposables.Disposable;

public class MainPresenter implements MainContractor.Presenter {

    private MainContractor.View mainView;
    private String TAG = "MainPresenter";
    private DataInteractor dataInteractor;

    MainPresenter(MainContractor.View mainView) {
        this.mainView = mainView;
        mainView.setPresenter(this);
        dataInteractor = new DataInteractor(mainView.getContextOfActivity());
    }

    @Override
    public void getResultsBasedOnQuery(String query) {
        if (mainView == null) return;
        mainView.showProgress();
        dataInteractor.getMoviesByQuery(query, new SearchFilmObserver());
    }

    @Override
    public void getMovies() {
        if (mainView == null) return;
        mainView.showProgress();
        dataInteractor.getMovies(new FilmObserver());
    }


    public class FilmObserver extends BaseSubscriber<MovieResponse>{

        @Override
        public void onSubscribe(Disposable d) {

        }

        @Override
        public void onNext(MovieResponse movieResponse) {
            mainView.displayMovies(movieResponse);
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
            mainView.hideProgress();
            mainView.displayError("Error fetching movie data");
        }

        @Override
        public void onComplete() {
            mainView.hideProgress();
        }
    }

    public class SearchFilmObserver extends BaseSubscriber<MovieResponse> {


        @Override
        public void onSubscribe(Disposable d) {

        }

        @Override
        public void onNext(MovieResponse movieResponse) {
            mainView.displayMovies(movieResponse);
        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);
            e.printStackTrace();
            mainView.hideProgress();
            mainView.displayError("Error fetching Movie Data");
        }

        @Override
        public void onComplete() {
            mainView.hideProgress();
        }
    }
}
