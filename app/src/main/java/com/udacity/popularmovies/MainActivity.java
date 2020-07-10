package com.udacity.popularmovies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;

import com.udacity.popularmovies.adapter.MovieAdapter;
import com.udacity.popularmovies.database.MovieEntry;
import com.udacity.popularmovies.interfaces.OnLoadMoreListener;
import com.udacity.popularmovies.model.Movie;
import com.udacity.popularmovies.utils.AppExecutors;
import com.udacity.popularmovies.utils.JsonUtils;
import com.udacity.popularmovies.utils.NetworkUtils;
import com.udacity.popularmovies.utils.ScreenUtils;
import com.udacity.popularmovies.viewmodels.FavoritesViewModel;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieAdapter.ListItemClickListener {
    private static final String KEY_SAVE_INSTANCE_RECYCLE_VIEW_STATE = "saves_rv_state";
    private static final Integer SEARCH_TYPE_POPULAR = 0;
    private static final Integer SEARCH_TYPE_TOP_RATED = 1;
    private static final Integer SEARCH_TYPE_FAVORITE = 2;
    private static final float PADDING_IN_DP = 16f;
    private static final float WIDTH_OF_THUMBNAIL = 185f;

    private static int pageNumber;
    private static int searchType;
    private static List<Movie> mMovieList;
    private static List<MovieEntry> mMovieEntryList;
    private static List<Integer> mFavoriteMovieIds;

    private GridLayoutManager mGridLayoutManager;
    private Parcelable mGridLayoutManagerSaveState;
    private MovieAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        RecyclerView movieListRecyclerView = findViewById(R.id.rv_movies);

        if (savedInstanceState != null && savedInstanceState.containsKey(KEY_SAVE_INSTANCE_RECYCLE_VIEW_STATE)) {
            mGridLayoutManagerSaveState = savedInstanceState.getParcelable(KEY_SAVE_INSTANCE_RECYCLE_VIEW_STATE);
        }

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

        mGridLayoutManager = new GridLayoutManager (this, numberOfColumns);
        movieListRecyclerView.setLayoutManager(mGridLayoutManager);
        movieListRecyclerView.setHasFixedSize(true);

        if (mGridLayoutManagerSaveState == null) {
            pageNumber = 1;
            mMovieList = new ArrayList<>();
            mFavoriteMovieIds = new ArrayList<>();
        }

        mAdapter = new MovieAdapter(mMovieList, movieListRecyclerView, this);
        movieListRecyclerView.setAdapter(mAdapter);

        setTitleForPage();

        if (mGridLayoutManagerSaveState != null) {
            mGridLayoutManager.onRestoreInstanceState(mGridLayoutManagerSaveState);
        } else {
            setupFavoritesViewModel();
            loadMovieData(pageNumber);
        }

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
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(KEY_SAVE_INSTANCE_RECYCLE_VIEW_STATE,
                mGridLayoutManager.onSaveInstanceState());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemSelected = item.getItemId();
        boolean needToChangeTitle = false;

        if (itemSelected == R.id.setting_popular) {
            if (searchType != SEARCH_TYPE_POPULAR) {
                needToChangeTitle = true;
                mAdapter.clearMovieData();
                searchType = SEARCH_TYPE_POPULAR;
                pageNumber = 1;
                loadMovieData(pageNumber);
            }
        } else if (itemSelected == R.id.setting_top_rated) {
            if (searchType != SEARCH_TYPE_TOP_RATED) {
                needToChangeTitle = true;
                mAdapter.clearMovieData();
                searchType = SEARCH_TYPE_TOP_RATED;
                pageNumber = 1;
                loadMovieData(pageNumber);
            }
        } else if (itemSelected == R.id.setting_favorite) {
            if (searchType != SEARCH_TYPE_FAVORITE) {
                needToChangeTitle = true;
                mAdapter.clearMovieData();
                searchType = SEARCH_TYPE_FAVORITE;
                pageNumber = 1;
                mAdapter.setMaximumNumberOfPages(1);
                loadMovieData(pageNumber);
            }
        } else {
            return super.onOptionsItemSelected(item);
        }

        if (needToChangeTitle) {
            setTitleForPage();
        }

        return true;
    }

    @Override
    public void onListItemClick(int itemClickedId) {
        Context context = MainActivity.this;
        Class<DetailActivity> destinationActivity = DetailActivity.class;
        Intent startChildActivityIntent = new Intent(context, destinationActivity);
        Movie movie = mMovieList.get(itemClickedId);
        movie.setIsFavorite(mFavoriteMovieIds.contains(movie.getId()));
        startChildActivityIntent.putExtra(String.valueOf(R.string.app_name_detail), movie);
        startActivity(startChildActivityIntent);
    }

    private void setTitleForPage() {
        if (searchType == SEARCH_TYPE_POPULAR) {
            setTitle(R.string.app_name);
        } else if (searchType == SEARCH_TYPE_TOP_RATED) {
            setTitle(R.string.app_name_top_rated);
        } else if (searchType == SEARCH_TYPE_FAVORITE) {
            setTitle(R.string.app_name_favorite);
        }
    }

    private void loadMovieData(final int pageNumber) {
        if (searchType == SEARCH_TYPE_FAVORITE) {
            if (mMovieEntryList != null) {
                mAdapter.addMovieData(convertToMovies(mMovieEntryList));
            }

        } else {
            AppExecutors.getInstance().getNetworkIO().execute(new Runnable() {
                @Override
                public void run() {
                    URL movieRequestUrl = NetworkUtils.buildUrlForMovieList(searchType == SEARCH_TYPE_POPULAR, pageNumber);
                    String jsonMovieListResponse = null;
                    try {
                        jsonMovieListResponse = NetworkUtils.getResponseFromHttpUrl(movieRequestUrl);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    final JsonUtils jsonUtils = new JsonUtils();
                    final List<Movie> movieList = jsonUtils.parseMovieJson(jsonMovieListResponse);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.setMaximumNumberOfPages(jsonUtils.getMaximumNumberOfMoviePages());
                            mAdapter.addMovieData(movieList);
                            mAdapter.setLoaded();
                        }
                    });
                }
            });
        }
    }

    private void setupFavoritesViewModel() {
        ViewModelProviders.of(this)
                .get(FavoritesViewModel.class)
                .getMovieEntryList()
                .observe(this, new Observer<List<MovieEntry>>() {
            @Override
            public void onChanged(List<MovieEntry> movieEntryList) {
                mMovieEntryList = movieEntryList;
                for (MovieEntry movieEntry : movieEntryList) {
                    mFavoriteMovieIds.add(movieEntry.getId());
                }
                if (searchType == SEARCH_TYPE_FAVORITE) {
                    mAdapter.clearMovieData();
                    mAdapter.addMovieData(convertToMovies(mMovieEntryList));
                }
            }
        });
    }

    private List<Movie> convertToMovies(List<MovieEntry> movieEntryList) {
        List<Movie> movieList = new ArrayList<>();

        for (MovieEntry movieEntry : movieEntryList) {
            Movie movie = new Movie(movieEntry.getId(),
                    movieEntry.getTitle(),
                    movieEntry.getMoviePoster(),
                    movieEntry.getPlotSynopsis(),
                    movieEntry.getUserRating(),
                    movieEntry.getReleaseDate(),
                    true);
            movieList.add(movie);
        }

        return movieList;
    }
}