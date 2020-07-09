package com.udacity.popularmovies;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.udacity.popularmovies.adapter.ReviewAdapter;
import com.udacity.popularmovies.adapter.TrailerAdapter;
import com.udacity.popularmovies.database.AppDatabase;
import com.udacity.popularmovies.database.MovieEntry;
import com.udacity.popularmovies.interfaces.OnLoadMoreListener;
import com.udacity.popularmovies.model.Movie;
import com.udacity.popularmovies.model.Review;
import com.udacity.popularmovies.model.Trailer;
import com.udacity.popularmovies.utils.AppExecutors;
import com.udacity.popularmovies.utils.JsonUtils;
import com.udacity.popularmovies.utils.NetworkUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity
        implements TrailerAdapter.TrailerListItemClickListener {
    private static int pageNumber;

    private TextView mPlotSynopsis;
    private TextView mShowPlotButton;
    private TextView mShowTrailerButton;
    private TextView mShowReviewsButton;
    private TextView mPlotHeader;
    private TextView mReviewHeader;
    private TextView mTrailerHeader;
    private ImageView mFavoriteIcon;
    private RecyclerView mReviewContainer;
    private RecyclerView mTrailerContainer;
    private ReviewAdapter mReviewAdapter;
    private TrailerAdapter mTrailerAdapter;

    private Movie mMovie;

    private List<Trailer>  mTrailerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        initViews();
    }

    @Override
    public void onTrailerListItemClick(int itemClickedId) {
        Trailer trailer = mTrailerList.get(itemClickedId);

        Intent videoIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(trailer.getVideoUrl()));
        try {
            DetailActivity.this.startActivity(videoIntent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void onFavoriteButtonClicked(View view) {
        final MovieEntry movieEntry = new MovieEntry(mMovie.getId(),
                mMovie.getOriginalTitle(),
                mMovie.getMoviePosterThumbnail(),
                mMovie.getPlotSynopsis(),
                mMovie.getUserRating(),
                mMovie.getReleaseDate());

        final AppDatabase db = AppDatabase.getInstance(getApplicationContext());

        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                if (mMovie.getIsFavorite()) {
                    db.movieDao().deleteMovie(movieEntry);
                } else {
                    db.movieDao().insertMovie(movieEntry);
                }
                mMovie.setIsFavorite(!mMovie.getIsFavorite());
                // about Android Architecture Components
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setFavoriteIcon();
                    }
                });
            }
        });
    }

    public void showPlotSynopsis(View view) {
        hideTrailers();
        hideReviews();
        mShowPlotButton.setVisibility(View.GONE);
        mPlotHeader.setVisibility(View.VISIBLE);
        mPlotSynopsis.setVisibility(View.VISIBLE);
    }

    public void showTrailers(View view) {
        hidePlotSynopsis();
        hideReviews();
        mShowTrailerButton.setVisibility(View.GONE);
        mTrailerHeader.setVisibility(View.VISIBLE);
        mTrailerContainer.setVisibility(View.VISIBLE);
    }

    public void showReviews(View view) {
        hidePlotSynopsis();
        hideTrailers();
        mShowReviewsButton.setVisibility(View.GONE);
        mReviewHeader.setVisibility(View.VISIBLE);
        mReviewContainer.setVisibility(View.VISIBLE);
    }

    private void initViews() {
        ImageView moviePosterImageView = findViewById(R.id.iv_detail_movie_poster);
        TextView originalTitle = findViewById(R.id.tv_original_title);
        TextView userRating = findViewById(R.id.tv_user_rating);
        TextView releaseDate = findViewById(R.id.tv_release_date);

        mFavoriteIcon = findViewById(R.id.iv_favorite_icon);
        mPlotSynopsis = findViewById(R.id.tv_plot_synopsis);
        mShowPlotButton = findViewById(R.id.tv_show_plot);
        mShowReviewsButton = findViewById(R.id.tv_show_reviews);
        mShowTrailerButton = findViewById(R.id.tv_show_preview);
        mPlotHeader = findViewById(R.id.tv_plot_header);
        mReviewHeader = findViewById(R.id.tv_review_header);
        mTrailerHeader = findViewById(R.id.tv_video_header);
        mReviewContainer = findViewById(R.id.rv_reviews);
        mTrailerContainer = findViewById(R.id.rv_videos);

        // Setting up the Review List
        pageNumber = 1;
        List<Review> reviewList = new ArrayList<>();
        LinearLayoutManager reviewLayoutManager = new LinearLayoutManager(this);
        mReviewContainer.setLayoutManager(reviewLayoutManager);
        mReviewContainer.setNestedScrollingEnabled(true);
        mReviewContainer.setHasFixedSize(true);
        mReviewAdapter = new ReviewAdapter(reviewList, mReviewContainer);
        mReviewContainer.setAdapter(mReviewAdapter);

        // Setting up the Video List
        mTrailerList = new ArrayList<>();
        LinearLayoutManager trailerLayoutManager = new LinearLayoutManager(this);
        mTrailerContainer.setLayoutManager(trailerLayoutManager);
        mTrailerContainer.setNestedScrollingEnabled(true);
        mTrailerContainer.setHasFixedSize(true);
        mTrailerAdapter = new TrailerAdapter(mTrailerList, this);
        mTrailerContainer.setAdapter(mTrailerAdapter);

        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra(String.valueOf(R.string.app_name_detail))) {
                mMovie = (Movie) intent.getSerializableExtra(String.valueOf(R.string.app_name_detail));
                if (mMovie != null) {
                    Picasso.get()
                            .load(mMovie.getMoviePosterThumbnailUrl())
                            .into(moviePosterImageView);
                    setTitle(R.string.app_name_detail);
                    originalTitle.setText(mMovie.getOriginalTitle());
                    mPlotSynopsis.setText(mMovie.getPlotSynopsis());
                    userRating.setText(mMovie.getUserRating());
                    releaseDate.setText(mMovie.getReleaseDate());
                    loadReviewData(mMovie.getId(), pageNumber);
                    loadTrailerData(mMovie.getId());
                    setFavoriteIcon();

                    mReviewAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                        @Override
                        public void onLoadMore() {
                            if (++pageNumber < mReviewAdapter.getMaximumNumberOfPages()) {
                                loadReviewData(mMovie.getId(), pageNumber);
                            }
                        }
                    });
                }
            }
        }
    }

    private void setFavoriteIcon() {
        int resId = (mMovie.getIsFavorite()) ?
                R.mipmap.heart_favorite :
                R.mipmap.heart_unfavorite;
        mFavoriteIcon.setImageResource(resId);
    }

    private void hidePlotSynopsis() {
        mShowPlotButton.setVisibility(View.VISIBLE);
        mPlotHeader.setVisibility(View.GONE);
        mPlotSynopsis.setVisibility(View.GONE);
    }

    private void hideTrailers() {
        mShowTrailerButton.setVisibility(View.VISIBLE);
        mTrailerHeader.setVisibility(View.GONE);
        mTrailerContainer.setVisibility(View.GONE);
    }

    private void hideReviews() {
        mShowReviewsButton.setVisibility(View.VISIBLE);
        mReviewHeader.setVisibility(View.GONE);
        mReviewContainer.setVisibility(View.GONE);
    }

    private void loadReviewData(final int id, final int page) {
        AppExecutors.getInstance().getNetworkIO().execute(new Runnable() {
            @Override
            public void run() {
                URL reviewRequestUrl = NetworkUtils.buildUrlForReviewList(id, page);
                String jsonReviewListResponse = null;
                try {
                    jsonReviewListResponse = NetworkUtils.getResponseFromHttpUrl(reviewRequestUrl);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                final JsonUtils jsonUtils = new JsonUtils();
                final List<Review> reviewList = jsonUtils.parseReviewJson(jsonReviewListResponse);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mReviewAdapter.setMaximumNumberOfPages(jsonUtils.getMaximumNumberOfReviewPages());
                        mReviewAdapter.addReviewData(reviewList);
                        mReviewAdapter.setLoaded();
                    }
                });
            }
        });
    }

    private void loadTrailerData(final int id) {
        AppExecutors.getInstance().getNetworkIO().execute(new Runnable() {
            @Override
            public void run() {
                URL trailerRequestUrl = NetworkUtils.buildUrlForTrailerList(id);
                String jsonTrailerListResponse = null;
                try {
                    jsonTrailerListResponse = NetworkUtils.getResponseFromHttpUrl(trailerRequestUrl);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                final JsonUtils jsonUtils = new JsonUtils();
                final List<Trailer> trailerList = jsonUtils.parseTrailerJson(jsonTrailerListResponse);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mTrailerAdapter.addTrailerData(trailerList);
                    }
                });
            }
        });
    }
}
