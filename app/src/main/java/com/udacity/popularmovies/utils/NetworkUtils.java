package com.udacity.popularmovies.utils;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public final class NetworkUtils {
    private static final String URI_SCHEME = "https";
    private static final String URI_HOST = "api.themoviedb.org";
    private static final String URI_PATH_BASE = "3/movie/";
    private static final String URI_PATH_POPULAR = "popular";
    private static final String URI_PATH_TOP_RATED = "top_rated";
    private static final String URI_PATH_REVIEWS = "/reviews";
    private static final String URI_PATH_TRAILERS = "/videos";
    private static final String URI_QUERY_PARAM_API_KEY = "api_key";
    private static final String URI_QUERY_PARAM_REGION = "region";
    private static final String URI_QUERY_PARAM_REGION_VALUE = "us";
    private static final String URI_QUERY_PARAM_PAGE = "page";

    // @TODO: Need to replace with your own api key
    private static final String URI_QUERY_PARAM_API_KEY_VALUE = "[REPLACE_THE_YOUR_OWN_API_KEY]";

    public static URL buildUrlForMovieList(boolean isPopular, int page) {
        String path = URI_PATH_BASE + (isPopular ? URI_PATH_POPULAR : URI_PATH_TOP_RATED);
        Uri.Builder uriBuilder = new Uri.Builder();
        Uri apiUri = uriBuilder.scheme(URI_SCHEME)
                .authority(URI_HOST)
                .appendEncodedPath(path)
                .appendQueryParameter(URI_QUERY_PARAM_API_KEY, URI_QUERY_PARAM_API_KEY_VALUE)
                .appendQueryParameter(URI_QUERY_PARAM_REGION, URI_QUERY_PARAM_REGION_VALUE)
                .appendQueryParameter(URI_QUERY_PARAM_PAGE, String.valueOf(page))
                .build();
        URL url = null;

        try {
            url = new URL(apiUri.toString());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static URL buildUrlForReviewList(int movieId, int page) {
        String path = URI_PATH_BASE + movieId + URI_PATH_REVIEWS;
        Uri.Builder uriBuilder = new Uri.Builder();
        Uri apiUri = uriBuilder.scheme(URI_SCHEME)
                .authority(URI_HOST)
                .appendEncodedPath(path)
                .appendQueryParameter(URI_QUERY_PARAM_API_KEY, URI_QUERY_PARAM_API_KEY_VALUE)
                .appendQueryParameter(URI_QUERY_PARAM_PAGE, String.valueOf(page))
                .build();
        URL url = null;

        try {
            url = new URL(apiUri.toString());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static URL buildUrlForTrailerList(int movieId) {
        String path = URI_PATH_BASE + movieId + URI_PATH_TRAILERS;
        Uri.Builder uriBuilder = new Uri.Builder();
        Uri apiUri = uriBuilder.scheme(URI_SCHEME)
                .authority(URI_HOST)
                .appendEncodedPath(path)
                .appendQueryParameter(URI_QUERY_PARAM_API_KEY, URI_QUERY_PARAM_API_KEY_VALUE)
                .build();
        URL url = null;

        try {
            url = new URL(apiUri.toString());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}