package com.alexapps.movienight.ui;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import com.alexapps.movienight.R;
import com.alexapps.movienight.model.Genre;
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
    public final static String TITLE = "TITLE";
    public final static String MESSAGE = "MESSAGE";
    public final static String GET_GENRES = "GET_MOVIE_GENRES";
    public final static String USER_REQUEST = "USER_REQUEST";
    static final int PICK_CONTACT_REQUEST = 1;
    public static final String MOVIE_GENRES = "MOVIE_GENRES";
    @BindView(R.id.yearFromSpinner) Spinner mYearFromSpinner;
    @BindView(R.id.yearToSpinner) Spinner mYearToSpinner;
    @BindView(R.id.ratingEditText) EditText mRatingEditText;
    @BindView(R.id.votesEditText) EditText mVotesEditText;
    @BindView(R.id.selectedMovieGenresTextView) TextView mSelectedMovieGenres;
    @BindView(R.id.submitButton) Button mSubmitButton;
    @BindView(R.id.selectMovieGenresButton) Button mSelectMovieGenresButton;
    @BindView(R.id.moviesSortOrderSpinner) Spinner mMoviesSortOrderSpinner;
    @BindView(R.id.moviesAscDescSpinner) Spinner mMoviesAscDescSpinner;
    Genre[] mMovieGenres;
    Years years = new Years();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSpinnerAdapter(years.getYears(), mYearFromSpinner);
        setSpinnerAdapter(years.getYears(), mYearToSpinner);
        setSpinnerAdapter(getResources().getStringArray(R.array.array_movies_sort_by), mMoviesSortOrderSpinner);
        setSpinnerAdapter(getResources().getStringArray(R.array.array_view_results), mMoviesAscDescSpinner);

        String urlMovieGenresList = "https://api.themoviedb.org/3/genre/movie/list?api_key=529915439cfafbc35e2bed2706c2eebd";

        webRequest(urlMovieGenresList, GET_GENRES);

        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            String request = createUserRequest();
            Intent intent = new Intent(MainActivity.this, MovieListActivity.class);
            intent.putExtra (MOVIE_GENRES, mMovieGenres);
            intent.putExtra(USER_REQUEST, request);
            startActivity(intent);
            }
        });
        mSelectMovieGenresButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGenreActivity();
            }
        });

        }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_CONTACT_REQUEST) {
            if (resultCode == RESULT_OK) {
                Parcelable[] parcelables = data.getParcelableArrayExtra(GENRE_ARRAY);
                mMovieGenres = Arrays.copyOf(parcelables, parcelables.length, Genre[].class);
                showSelectedGenres();
            }
        }
    }

    private void showSelectedGenres() {
        String selectedGenres = "";
        boolean allGenres = true;
        int count = 0;
        for (Genre genre : mMovieGenres) {
            if (genre.isSelected()) {
                count++;
                if (count > 4) {
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
        mSelectedMovieGenres.setText(selectedGenres);
            }

    public void startGenreActivity() {
        if (mMovieGenres == null) {
            alertInternetError();
            return;
        }
        Intent intent = new Intent(this, GenreActivity.class);
        intent.putExtra(GENRE_ARRAY, mMovieGenres);
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
        return new Genre[0];
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

    public String getReleaseYearFrom() {
        if (mYearFromSpinner.getSelectedItem().equals(mYearToSpinner.getSelectedItem())) {
             return "&primary_release_year=" + mYearFromSpinner.getSelectedItem();
             } else {
            return "&primary_release_date.gte=" + mYearFromSpinner.getSelectedItem();
        }
    }

    public String getReleaseYearTo() {
        if (mYearFromSpinner.getSelectedItem().equals(mYearToSpinner.getSelectedItem())) {
            return "";
        }
            return "&primary_release_date.lte=" + (Integer.parseInt(mYearToSpinner.getSelectedItem()+"")+1);
       }

    public String getSortParameter() {
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
        return "";
       }

    public String getSortOrder() {
        if (mMoviesAscDescSpinner.getSelectedItemPosition() == 0) {
            return ".desc";
        } else return ".asc";
    }


    public String createUserRequest() {
        String url = "https://api.themoviedb.org/3/discover/movie?";
        url = url +
                getApiKey() +
                getSortParameter() +
                getSortOrder() +
                getReleaseYearFrom() +
                getReleaseYearTo() +
                getNumberOfVotes() +
                getRatingThreshold() +
                getSelectedGenres();
        Log.v("REQUEST URL CREATED:", url);
        return url;
    }

    private String getSelectedGenres() {
        String genresRequest = "";
        Boolean allSelected = true;
        for (Genre genre : mMovieGenres) {
            if (genre.isSelected()) {
                if (genresRequest.equals("")) {
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

     public void setSpinnerAdapter(String[] array, Spinner spinner) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                array);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }


    private void webRequest(final String url, final String tag) {

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

                            if (tag.equals(GET_GENRES)) {
                                mMovieGenres = getGenresArray(resp);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mSelectedMovieGenres.setText(getResources().getString(R.string.all_genres));
                                    }
                                });
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
