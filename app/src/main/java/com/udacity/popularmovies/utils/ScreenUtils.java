package com.udacity.popularmovies.utils;

import android.app.Activity;
import android.util.DisplayMetrics;

public final class ScreenUtils {
    private final int mOrientation;
    private final int mWidthInPixels;

    public ScreenUtils(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        mOrientation = activity.getResources().getConfiguration().orientation;
        mWidthInPixels = displayMetrics.widthPixels;
    }

    public int getOrientation() {
        return mOrientation;
    }

    public int getWidthInPixels() {
        return mWidthInPixels;
    }
}
