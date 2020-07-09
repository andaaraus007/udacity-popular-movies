package com.udacity.popularmovies.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "movie")
public class MovieEntry {
    @PrimaryKey
    private final int id;
    private final String title;
    @ColumnInfo(name = "movie_poster")
    private final String moviePoster;
    @ColumnInfo(name = "plot_synopsis")
    private final String plotSynopsis;
    @ColumnInfo(name = "user_rating")
    private final String userRating;
    @ColumnInfo(name = "release_date")
    private final String releaseDate;

    public MovieEntry(int id,
                      String title,
                      String moviePoster,
                      String plotSynopsis,
                      String userRating,
                      String releaseDate) {
        this.id = id;
        this.title = title;
        this.moviePoster = moviePoster;
        this.plotSynopsis = plotSynopsis;
        this.userRating = userRating;
        this.releaseDate = releaseDate;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getMoviePoster() {
        return moviePoster;
    }

    public String getPlotSynopsis() {
        return plotSynopsis;
    }

    public String getUserRating() {
        return userRating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }
}
