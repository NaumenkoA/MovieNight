package com.alexapps.movienight.adapters;

import android.preference.TwoStatePreference;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alexapps.movienight.R;
import com.alexapps.movienight.model.Movie;

import org.w3c.dom.Text;

import java.util.zip.Inflater;

public class MovieAdapter extends RecyclerView.Adapter <MovieAdapter.MovieViewHolder> {

    private Movie[] mMovies;
    public MovieAdapter (Movie [] movies) {
        mMovies = movies;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.movie_list_item, parent, false);
        MovieViewHolder viewHolder = new MovieViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        holder.bindMovie(mMovies [position]);
    }

    @Override
    public int getItemCount() {
        return mMovies.length;
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {

        public TextView mTitle;
        public TextView mGenres;
        public TextView mRate;
        public TextView mReleaseYear;

        public MovieViewHolder(View itemView) {
            super(itemView);
            mTitle = (TextView) itemView.findViewById(R.id.titleTextView);
            mGenres = (TextView) itemView.findViewById(R.id.genreTextView);
            mRate = (TextView) itemView.findViewById(R.id.ratingTextView);
            mReleaseYear = (TextView) itemView.findViewById(R.id.yearTextView);
        }

        public void bindMovie (Movie movie) {
            mTitle.setText (movie.getTitle());
            mGenres.setText (movie.getGenres());
            mRate.setText(movie.getVoteAsString());
            mReleaseYear.setText(movie.getReleaseYearAsString());
        }
    }

}
