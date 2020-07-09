package com.udacity.popularmovies.utils;

import android.app.Activity;
import android.util.DisplayMetrics;

public final class ScreenUtils {
    private final int mWidthInPixels;

    public ScreenUtils(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        mWidthInPixels = displayMetrics.widthPixels;
    }

    public int getWidthInPixels() {
        return mWidthInPixels;
    }
}
