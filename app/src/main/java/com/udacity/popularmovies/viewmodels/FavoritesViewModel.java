package com.udacity.popularmovies.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.udacity.popularmovies.database.AppDatabase;
import com.udacity.popularmovies.database.MovieEntry;

import java.util.List;

public class FavoritesViewModel extends AndroidViewModel {
    private static LiveData<List<MovieEntry>> mMovieEntryList;

    public FavoritesViewModel(@NonNull Application application) {
        super(application);
        AppDatabase db = AppDatabase.getInstance(this.getApplication());
        mMovieEntryList = db.movieDao().loadFavoriteMovies();
    }

    public static LiveData<List<MovieEntry>> getMovieEntryList() {
        return mMovieEntryList;
    }
}
