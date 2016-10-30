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

import com.alexapps.movienight.R;
import com.alexapps.movienight.model.Genre;
import com.alexapps.movienight.model.Movie;
import com.alexapps.movienight.model.Years;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    public final static String TAG = MainActivity.class.getSimpleName();
    public final static String GENRE_ARRAY = "GENRE_ARRAY";
    public final static String IS_MOVIE = "IS_MOVIE";
    static final int PICK_CONTACT_REQUEST = 1;
    @BindView(R.id.genres1InfoTextView) TextView mMovieGenresInfoTextView;
    @BindView(R.id.genres2InfoTextView) TextView mTVGenresInfoTextView;
    @BindView(R.id.sortTypeSpinner) Spinner mSortTypeSpinner;
    @BindView(R.id.moviesCheckBox) CheckBox mMoviesCheckBox;
    @BindView(R.id.SortAscDescSpinner) Spinner mAscDescSpinner;
    @BindView(R.id.yearFromSpinner) Spinner mYearFromSpinner;
    @BindView(R.id.yearToSpinner) Spinner mYearToSpinner;
    @BindView(R.id.showsCheckBox) CheckBox mTVCheckBox;
    @BindView(R.id.ratingEditText) EditText mRatingEditText;
    @BindView(R.id.votesEditText) EditText mVotesEditText;
    @BindView(R.id.selectedMovieGenresTextView) TextView mSelectedMovieGenres;
    @BindView(R.id.selectedTVGenresTextView) TextView mSelectedTVGenres;
    @BindView(R.id.submitButton) Button mSubmitButton;
    @BindView(R.id.selectMovieGenresButton)  Button mSelectMovieGenresButton;
    @BindView(R.id.selectShowsGenreButton) Button mSelectShowsGenresButton;
    Genre [] mMovieGenres;
    Genre [] mTVShowGenres;
    Years years = new Years();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(
                this,
                R.array.array_sort_by,
                android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSortTypeSpinner.setAdapter(adapter1);

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(
                this,
                R.array.array_view_results,
                android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mAscDescSpinner.setAdapter(adapter2);

        ArrayAdapter <String> adapter3 = new ArrayAdapter <>(
                this,
                android.R.layout.simple_spinner_item,
                years.getYears());
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mYearFromSpinner.setAdapter(adapter3);
        mYearToSpinner.setAdapter(adapter3);

        String urlMovieGenresList = "https://api.themoviedb.org/3/genre/movie/list?api_key=529915439cfafbc35e2bed2706c2eebd";
        String urlTVGenresList = "https://api.themoviedb.org/3/genre/tv/list?api_key=529915439cfafbc35e2bed2706c2eebd";

        webRequest(urlMovieGenresList, "GET_MOVIE_GENRES");
        webRequest(urlTVGenresList, "GET_TV_GENRES");
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = createUserRequest(true);
                Log.v ("User request:", url);
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
            changeGenreSelection (true);
            }
        });
        mTVCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeGenreSelection(false);
            }
        });

        }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_CONTACT_REQUEST) {
            if (resultCode == RESULT_OK) {
                Parcelable[] parcelables = data.getParcelableArrayExtra(GENRE_ARRAY);
                Genre [] genres = Arrays.copyOf (parcelables, parcelables.length, Genre[].class);
                boolean isMovie = data.getBooleanExtra(IS_MOVIE, true);
                if (isMovie){
                    mMovieGenres = genres;
                } else {
                    mTVShowGenres = genres;
                }
                refreshGenreTextView (isMovie);
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

    private void showSelectedGenres (Genre[] genres, boolean isMovie) {
        String selectedGenres = "";
        boolean allGenres = true;
        for (Genre genre : genres) {
            if (genre.isSelected()) {
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
        if (isMovie)  mSelectedMovieGenres.setText(selectedGenres);
        else mSelectedTVGenres.setText(selectedGenres);
            }


    private void changeGenreSelection(boolean isMovie) {
        if (isMovie) {
            revertStates(mMoviesCheckBox, mMovieGenresInfoTextView, mSelectedMovieGenres, mSelectMovieGenresButton);
        } else {
            revertStates(mTVCheckBox, mTVGenresInfoTextView, mSelectedTVGenres, mSelectShowsGenresButton);
        }
    }

    private void revertStates (CheckBox checkbox, TextView textView1, TextView textView2, Button button) {
        button.setEnabled(checkbox.isChecked());
        if (checkbox.isChecked()) {
            setGenreTextColor(textView1, textView2, "#ccffffff");
        } else
        {
            setGenreTextColor(textView1, textView2, "#848587");
        }
        }

    private void setGenreTextColor(TextView textView1, TextView textView2, String color) {
        textView1.setTextColor(Color.parseColor(color));
        textView2.setTextColor(Color.parseColor(color));
    }

    public void startGenreActivity (boolean isMovie) {
    Intent intent = new Intent(this, GenreActivity.class);
    intent.putExtra (IS_MOVIE, isMovie);
        if (isMovie) {
            intent.putExtra(GENRE_ARRAY, mMovieGenres);
        } else
        {
            intent.putExtra (GENRE_ARRAY, mTVShowGenres);
        }
    startActivityForResult(intent, PICK_CONTACT_REQUEST);
    }


    private Genre [] getGenresArray (String jsonData) {

        try {
                JSONObject jsonObject = new JSONObject(jsonData);
                JSONArray genresArray = jsonObject.getJSONArray("genres");
                Genre[] genreArray =  new Genre[genresArray.length()];
                for (int i=0; i < genresArray.length(); i++) {
                JSONObject object = genresArray.getJSONObject(i);
                genreArray[i] = new Genre(object.getString("name"), object.getInt("id"));
                     }
            return genreArray;
        } catch (JSONException e) {
            Log.e(TAG, "JSON Exception: ");
        }
        Genre[] nullGenre =  new Genre[0];;
        return nullGenre;
    }

    private void alertInternetError() {
        AlertNoInternetFragment dialog = new AlertNoInternetFragment();
        dialog.show(getFragmentManager(),getString(R.string.no_internet_tag));
    }

    private boolean networkIsAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo network = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (network !=null && network.isConnected()) {
            isAvailable = true;
        }
        return isAvailable;
    }

    private void alertError() {
    AlertDialogFragment dialog = new AlertDialogFragment();
    dialog.show(getFragmentManager(),getString(R.string.error_dialog));
    }

    public String getApiKey () {
        return "api_key=529915439cfafbc35e2bed2706c2eebd";
    }

    public String getRatingThreshold () {
        if (mRatingEditText.getText().toString().equals("")) {
            return "&vote_average.gte=7.0";
        }
        return "&vote_average.gte=" + mRatingEditText.getText();
}
    public String getNumberOfVotes () {
        if (mVotesEditText.getText().toString().equals("")) {
            return "&vote_count.gte=1000";
        }
        return "&vote_count.gte=" + mVotesEditText.getText();
    }

    public String getReleaseYearFrom (boolean isMovie) {
        if (isMovie) {
            return "&primary_release_date.gte=" + mYearFromSpinner.getSelectedItem();
        } else {
            return "&first_air_date.gte=" + mYearFromSpinner.getSelectedItem();
        }
    }

    public String getReleaseYearTo (boolean isMovie) {
        if (isMovie) {
            return "&primary_release_date.lte=" + mYearToSpinner.getSelectedItem();
        } else {
            return "&first_air_date.lte=" + mYearToSpinner.getSelectedItem();
        }
    }

    public String getSortParameter (boolean isMovie) {
        if (isMovie) {
            switch (mSortTypeSpinner.getSelectedItemPosition()) {
                case (0): return "&sort_by=popularity";
                case (1): return "&sort_by=primary_release_date";
                case (2): return "&sort_by=revenue";
                case (3): return "&sort_by=vote_average";
                case (4): return "&sort_by=vote_count";
            }
        } else {
            switch (mSortTypeSpinner.getSelectedItemPosition()) {
                case (0): return "&sort_by=popularity";
                case (1): return "&sort_by=first_air_date";
                case (2): return "";
                case (3): return "&sort_by=vote_average";
                case (4): return "";
                   }
        }
        return "";
    }

    public String getSortOrder (boolean isMovie) {
        if (getSortParameter(isMovie).equals("")) {return "";}
      if (mAscDescSpinner.getSelectedItemPosition() == 0) { return ".desc";}
        else return ".asc";
    }

    public String createUserRequest (boolean isMovie) {
        String url;
        if (isMovie) {
        url  = "https://api.themoviedb.org/3/discover/movie?";
        }
        else {
        url = "https://api.themoviedb.org/3/discover/tv?";
        }
        return url +
                getApiKey()+
                getSortParameter(isMovie)+
                getSortOrder(isMovie)+
                getReleaseYearFrom(isMovie)+
                getReleaseYearTo(isMovie)+
                getNumberOfVotes()+
                getRatingThreshold();
               //getGenres(index) +
        }

    public Movie[] getMovieArrayByRequest(boolean isMovie) {
      String jsonData =  createUserRequest(isMovie);
        return null;


    }


    public void webRequest (String url, final String tag) {
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
            public void onResponse(Call call, Response response) throws IOException {
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

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                }
                            });
                        }

                         }
                    else {
                        alertError();
                    }
                } catch (IOException e) {
                    Log.e("Error loading data", "Exeption caught: ", e);
                }
            }
        });

    } else
    {
        alertInternetError();
    }

}

       }
