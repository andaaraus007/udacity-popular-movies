package com.udacity.popularmovies.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Movie {
    private String mOriginalTitle;
    private String mMoviePosterThumbnail;
    private String mPlotSynopsis;
    private String mUserRating;
    private String mReleaseDate;

    public static Movie[] parseMovieJson(String jsonString) throws JSONException {
        Movie movies= null;
        JSONObject jsonPayload = new JSONObject(jsonString);

        if (jsonPayload instanceof JSONObject) {

        }

        return movies;
    }

    @Nullable
    public static String optString(@NonNull JSONObject json, @NonNull String parameter) throws JSONException {
        if (isParameterValid(json, parameter)) {
            return json.getString(parameter);
        }
        return null;
    }

    @Nullable
    public static JSONObject optJSONObject(@NonNull JSONObject json, @NonNull String parameter) throws JSONException {
        if (isParameterValid(json, parameter)) {
            JSONObject response = json.optJSONObject(parameter);
            if (response == null) {
                response = new JSONObject(json.getString(parameter));
            }
            return response;
        }
        return null;
    }

    @NonNull
    public static List<String> optJSONStringList(@NonNull JSONObject json, @NonNull String parameter) throws JSONException {
        List<String> strings = new ArrayList<>();
        if (isParameterValid(json, parameter)) {
            JSONArray array = json.optJSONArray(parameter);
            if (array != null) {
                for (int i = 0; i < array.length(); i++) {
                    strings.add(array.getString(i));
                }
            } else {
                strings.add(json.getString(parameter));
            }
        }
        return strings;
    }

    private static boolean isParameterValid(@NonNull JSONObject json, @NonNull String parameter) {
        return json.has(parameter) && !json.isNull(parameter);
    }
}
