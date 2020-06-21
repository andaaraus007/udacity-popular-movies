package com.udacity.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.udacity.popularmovies.interfaces.OnLoadMoreListener;
import com.udacity.popularmovies.model.Movie;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    private static final int visibleThreshold = 10;

    private final ListItemClickListener mOnClickListener;

    private final List<Movie> mMovieList;
    private int mLastVisibleItem;
    private int mTotalItemCount;
    private int mMaximumNumberOfPages;
    private boolean mIsLoading;
    private OnLoadMoreListener mOnLoadMoreListener;

    public interface ListItemClickListener {
        void onListItemClick(int itemClickedId);
    }

    public MovieAdapter(List<Movie> movieList, RecyclerView recyclerView, ListItemClickListener onClickListener) {
        mMovieList = movieList;
        mOnClickListener = onClickListener;

        if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
            final GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    mTotalItemCount = layoutManager.getItemCount();
                    mLastVisibleItem = layoutManager.findLastVisibleItemPosition();

                    if (!mIsLoading && mTotalItemCount <= (mLastVisibleItem + visibleThreshold)) {
                        if (mOnLoadMoreListener != null) {
                            mOnLoadMoreListener.onLoadMore();
                        }
                        mIsLoading = true;
                    }
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        final int VIEW_ITEM = 1;
        final int VIEW_LISTING = 0;

        return mMovieList.get(position) != null ? VIEW_ITEM : VIEW_LISTING;
    }

    @Override
    @NonNull
    public MovieViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.movie_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return (null == mMovieList) ? 0 : mMovieList.size();
    }

    public void setLoaded() {
        mIsLoading = false;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        mOnLoadMoreListener = onLoadMoreListener;
    }

    public void addMovieData(List<Movie> movies) {
        mMovieList.addAll(movies);
        notifyDataSetChanged();
    }

    public void clearMovieData() {
        mMovieList.clear();
        notifyDataSetChanged();
    }

    public void setMaximumNumberOfPages(int maximumNumberOfPages) {
        mMaximumNumberOfPages = maximumNumberOfPages;
    }

    public int getMaximumNumberOfPages() {
        return mMaximumNumberOfPages;
    }

    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ImageView movieItemImageView;

        public MovieViewHolder(View itemView) {
            super(itemView);
            movieItemImageView = itemView.findViewById(R.id.iv_movie_poster);
            movieItemImageView.setOnClickListener(this);
        }

        public void bind(int listIndex) {
            if (listIndex <= mMovieList.size()) {
                Movie movie = mMovieList.get(listIndex);

                Picasso.get()
                        .load(movie.getMoviePosterThumbnail())
                        .into(movieItemImageView);
            }
        }
        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(adapterPosition);
        }
    }
}
