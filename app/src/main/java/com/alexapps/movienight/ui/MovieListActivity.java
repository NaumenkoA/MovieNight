package com.alexapps.movienight.ui;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.alexapps.movienight.R;
import com.alexapps.movienight.adapters.MovieAdapter;
import com.alexapps.movienight.model.Genre;
import com.alexapps.movienight.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MovieListActivity extends AppCompatActivity {
    public static final String USER_REQUEST_MOVIES = "USER_REQUEST_MOVIES";
    public static final String USER_REQUEST_SHOWS = "USER_REQUEST_SHOWS";
    public static final String TAG = MovieListActivity.class.getSimpleName();
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    private Movie [] mMovies;
    private Movie [] mShows;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        int flag = intent.getIntExtra(MainActivity.FLAG, 0);

        switch (flag) {
            case 0:
                getMoviesFromIntent(intent);
            case 1:
                getShowsFromIntent(intent);
            case 2:
                getMoviesFromIntent(intent);
                getShowsFromIntent(intent);
        }

        if (mMovies != null) {
            MovieAdapter adapter = new MovieAdapter(mMovies);
            mRecyclerView.setAdapter(adapter);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            mRecyclerView.setLayoutManager(layoutManager);
            mRecyclerView.setHasFixedSize(true);
        }
    }

    private void getShowsFromIntent(Intent intent) {
        Parcelable[] parcelables;
        parcelables = intent.getParcelableArrayExtra(MainActivity.SHOWS);
        if (parcelables !=null) mShows = Arrays.copyOf(parcelables, parcelables.length, Movie[].class);
    }

    private void getMoviesFromIntent(Intent intent) {
        Parcelable[] parcelables;
        parcelables = intent.getParcelableArrayExtra(MainActivity.MOVIES);
        if (parcelables != null) mMovies = Arrays.copyOf(parcelables, parcelables.length, Movie[].class);
    }

}


