package com.udacity.popularmovies.utils;

import com.udacity.popularmovies.model.Movie;
import com.udacity.popularmovies.model.Review;
import com.udacity.popularmovies.model.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public final class JsonUtils {
    private static final String KEY_MOVIE_ID = "id";
    private static final String KEY_TOTAL_PAGES = "total_pages";
    private static final String KEY_RESULTS = "results";
    private static final String KEY_ORIGINAL_TITLE = "original_title";
    private static final String KEY_MOVIE_POSTER_IMAGE = "poster_path";
    private static final String KEY_PLOT_SYNOPSIS = "overview";
    private static final String KEY_USER_RATING = "vote_average";
    private static final String KEY_RELEASE_DATE = "release_date";
    private static final String KEY_REVIEW_AUTHOR = "author";
    private static final String KEY_REVIEW_CONTENT = "content";
    private static final String KEY_TRAILER_NAME = "name";
    private static final String KEY_TRAILER_KEY = "key";

    private static final int FALLBACK_INT = 0;

    private int mMaximumNumberOfMoviePages;
    private int mMaximumNumberOfReviewPages;

    public JsonUtils() {
    }

    public List<Movie> parseMovieJson(String jsonString) {
        List<Movie> movies = new ArrayList<>();
        try {
            JSONObject jsonPayload = new JSONObject(jsonString);

            // get the list of movies
            mMaximumNumberOfMoviePages = jsonPayload.optInt(KEY_TOTAL_PAGES, FALLBACK_INT);
            JSONArray results = jsonPayload.optJSONArray(KEY_RESULTS);
            if (results != null) {
                for (int i = 0; i < results.length(); i++) {
                    JSONObject movieObject = results.getJSONObject(i);
                    Movie movie = new Movie(
                            movieObject.optInt(KEY_MOVIE_ID, 0),
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

    public List<Review> parseReviewJson(String jsonString) {
        List<Review> reviews = new ArrayList<>();
        try {
            JSONObject jsonPayload = new JSONObject(jsonString);
            // get the list of reviews
            mMaximumNumberOfReviewPages = jsonPayload.optInt(KEY_TOTAL_PAGES, FALLBACK_INT);
            JSONArray results = jsonPayload.optJSONArray(KEY_RESULTS);
            if (results != null) {
                for (int i = 0; i < results.length(); i++) {
                    JSONObject reviewObject = results.getJSONObject(i);
                    Review review = new Review(
                            reviewObject.optString(KEY_REVIEW_AUTHOR, ""),
                            reviewObject.optString(KEY_REVIEW_CONTENT, ""));
                    reviews.add(review);
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return reviews;
    }

    public List<Trailer> parseTrailerJson(String jsonString) {
        List<Trailer> trailers = new ArrayList<>();
        try {
            JSONObject jsonPayload = new JSONObject(jsonString);
            // get the list of reviews
            JSONArray results = jsonPayload.optJSONArray(KEY_RESULTS);
            if (results != null) {
                for (int i = 0; i < results.length(); i++) {
                    JSONObject trailerObject = results.getJSONObject(i);
                    Trailer trailer = new Trailer(
                            trailerObject.optString(KEY_TRAILER_NAME, ""),
                            trailerObject.optString(KEY_TRAILER_KEY, ""));
                    trailers.add(trailer);
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return trailers;
    }

    public int getMaximumNumberOfMoviePages() {
        return mMaximumNumberOfMoviePages;
    }

    public int getMaximumNumberOfReviewPages() {
        return mMaximumNumberOfReviewPages;
    }
}
