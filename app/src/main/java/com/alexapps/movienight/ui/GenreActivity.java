package com.alexapps.movienight.ui;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.alexapps.movienight.R;
import com.alexapps.movienight.adapters.GenreAdapter;
import com.alexapps.movienight.model.Genre;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GenreActivity extends ListActivity {
    private Genre[] mGenre;
    @BindView(R.id.selectGenresInfoTextView)
    TextView mSelectTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genre);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        boolean isMovies = intent.getBooleanExtra(MainActivity.IS_MOVIE, true);
        Parcelable[] parcelables = intent.getParcelableArrayExtra(MainActivity.GENRE_ARRAY);
        mGenre = Arrays.copyOf(parcelables, parcelables.length, Genre[].class);
        GenreAdapter adapter = new GenreAdapter(this, mGenre);
        setListAdapter(adapter);
        if (isMovies) {
            mSelectTextView.setText(getResources().getString(R.string.select_movie_genres));
        } else {
            mSelectTextView.setText(R.string.select_show_genres);
        }
    }
}
