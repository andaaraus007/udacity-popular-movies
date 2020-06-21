package com.udacity.popularmovies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;

import com.udacity.popularmovies.interfaces.OnLoadMoreListener;
import com.udacity.popularmovies.model.Movie;
import com.udacity.popularmovies.utils.JsonUtils;
import com.udacity.popularmovies.utils.NetworkUtils;
import com.udacity.popularmovies.utils.ScreenUtils;

import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieAdapter.ListItemClickListener {
    private static final float PADDING_IN_DP = 16f;
    private static final float WIDTH_OF_THUMBNAIL = 185f;
    private static int pageNumber;
    private static int searchType;

    private MovieAdapter mAdapter;
    private List<Movie> mMovieList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView movieListRecyclerView = findViewById(R.id.rv_movies);
        pageNumber = 1;
        mMovieList = new ArrayList<>();
        int numberOfColumns;
        ScreenUtils screenUtils = new ScreenUtils(this);
        Resources r = getResources();
        float padding = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                PADDING_IN_DP,
                r.getDisplayMetrics()
        );

        float denominator = WIDTH_OF_THUMBNAIL + padding;
        numberOfColumns = Math.round(screenUtils.getWidthInPixels() / denominator);

        GridLayoutManager layoutManager = new GridLayoutManager (this, numberOfColumns);
        movieListRecyclerView.setLayoutManager(layoutManager);
        movieListRecyclerView.setHasFixedSize(true);

        mAdapter = new MovieAdapter(mMovieList, movieListRecyclerView, this);
        movieListRecyclerView.setAdapter(mAdapter);

        loadMovieData(pageNumber);

        mAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (++pageNumber < mAdapter.getMaximumNumberOfPages()) {
                    loadMovieData(pageNumber);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemSelected = item.getItemId();

        if (itemSelected == R.id.setting_popular) {
            if (searchType != NetworkUtils.getSearchTypePopular()) {
                mAdapter.clearMovieData();
                searchType = NetworkUtils.getSearchTypePopular();
                pageNumber = 1;
                loadMovieData(pageNumber);
                setTitle(R.string.app_name);
            }
            return true;
        } else if (itemSelected == R.id.setting_top_rated) {
            if (searchType != NetworkUtils.getSearchTypeTopRated()) {
                mAdapter.clearMovieData();
                searchType = NetworkUtils.getSearchTypeTopRated();
                pageNumber = 1;
                loadMovieData(pageNumber);
                setTitle(R.string.app_name_top_rated);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListItemClick(int itemClickedId) {
        Context context = MainActivity.this;
        Class<DetailActivity> destinationActivity = DetailActivity.class;
        Intent startChildActivityIntent = new Intent(context, destinationActivity);
        Movie movie = mMovieList.get(itemClickedId);
        startChildActivityIntent.putExtra("MovieDetails", movie);
        startActivity(startChildActivityIntent);
    }

    private void loadMovieData(int pageNumber) {

        Integer[] queryParams = {searchType, pageNumber};
        new FetchMovieTask(this).execute(queryParams);
    }

    public static class FetchMovieTask extends AsyncTask<Integer, Void, List<Movie>> {
        private final WeakReference<MainActivity> activityReference;

        FetchMovieTask(MainActivity context) {
            activityReference = new WeakReference<>(context);
        }
        @Override
        protected List<Movie> doInBackground(Integer... params) {
            if (params.length < 2) {
                return null;
            }

            int searchType = params[0];
            int page = params[1];
            List<Movie> movieList = new ArrayList<>();

            try {
                MainActivity activity = activityReference.get();
                URL movieRequestUrl = NetworkUtils.buildUrl(searchType, page);
                String jsonMovieListResponse = NetworkUtils.getResponseFromHttpUrl(movieRequestUrl);
                JsonUtils jsonUtils = new JsonUtils();
                movieList = jsonUtils.parseMovieJson(jsonMovieListResponse);
                activity.mAdapter.setMaximumNumberOfPages(jsonUtils.getMaximumNumberOfPages());

            } catch (Exception e) {
                e.printStackTrace();
            }
            return movieList;
        }

        @Override
        protected void onPostExecute(List<Movie> movieList) {
            if (movieList != null) {
                MainActivity activity = activityReference.get();
                activity.mAdapter.addMovieData(movieList);
                activity.mAdapter.setLoaded();
            }
        }
    }
}