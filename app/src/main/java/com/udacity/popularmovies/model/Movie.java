package com.udacity.popularmovies.model;

import java.io.Serializable;

public class Movie implements Serializable {
    private static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w185";
    private final int mId;
    private final String mOriginalTitle;
    private final String mMoviePosterThumbnail;
    private final String mPlotSynopsis;
    private final String mUserRating;
    private final String mReleaseDate;
    private boolean mIsFavorite;

    public Movie(int id,
                 String originalTitle,
                 String moviePosterThumbnail,
                 String plotSynopsis,
                 String userRating,
                 String releaseDate) {
        mId = id;
        mOriginalTitle = originalTitle;
        mMoviePosterThumbnail = moviePosterThumbnail;
        mPlotSynopsis= plotSynopsis;
        mUserRating = userRating;
        mReleaseDate = releaseDate;
    }

    public Movie(int id,
                 String originalTitle,
                 String moviePosterThumbnail,
                 String plotSynopsis,
                 String userRating,
                 String releaseDate,
                 boolean isFavorite) {
        mId = id;
        mOriginalTitle = originalTitle;
        mMoviePosterThumbnail = moviePosterThumbnail;
        mPlotSynopsis= plotSynopsis;
        mUserRating = userRating;
        mReleaseDate = releaseDate;
        mIsFavorite = isFavorite;
    }

    public int getId() {
        return mId;
    }

    public String getOriginalTitle() {
        return mOriginalTitle;
    }

    public String getMoviePosterThumbnail() {
        return mMoviePosterThumbnail;
    }

    public String getMoviePosterThumbnailUrl() {
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

    public boolean getIsFavorite() {
        return mIsFavorite;
    }

    public void setIsFavorite(boolean isFavorite) {
        mIsFavorite = isFavorite;
    }
}
