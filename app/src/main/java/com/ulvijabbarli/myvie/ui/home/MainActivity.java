package com.ulvijabbarli.myvie.ui.home;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ulvijabbarli.myvie.R;
import com.ulvijabbarli.myvie.data.pojo.MovieResponse;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MainContractor.View {

    private MainContractor.Presenter mainPresenter;
    @BindView(R.id.rvMovies)
    RecyclerView rvMovies;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        new MainPresenter(this);
        setSupportActionBar(toolbar);
        rvMovies.setLayoutManager(new LinearLayoutManager(this));
        mainPresenter.getMovies();
    }

    @Override
    public void setPresenter(MainContractor.Presenter presenter) {
        this.mainPresenter = presenter;
    }

    @Override
    public Context getContextOfActivity() {
        return this;
    }

    @Override
    public Activity getActivityOfActivity() {
        return this;
    }

    @Override
    public void showErrorMessage(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showToast(String str) {
        Toast.makeText(MainActivity.this, str, Toast.LENGTH_LONG).show();
    }

    @Override
    public void displayMovies(MovieResponse movieResponse) {
        if (movieResponse != null && movieResponse.getResults().size() > 0) {
            String TAG = "MainActivity";
            Log.d(TAG, movieResponse.getResults().get(0).getTitle());
            adapter = new MoviesAdapter(movieResponse.getResults(), MainActivity.this);
            rvMovies.setAdapter(adapter);
        } else {
            rvMovies.setAdapter(null);
        }
    }

    @Override
    public void displayError(String e) {
        showToast(e);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setQueryHint("Enter Movie name..");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.trim().length() > 0) {
                    mainPresenter.getResultsBasedOnQuery(query.trim());
                } else {
                    mainPresenter.getMovies();
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.trim().length() > 0) {
                    mainPresenter.getResultsBasedOnQuery(newText.trim());
                } else {
                    mainPresenter.getMovies();
                }
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
