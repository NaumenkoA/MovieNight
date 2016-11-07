package com.alexapps.movienight.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alexapps.movienight.R;
import com.alexapps.movienight.model.Genre;
import com.alexapps.movienight.model.Movie;
import com.alexapps.movienight.model.Years;

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

public class MainActivity extends AppCompatActivity {
    public final static String TAG = MainActivity.class.getSimpleName();
    public final static String GENRE_ARRAY = "GENRE_ARRAY";
    public final static String IS_MOVIE = "IS_MOVIE";
    public final static String TITLE = "TITLE";
    public final static String MESSAGE = "MESSAGE";
    public final static String FLAG = "FLAG";
    public final static String MOVIES = "MOVIES";
    public final static String SHOWS = "SHOWS";
    public final static String BUNDLE = "BUNDLE";
    static final int PICK_CONTACT_REQUEST = 1;
    @BindView(R.id.genres1InfoTextView)
    TextView mMovieGenresInfoTextView;
    @BindView(R.id.genres2InfoTextView)
    TextView mTVGenresInfoTextView;
    @BindView(R.id.moviesCheckBox)
    CheckBox mMoviesCheckBox;
    @BindView(R.id.yearFromSpinner)
    Spinner mYearFromSpinner;
    @BindView(R.id.yearToSpinner)
    Spinner mYearToSpinner;
    @BindView(R.id.showsCheckBox)
    CheckBox mTVCheckBox;
    @BindView(R.id.ratingEditText)
    EditText mRatingEditText;
    @BindView(R.id.votesEditText)
    EditText mVotesEditText;
    @BindView(R.id.selectedMovieGenresTextView)
    TextView mSelectedMovieGenres;
    @BindView(R.id.selectedTVGenresTextView)
    TextView mSelectedTVGenres;
    @BindView(R.id.submitButton)
    Button mSubmitButton;
    @BindView(R.id.selectMovieGenresButton)
    Button mSelectMovieGenresButton;
    @BindView(R.id.selectShowsGenreButton)
    Button mSelectShowsGenresButton;
    @BindView(R.id.moviesSortOrderSpinner)
    Spinner mMoviesSortOrderSpinner;
    @BindView(R.id.moviesAscDescSpinner)
    Spinner mMoviesAscDescSpinner;
    @BindView(R.id.showsSortOrderSpinner)
    Spinner mShowsSortOrderSpinner;
    @BindView(R.id.showsAscDescSpinner)
    Spinner mShowsAscDescSpinner;
    @BindView(R.id.sortMoviesInfoTextView)
    TextView mSortMoviesInfoTextView;
    @BindView(R.id.sortShowsInfoTextView)
    TextView mSortShowsInfoTextView;
    Genre[] mMovieGenres;
    Genre[] mTVShowGenres;
    Movie[] mMoviesByRequest;
    Movie[] mShowsByRequest;
    Bundle mBundle;
    Years years = new Years();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mBundle = new Bundle();
        setSpinnerAdapter(years.getYears(), mYearFromSpinner);
        setSpinnerAdapter(years.getYears(), mYearToSpinner);
        setSpinnerAdapter(getResources().getStringArray(R.array.array_movies_sort_by), mMoviesSortOrderSpinner);
        setSpinnerAdapter(getResources().getStringArray(R.array.array_view_results), mMoviesAscDescSpinner);
        setSpinnerAdapter(getResources().getStringArray(R.array.array_shows_sort_by), mShowsSortOrderSpinner);
        setSpinnerAdapter(getResources().getStringArray(R.array.array_view_results), mShowsAscDescSpinner);

        String urlMovieGenresList = "https://api.themoviedb.org/3/genre/movie/list?api_key=529915439cfafbc35e2bed2706c2eebd";
        String urlTVGenresList = "https://api.themoviedb.org/3/genre/tv/list?api_key=529915439cfafbc35e2bed2706c2eebd";

        webRequest(urlMovieGenresList, "GET_MOVIE_GENRES", true);
        webRequest(urlTVGenresList, "GET_TV_GENRES", false);
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = createUserRequest(true);
                Log.v("User request:", url);
            }
        });
        mSelectMovieGenresButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGenreActivity(true);
            }
        });
        mSelectShowsGenresButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGenreActivity(false);
            }
        });

        mMoviesCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeGenreSelection(true);
            }
        });
        mTVCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeGenreSelection(false);
            }
        });

        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchMoviesByRequest()) {
                int flag = getWhatWasSearched();
                mBundle.putInt(FLAG,flag);
                mBundle.putParcelableArray (MOVIES, mMoviesByRequest);
                mBundle.putParcelableArray(SHOWS, mShowsByRequest);
                Intent intent = new Intent(MainActivity.this, MovieListActivity.class);
                    intent.putExtra(BUNDLE, mBundle);
                    startActivity(intent);
                    }
            }
        });

    }

    private int getWhatWasSearched() {
        if (mMoviesCheckBox.isChecked() && mTVCheckBox.isChecked()) return 2;
        if (mMoviesCheckBox.isChecked()) return 0;
        if (mTVCheckBox.isChecked()) return 1;
        return 0;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_CONTACT_REQUEST) {
            if (resultCode == RESULT_OK) {
                Parcelable[] parcelables = data.getParcelableArrayExtra(GENRE_ARRAY);
                Genre[] genres = Arrays.copyOf(parcelables, parcelables.length, Genre[].class);
                boolean isMovie = data.getBooleanExtra(IS_MOVIE, true);
                if (isMovie) {
                    mMovieGenres = genres;
                } else {
                    mTVShowGenres = genres;
                }
                refreshGenreTextView(isMovie);
            }
        }
    }

    private void refreshGenreTextView(boolean isMovie) {
        if (isMovie) {
            showSelectedGenres(mMovieGenres, true);
        } else {
            showSelectedGenres(mTVShowGenres, false);
        }
    }

    private void showSelectedGenres(Genre[] genres, boolean isMovie) {
        String selectedGenres = "";
        boolean allGenres = true;
        int count = 0;
        for (Genre genre : genres) {
            if (genre.isSelected()) {
                count++;
                if (count >= 4) {
                    selectedGenres = selectedGenres +
                            ", ...";
                    break;
                }
                if (selectedGenres.equals("")) {
                    selectedGenres = genre.getName();
                } else {
                    selectedGenres = selectedGenres +
                            ", " + genre.getName();
                }
            } else {
                allGenres = false;
            }
        }
        if (allGenres) selectedGenres = getResources().getString(R.string.all_genres);
        if (isMovie) mSelectedMovieGenres.setText(selectedGenres);
        else mSelectedTVGenres.setText(selectedGenres);
    }


    private void changeGenreSelection(boolean isMovie) {
        if (isMovie) {
            revertStates(mMoviesCheckBox, mMovieGenresInfoTextView, mSelectedMovieGenres, mSelectMovieGenresButton,
                    mSortMoviesInfoTextView, mMoviesSortOrderSpinner, mMoviesAscDescSpinner);
        } else {
            revertStates(mTVCheckBox, mTVGenresInfoTextView, mSelectedTVGenres, mSelectShowsGenresButton,
                    mSortShowsInfoTextView, mShowsSortOrderSpinner, mShowsAscDescSpinner);
        }
    }

    private void revertStates(CheckBox checkbox, TextView textView1, TextView textView2,
                              Button button, TextView textView3, Spinner spinner1, Spinner spinner2) {
        button.setEnabled(checkbox.isChecked());
        spinner1.setEnabled(checkbox.isChecked());
        spinner2.setEnabled(checkbox.isChecked());
        if (checkbox.isChecked()) {
            setTextColor(textView1, textView2, textView3, "#ccffffff");
        } else {
            setTextColor(textView1, textView2, textView3, "#848587");
        }
    }

    private void setTextColor(TextView textView1, TextView textView2, TextView textView3, String color) {
        textView1.setTextColor(Color.parseColor(color));
        textView2.setTextColor(Color.parseColor(color));
        textView3.setTextColor(Color.parseColor(color));
    }

    public void startGenreActivity(boolean isMovie) {
        if (mMovieGenres == null || mTVShowGenres == null) {
            alertInternetError();
            return;
        }
        Intent intent = new Intent(this, GenreActivity.class);
        intent.putExtra(IS_MOVIE, isMovie);
        if (isMovie) {
            intent.putExtra(GENRE_ARRAY, mMovieGenres);
        } else {
            intent.putExtra(GENRE_ARRAY, mTVShowGenres);
        }
        startActivityForResult(intent, PICK_CONTACT_REQUEST);
    }


    private Genre[] getGenresArray(String jsonData) {

        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONArray genresArray = jsonObject.getJSONArray("genres");
            Genre[] genreArray = new Genre[genresArray.length()];
            for (int i = 0; i < genresArray.length(); i++) {
                JSONObject object = genresArray.getJSONObject(i);
                genreArray[i] = new Genre(object.getString("name"), object.getInt("id"));
            }
            return genreArray;
        } catch (JSONException e) {
            Log.e(TAG, "JSON Exception: ");
        }
        Genre[] nullGenre = new Genre[0];
        return nullGenre;
    }

    private Movie[] getMoviesByRequest(String jsonData, boolean isMovie) {
        String title;
        String overview;
        String genres;
        Double vote;
        String releaseDate;

        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONArray moviesArray = jsonObject.getJSONArray("results");
            Movie[] movies = new Movie[moviesArray.length()];
            for (int i = 0; i < moviesArray.length(); i++) {
                JSONObject object = moviesArray.getJSONObject(i);
                overview = object.getString("overview");
                genres = getGenresFromJSON(object, isMovie);
                vote = object.getDouble("vote_average");
                if (isMovie) {
                    title = object.getString("title");
                    releaseDate = object.getString("release_date");
                } else {
                    title = object.getString("name");
                    releaseDate = object.getString("first_air_date");
                }
                movies[i] = new Movie(title, overview, genres, vote, releaseDate);
            }
            return movies;
        } catch (JSONException e) {
            Log.e(TAG, "JSON Exception: ");
        }
        Movie[] nullArray = new Movie[0];
        return nullArray;
    }

    private String getGenresFromJSON(JSONObject object, boolean isMovie) {
        int genreId;
        String genreName = "";
        try {
            JSONArray genresArray = object.getJSONArray("genre_ids");
            for (int i = 0; i < genresArray.length(); i++) {
                genreId = genresArray.optInt(i);
                if (genreName.equals("")) {
                    genreName = findGenreNameById(genreId, isMovie);
                } else {
                    genreName = genreName + ", " + findGenreNameById(genreId, isMovie);
                }
            }
            return genreName;
        } catch (JSONException e) {
            Log.e(TAG, "JSON Exception: ");
        }
        return "";
    }

    private String findGenreNameById(int id, boolean isMovie) {
        Genre[] genresArray;
        if (isMovie) {
            genresArray = mMovieGenres;
        } else {
            genresArray = mTVShowGenres;
        }

        for (Genre genre : genresArray) {
            if (genre.getId() == id) {
                return genre.getName();
            }
        }
        return "";
    }


    private void alertInternetError() {
        UserDialogFragment dialog = new UserDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TITLE, getResources().getString(R.string.network_error));
        bundle.putString(MESSAGE, getResources().getString(R.string.network_error_message));
        dialog.setArguments(bundle);
        dialog.show(getFragmentManager(), getString(R.string.no_internet_tag));
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

    private void alertError() {
        Bundle bundle = new Bundle();
        bundle.putString(TITLE, getResources().getString(R.string.error_title));
        bundle.putString(MESSAGE, getResources().getString(R.string.error_message));
        UserDialogFragment errorFragment = new UserDialogFragment();
        errorFragment.setArguments(bundle);
        errorFragment.show(getFragmentManager(), getString(R.string.error_dialog));
    }


    public String getApiKey() {
        return "api_key=529915439cfafbc35e2bed2706c2eebd";
    }

    public String getRatingThreshold() {
        if (mRatingEditText.getText().toString().equals("")) {
            return "&vote_average.gte=7.0";
        }
        return "&vote_average.gte=" + mRatingEditText.getText();
    }

    public String getNumberOfVotes() {
        if (mVotesEditText.getText().toString().equals("")) {
            return "&vote_count.gte=10";
        }
        return "&vote_count.gte=" + mVotesEditText.getText();
    }

    public String getReleaseYearFrom(boolean isMovie) {
        if (mYearFromSpinner.getSelectedItem().equals(mYearToSpinner.getSelectedItem())) {
            if (isMovie) {
                return "&primary_release_year=" + mYearFromSpinner.getSelectedItem();
            } else {
                return "&first_air_date_year=" + mYearFromSpinner.getSelectedItem();
            }
        } else if (isMovie) {
            return "&primary_release_date.gte=" + mYearFromSpinner.getSelectedItem();
        } else {
            return "&first_air_date.gte=" + mYearFromSpinner.getSelectedItem();
        }
    }

    public String getReleaseYearTo(boolean isMovie) {
        if (mYearFromSpinner.getSelectedItem().equals(mYearToSpinner.getSelectedItem())) {
            return "";
        }
        if (isMovie) {
            return "&primary_release_date.lte=" + mYearToSpinner.getSelectedItem();
        } else {
            return "&first_air_date.lte=" + mYearToSpinner.getSelectedItem();
        }
    }

    public String getSortParameter(boolean isMovie) {
        if (isMovie) {
            switch (mMoviesSortOrderSpinner.getSelectedItemPosition()) {
                case (0):
                    return "&sort_by=popularity";
                case (1):
                    return "&sort_by=primary_release_date";
                case (2):
                    return "&sort_by=revenue";
                case (3):
                    return "&sort_by=vote_average";
                case (4):
                    return "&sort_by=vote_count";
            }
        } else {
            switch (mShowsSortOrderSpinner.getSelectedItemPosition()) {
                case (0):
                    return "&sort_by=popularity";
                case (1):
                    return "&sort_by=first_air_date";
                case (2):
                    return "&sort_by=vote_average";
            }
        }
        return "";
    }

    public String getSortOrder(boolean isMovie) {
        Spinner spinner;
        if (isMovie) {
            spinner = mMoviesAscDescSpinner;
        } else {
            spinner = mShowsAscDescSpinner;
        }

        if (spinner.getSelectedItemPosition() == 0) {
            return ".desc";
        } else return ".asc";
    }


    public String createUserRequest(boolean isMovie) {
        String url = "";
        if (isMovie) {
            url = "https://api.themoviedb.org/3/discover/movie?";
        } else {
            url = "https://api.themoviedb.org/3/discover/tv?";
        }
        url = url +
                getApiKey() +
                getSortParameter(isMovie) +
                getSortOrder(isMovie) +
                getReleaseYearFrom(isMovie) +
                getReleaseYearTo(isMovie) +
                getNumberOfVotes() +
                getRatingThreshold() +
                getSelectedGenres(isMovie);
        Log.v("REQUEST URL CREATED:", url);
        return url;
    }

    private String getSelectedGenres(boolean isMovie) {
        String genresRequest = "";
        Boolean allSelected = true;
        Genre[] genres;
        if (isMovie) {
            genres = mMovieGenres;
        } else {
            genres = mTVShowGenres;
        }
        for (Genre genre : genres) {
            if (genre.isSelected()) {
                if (genresRequest == "") {
                    genresRequest = "&with_genres=" + genre.getId();
                } else {
                    genresRequest = genresRequest + "|" + genre.getId();
                }
            } else {
                allSelected = false;
            }
        }
        if (allSelected) return "";
        return genresRequest;
    }

    public boolean searchMoviesByRequest() {
        String userRequestMovies = "";
        String userRequestShows = "";
       if (mMovieGenres == null || mTVShowGenres == null) {
            alertInternetError();
            return false;
        }
        if (!mMoviesCheckBox.isChecked() && !mTVCheckBox.isChecked()) {
            Toast.makeText(this, "Select movies or TV shows to search", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (mMoviesCheckBox.isChecked()) {
            userRequestMovies = createUserRequest(true);
            Object result = webRequest(userRequestMovies, "USER_REQUEST", true).execute().get();
        }
        if (mTVCheckBox.isChecked()) {
            userRequestShows = createUserRequest(false);
            webRequest(userRequestShows, "USER_REQUEST", false);
        }
        return true;
    }


    public void setSpinnerAdapter(String[] array, Spinner spinner) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                array);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }


    public void webRequest(String url, final String tag, final boolean isMovie) {

        if (networkIsAvailable()) {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(url).build();
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
                        Log.v("Data request", resp);
                        if (response.isSuccessful()) {

                            if (tag.equals("GET_MOVIE_GENRES")) {
                                mMovieGenres = getGenresArray(resp);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mSelectedMovieGenres.setText(getResources().getString(R.string.all_genres));
                                    }
                                });
                            }
                            if (tag.equals("GET_TV_GENRES")) {
                                mTVShowGenres = getGenresArray(resp);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mSelectedTVGenres.setText(getResources().getString(R.string.all_genres));
                                    }
                                });
                            }
                            if (tag.equals("USER_REQUEST")) {
                                if (isMovie) {
                                    mMoviesByRequest = getMoviesByRequest(resp, true);
                                } else {
                                    mShowsByRequest = getMoviesByRequest(resp, false);
                                }

                                }


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
}
