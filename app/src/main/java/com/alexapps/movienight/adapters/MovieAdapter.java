package com.alexapps.movienight.adapters;

import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.preference.TwoStatePreference;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alexapps.movienight.R;
import com.alexapps.movienight.model.Movie;
import com.alexapps.movienight.ui.MainActivity;
import com.alexapps.movienight.ui.UserDialogFragment;

import org.w3c.dom.Text;

import java.util.zip.Inflater;

public class MovieAdapter extends RecyclerView.Adapter <MovieAdapter.MovieViewHolder> {

    private Movie[] mMovies;
    private FragmentManager mFragmentManager;
    public MovieAdapter (FragmentManager manager, Movie [] movies) {
        mFragmentManager = manager;
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

    public class MovieViewHolder extends RecyclerView.ViewHolder
    implements View.OnClickListener {

        private static final String MOVIE_OVERVIEW = "MOVIE OVERVIEW IN FRAGMENT";
        public TextView mTitle;
        public TextView mGenres;
        public TextView mRate;
        public Movie mMovie;

        public MovieViewHolder(View itemView) {
            super(itemView);
            mTitle = (TextView) itemView.findViewById(R.id.titleTextView);
            mGenres = (TextView) itemView.findViewById(R.id.genreTextView);
            mRate = (TextView) itemView.findViewById(R.id.ratingTextView);
            itemView.setOnClickListener(this);
            }

        public void bindMovie (Movie movie) {
            mTitle.setText (movie.getMoviePlusYear());
            mGenres.setText (movie.convertGenresToString());
            mRate.setText(movie.getVoteAsString());
            mMovie = movie;
            }

        @Override
        public void onClick(View v) {
            String title = mMovie.getTitle();
            String overview = mMovie.getOverview();
            UserDialogFragment dialog = new UserDialogFragment();
            Bundle bundle = new Bundle();
            bundle.putString(MainActivity.TITLE, title);
            bundle.putString(MainActivity.MESSAGE, overview);
            dialog.setArguments(bundle);
            dialog.show(mFragmentManager, MOVIE_OVERVIEW);
        }
    }

}
