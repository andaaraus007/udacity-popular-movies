package com.udacity.popularmovies.utils;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AppExecutors {
    private static final Object LOCK = new Object();
    private static final int MAX_NUM_THREADS = 3;
    private static AppExecutors sInstance;
    private final Executor mDiskIO;
    private final Executor mNetworkIO;

    private AppExecutors(Executor diskIO, Executor networkIO) {
        mDiskIO = diskIO;
        mNetworkIO = networkIO;
    }

    public static AppExecutors getInstance() {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new AppExecutors(
                        Executors.newSingleThreadExecutor(),
                        Executors.newFixedThreadPool(MAX_NUM_THREADS)
                );
            }
        }
        return sInstance;
    }

    public Executor getDiskIO() {
        return mDiskIO;
    }

    public Executor getNetworkIO() {
        return mNetworkIO;
    }
}
