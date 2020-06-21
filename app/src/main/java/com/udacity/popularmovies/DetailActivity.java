package com.udacity.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;
import com.udacity.popularmovies.model.Movie;
import com.udacity.popularmovies.utils.ScreenUtils;

import static android.content.res.Configuration.ORIENTATION_PORTRAIT;

public class DetailActivity extends AppCompatActivity {
    private static final int PORTRAIT_WEIGHTED_SUM = 3;
    private static final int LANDSCAPE_WEIGHTED_SUM = 4;
    private static final int PORTRAIT_LAYOUT_WEIGHT = 2;
    private static final int LANDSCAPE_LAYOUT_WEIGHT = 3;

    LinearLayout mOuterWrapper;
    LinearLayout mInnerRight;
    ImageView mMoviePosterImageView;
    TextView mOriginalTitle;
    TextView mPlotSynopsis;
    TextView mUserRating;
    TextView mReleaseDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mMoviePosterImageView = findViewById(R.id.iv_detail_movie_poster);
        mOriginalTitle = findViewById(R.id.tv_original_title);
        mPlotSynopsis = findViewById(R.id.tv_plot_synopsis);
        mUserRating = findViewById(R.id.tv_user_rating);
        mReleaseDate = findViewById(R.id.tv_release_date);
        mOuterWrapper = findViewById(R.id.ll_outer_wrapper);
        mInnerRight = findViewById(R.id.ll_inner_right);

        // Start: Adjust layout depending on screen orientation
        ScreenUtils screenUtils = new ScreenUtils(this);
        int orientation = screenUtils.getOrientation();
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mInnerRight.getLayoutParams();

        if (orientation == ORIENTATION_PORTRAIT) {
            mOuterWrapper.setWeightSum(PORTRAIT_WEIGHTED_SUM);
            params.weight = PORTRAIT_LAYOUT_WEIGHT;
        } else {
            mOuterWrapper.setWeightSum(LANDSCAPE_WEIGHTED_SUM);
            params.weight = LANDSCAPE_LAYOUT_WEIGHT;
        }

        mInnerRight.setLayoutParams(params);
        // Stop: Adjust layout depending on screen orientation

        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra("MovieDetails")) {
                Movie movie = (Movie) intent.getSerializableExtra("MovieDetails");
                if (movie != null) {
                    Picasso.get()
                            .load(movie.getMoviePosterThumbnail())
                            .into(mMoviePosterImageView);
                    mOriginalTitle.setText(movie.getOriginalTitle());
                    mPlotSynopsis.setText(movie.getPlotSynopsis());
                    mUserRating.setText(movie.getUserRating());
                    mReleaseDate.setText(movie.getReleaseDate());
                }
            }
        }

    }
}
