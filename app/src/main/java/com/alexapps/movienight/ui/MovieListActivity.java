package com.alexapps.movienight.ui;


import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
    private static final String TAG = MovieListActivity.class.getSimpleName();
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    private String mUserRequest;
    private Movie [] mMovies;
    private int mPage;
    private Genre[] mGenres;
    @BindView(R.id.moreMoviesButton)
    Button mMoreMoviesButton;
    @BindView(R.id.emptyTextView)
    TextView mEmptyTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        Parcelable [] parcelables = intent.getParcelableArrayExtra(MainActivity.MOVIE_GENRES);
        mGenres = Arrays.copyOf(parcelables, parcelables.length,Genre[].class);
        mUserRequest = intent.getStringExtra(MainActivity.USER_REQUEST);
        mPage = 1;
        webRequest(mUserRequest, mPage);
        mMoreMoviesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPage++;
                webRequest(mUserRequest, mPage);
            }
        });
        }

    private Movie[] getMoviesByRequest(String jsonData) {
        String title;
        String overview;
        Genre[] genres;
        Double vote;
        String releaseDate;
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONArray moviesArray = jsonObject.getJSONArray("results");
            Movie[] movies = new Movie[moviesArray.length()];
            for (int i = 0; i < moviesArray.length(); i++) {
                JSONObject object = moviesArray.getJSONObject(i);
                overview = object.getString("overview");
                genres = getGenresFromJSON(object);
                vote = object.getDouble("vote_average");
                title = object.getString("title");
                releaseDate = object.getString("release_date");
                movies[i] = new Movie(title, overview, genres, vote, releaseDate);
            }
            return movies;
        } catch (JSONException e) {
            Log.e(TAG, "JSON Exception: ");
        }
        return new Movie[0];
    }

    private Genre[] getGenresFromJSON(JSONObject object) {
        int genreId;
        String genreName;
        try {
            JSONArray genresArray = object.getJSONArray("genre_ids");
            Genre [] genres = new Genre[genresArray.length()];
            for (int i = 0; i < genresArray.length(); i++) {
                genreId = genresArray.optInt(i);
                genreName = findGenreNameById(genreId);
                genres [i] = new Genre(genreName, genreId);
            }
            return genres;
        } catch (JSONException e) {
            Log.e(TAG, "JSON Exception: ");
        }
        return new Genre [0];
    }

    private String findGenreNameById(int id) {
        for (Genre genre : mGenres) {
            if (genre.getId() == id) {
                return genre.getName();
            }
        }
        return "";
    }



    private void webRequest(final String url, int page) {

        if (networkIsAvailable()) {
            OkHttpClient client = new OkHttpClient();
            String urlWithPage = url + "&page=" + page;
            Log.v("User_request: ", urlWithPage);
            Request request = new Request.Builder().url(urlWithPage).build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {

                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e(TAG, "Error: ", e);
                }

                @Override
                public void onResponse(Call call, final Response response) throws IOException {
                    try {
                        String resp = response.body().string();
                        if (response.isSuccessful()) {
                        mMovies = getMoviesByRequest(resp);
                        final int availablePages = getNumberOfPages (resp);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                if (availablePages <= mPage) {
                                    mMoreMoviesButton.setEnabled(false);
                                }
                                if (mMovies.length == 0) {
                                    mEmptyTextView.setVisibility(View.VISIBLE);
                                } else {
                                    mEmptyTextView.setVisibility(View.INVISIBLE);
                                }
                                FragmentManager manager = getFragmentManager();
                                MovieAdapter adapter = new MovieAdapter(manager, mMovies);
                                mRecyclerView.setAdapter(adapter);
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MovieListActivity.this);
                                mRecyclerView.setLayoutManager(layoutManager);
                                mRecyclerView.setHasFixedSize(true);
                                }
                            });
                        } else {
                            alertError();
                        }
                    } catch (IOException e) {
                        Log.e("Error loading data", "Exception caught: ", e);
                    }
                }


            });

        } else {
            alertInternetError();
        }
    }

    private int getNumberOfPages(String jsonData) {
        int numberOfPages = 0;
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            numberOfPages = jsonObject.getInt("total_pages");
        } catch (JSONException e) {
            Log.e(TAG, "JSON Exception: ");
        }
        return numberOfPages;
    }

    private void alertInternetError() {
        UserDialogFragment dialog = new UserDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(MainActivity.TITLE, getResources().getString(R.string.network_error));
        bundle.putString(MainActivity.MESSAGE, getResources().getString(R.string.network_error_message));
        dialog.setArguments(bundle);
        dialog.show(getFragmentManager(), getString(R.string.no_internet_tag));
    }

    private void alertError() {
        Bundle bundle = new Bundle();
        bundle.putString(MainActivity.TITLE, getResources().getString(R.string.error_title));
        bundle.putString(MainActivity.MESSAGE, getResources().getString(R.string.error_message));
        UserDialogFragment errorFragment = new UserDialogFragment();
        errorFragment.setArguments(bundle);
        errorFragment.show(getFragmentManager(), getString(R.string.error_dialog));
    }
    private boolean networkIsAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo network = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (network != null && network.isConnected()) {
            isAvailable = true;
        }
        return isAvailable;
    }
    }

