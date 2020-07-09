package com.udacity.popularmovies.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.udacity.popularmovies.R;
import com.udacity.popularmovies.model.Trailer;

import java.util.List;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {
    private final List<Trailer> mTrailerList;
    private final TrailerListItemClickListener mOnClickListener;

    public interface TrailerListItemClickListener {
        void onTrailerListItemClick(int itemClickedId);
    }

    public TrailerAdapter(List<Trailer> trailerList, TrailerListItemClickListener listener) {
        mTrailerList = trailerList;
        mOnClickListener = listener;
    }

    @NonNull
    @Override
    public TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trailer_list_item, parent, false);
        return new TrailerAdapter.TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return (null == mTrailerList) ? 0 : mTrailerList.size();
    }

    public void addTrailerData(List<Trailer> trailers) {
        mTrailerList.addAll(trailers);
        notifyDataSetChanged();
    }

    class TrailerViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
        private final TextView mTextView;

        public TrailerViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.tv_movie_video_name);
            itemView.setOnClickListener(this);
        }

        public void bind(int listIndex) {
            if (listIndex <= mTrailerList.size()) {
                Trailer trailer = mTrailerList.get(listIndex);
                mTextView.setText(trailer.getName());
            }
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onTrailerListItemClick(clickedPosition);
        }
    }
}
