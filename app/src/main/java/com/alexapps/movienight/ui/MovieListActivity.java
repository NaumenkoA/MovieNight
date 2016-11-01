package com.alexapps.movienight.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.alexapps.movienight.R;

public class MovieListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);
    }

    /*public String getSortParameter (boolean isMovie) {
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
    }*/
}
