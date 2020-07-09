package com.udacity.popularmovies.model;

public class Trailer {
    private static final String YOUTUBE_URL_ROOT = "https://youtu.be/";

    private final String mName;
    private final String mSource;

    public Trailer(String name,
                   String source) {
        mName = name;
        mSource = source;
    }

    public String getName() {
        return mName;
    }

    public String getVideoUrl() {
        return YOUTUBE_URL_ROOT + mSource;
    }
}
