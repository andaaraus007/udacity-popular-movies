package com.udacity.popularmovies.model;

import java.io.Serializable;

public class Movie implements Serializable {
    private static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w185";
    private final String mOriginalTitle;
    private final String mMoviePosterThumbnail;
    private final String mPlotSynopsis;
    private final String mUserRating;
    private final String mReleaseDate;

    public Movie(String originalTitle,
                 String moviePosterThumbnail,
                 String plotSynopsis,
                 String userRating,
                 String releaseDate) {
        mOriginalTitle = originalTitle;
        mMoviePosterThumbnail = moviePosterThumbnail;
        mPlotSynopsis= plotSynopsis;
        mUserRating = userRating;
        mReleaseDate = releaseDate;
    }

    public String getOriginalTitle() {
        return mOriginalTitle;
    }

    public String getMoviePosterThumbnail() {
        return IMAGE_BASE_URL + mMoviePosterThumbnail;
    }

    public String getPlotSynopsis() {
        return mPlotSynopsis;
    }

    public String getUserRating() {
        return mUserRating;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

}
