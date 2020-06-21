package com.udacity.popularmovies.utils;

import com.udacity.popularmovies.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public final class JsonUtils {
    private static final String KEY_TOTAL_PAGES = "total_pages";
    private static final String KEY_RESULTS = "results";
    private static final String KEY_ORIGINAL_TITLE = "original_title";
    private static final String KEY_MOVIE_POSTER_IMAGE = "poster_path";
    private static final String KEY_PLOT_SYNOPSIS = "overview";
    private static final String KEY_USER_RATING = "vote_average";
    private static final String KEY_RELEASE_DATE = "release_date";

    private static final int FALLBACK_INT = 0;

    private int mMaximumNumberOfPages;

    public JsonUtils() {
    }

    public List<Movie> parseMovieJson(String jsonString) {
        List<Movie> movies = new ArrayList<>();
        try {
            JSONObject jsonPayload = new JSONObject(jsonString);

            // get the list of movies
            mMaximumNumberOfPages = jsonPayload.optInt(KEY_TOTAL_PAGES, FALLBACK_INT);
            JSONArray results = jsonPayload.optJSONArray(KEY_RESULTS);
            if (results != null) {
                for (int i = 0; i < results.length(); i++) {
                    JSONObject movieObject = results.getJSONObject(i);
                    Movie movie = new Movie(
                            movieObject.optString(KEY_ORIGINAL_TITLE, ""),
                            movieObject.optString(KEY_MOVIE_POSTER_IMAGE, ""),
                            movieObject.optString(KEY_PLOT_SYNOPSIS, ""),
                            movieObject.optString(KEY_USER_RATING, ""),
                            movieObject.optString(KEY_RELEASE_DATE, ""));
                    movies.add(movie);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return movies;
    }

    public int getMaximumNumberOfPages() {
        return mMaximumNumberOfPages;
    }
}
