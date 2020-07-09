package com.udacity.popularmovies.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.udacity.popularmovies.R;
import com.udacity.popularmovies.interfaces.OnLoadMoreListener;
import com.udacity.popularmovies.model.Review;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>{
    private static final int visibleThreshold = 10;

    private final List<Review> mReviewList;
    private int mLastVisibleItem;
    private int mTotalItemCount;
    private int mMaximumNumberOfPages;
    private boolean mIsLoading;
    private OnLoadMoreListener mOnLoadMoreListener;

    public ReviewAdapter(List<Review> reviewList, RecyclerView recyclerView) {
        mReviewList = reviewList;

        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            final LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
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

    @NonNull
    @Override
    public ReviewAdapter.ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reviews_list_item, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter.ReviewViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return (null == mReviewList) ? 0 : mReviewList.size();
    }

    public void setLoaded() {
        mIsLoading = false;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        mOnLoadMoreListener = onLoadMoreListener;
    }

    public void addReviewData(List<Review> reviews) {
        mReviewList.addAll(reviews);
        notifyDataSetChanged();
    }

    public void setMaximumNumberOfPages(int maximumNumberOfPages) {
        mMaximumNumberOfPages = maximumNumberOfPages;
    }

    public int getMaximumNumberOfPages() {
        return mMaximumNumberOfPages;
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder {

        private final TextView mAuthorTextView;
        private final TextView mContentTextView;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            mAuthorTextView = itemView.findViewById(R.id.tv_author_name);
            mContentTextView = itemView.findViewById(R.id.tv_review);
        }

        public void bind(int listIndex) {
            if (listIndex <= mReviewList.size()) {
                Review review = mReviewList.get(listIndex);
                mAuthorTextView.setText(review.getAuthor());
                mContentTextView.setText(review.getContent());
            }
        }
    }
}
