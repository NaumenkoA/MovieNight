package com.alexapps.movienight.ui;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alexapps.movienight.R;
import com.alexapps.movienight.adapters.GenreAdapter;
import com.alexapps.movienight.model.Genre;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GenreActivity extends Activity {
    private Genre[] mGenre;
    private boolean mIsMovies;
    GenreAdapter mAdapter;
    @BindView(R.id.genreList) ListView mListView;
    @BindView(R.id.emptyListTextView) TextView mEmptyTextView;
    @BindView(R.id.selectGenresInfoTextView) TextView mSelectTextView;
    @BindView(R.id.selectAllCheckBox) CheckBox mSelectAllCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genre);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        mIsMovies = intent.getBooleanExtra(MainActivity.IS_MOVIE, true);
        Parcelable[] parcelables = intent.getParcelableArrayExtra(MainActivity.GENRE_ARRAY);
        mGenre = Arrays.copyOf(parcelables, parcelables.length, Genre[].class);
        mAdapter = new GenreAdapter(this, mGenre);
        mListView.setAdapter(mAdapter);
        mListView.setEmptyView(mEmptyTextView);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckBox genreCheckBox = (CheckBox) view.findViewById(R.id.genreItemCheckBox);
                genreCheckBox.setChecked(!genreCheckBox.isChecked());
                mGenre [position].setSelected(genreCheckBox.isChecked());
                if (mSelectAllCheckBox.isChecked() && !genreCheckBox.isChecked()) {
                    mSelectAllCheckBox.setChecked(false);
                }
                     }
        });
        if (mIsMovies) {
            mSelectTextView.setText(getResources().getString(R.string.select_movie_genres));
        } else {
            mSelectTextView.setText(R.string.select_show_genres);
        }
        setSelectAllCheckBox ();
                   }

    @OnClick (R.id.submitButton)
    void proceedGenresSelected () {
    boolean someGenreSelected = false;
        for (Genre genre:mGenre){
            if (genre.isSelected()) {
                someGenreSelected = true;
            }
                    }
        if (!someGenreSelected) {
            Toast.makeText(this, "Select some genres", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(MainActivity.IS_MOVIE, mIsMovies);
        intent.putExtra(MainActivity.GENRE_ARRAY, mGenre);
        setResult(RESULT_OK,intent);
    finish();
    }

     private void setSelectAllCheckBox() {
        boolean allSelected = true;
        for (Genre genre:mGenre) {
            if (!genre.isSelected()){
                allSelected = false;
            }
                    }
        mSelectAllCheckBox.setChecked(allSelected);
    }

    @OnClick (R.id.selectAllCheckBox)
    void selectAll () {
        for (Genre genre:mGenre) {
        genre.setSelected(mSelectAllCheckBox.isChecked());
            }
        mAdapter.notifyDataSetChanged();
        }
    }

