package com.udacity.popularmovies.utils;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public final class NetworkUtils {
    private static final String TAG = NetworkUtils.class.getSimpleName();
    private static final String URI_SCHEME = "https";
    private static final String URI_HOST = "api.themoviedb.org";
    private static final String URI_PATH_POPULAR = "movie/popular";
    private static final String URI_PATH_TOP_RATED = "movie/top_rated";
    private static final String URI_QUERY_PARAM_API_KEY = "api_key";
    // @TODO: Need to replace with your own api key
    private static final String URI_QUERY_PARAM_API_KEY_VALUE = "YOUR_API_KEY";

    public static URL buildUrl(boolean isPopular) {
        String path = isPopular ? URI_PATH_POPULAR : URI_PATH_TOP_RATED;
        Uri.Builder uriBuilder = new Uri.Builder();
        Uri apiUri = uriBuilder.scheme(URI_SCHEME)
                .authority(URI_HOST)
                .appendPath(path)
                .appendQueryParameter(URI_QUERY_PARAM_API_KEY, URI_QUERY_PARAM_API_KEY_VALUE)
                .build();

        URL url = null;
        try {
            url = new URL(apiUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

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