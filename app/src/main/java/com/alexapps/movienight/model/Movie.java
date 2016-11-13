package com.alexapps.movienight.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.DecimalFormat;

public class Movie implements Parcelable {

    private String mTitle;
    private String mOverview;
    private Genre[] mGenres;
    private double mAverageVote;
    private String mReleaseDate;
    public final static String GENRES = "GENRES";

    public Movie () {}

    public Movie (String title, String overview, Genre[] genres, double averageVote, String releaseDate) {
        mTitle = title;
        mOverview = overview;
        mGenres = genres;
        mAverageVote = averageVote;
        mReleaseDate = releaseDate;
    }

    public String convertGenresToString () {
        String genreNames = "";
        for (Genre genre: mGenres) {
            if (genreNames.equals("")) {
                genreNames = genre.getName();
            } else {
                genreNames = genreNames + ", " + genre.getName();
            }
        }
        if (mGenres.length ==1) {return "Genre: " + genreNames;}
        else {return "Genres: "+ genreNames;}
    }

    public String getMoviePlusYear () {
        return getTitle() + " (" + getReleaseYearAsString() + ")";
    }

    public String getVoteAsString () {
        return new DecimalFormat("#0.0").format(mAverageVote);
            }

    public String getReleaseYearAsString () {
        return mReleaseDate.substring(0,4);
            }



    public String getReleaseDate() {
        return mReleaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        mReleaseDate = releaseDate;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getOverview() {
        return mOverview;
    }

    public void setOverview(String overview) {
        mOverview = overview;
    }

    public Genre[] getGenres() {
        return mGenres;
    }

    public void setGenres(Genre[] genres) {
        mGenres = genres;
    }

    public double getAverageVote() {
        return mAverageVote;
    }

    public void setAverageVote(double averageVote) {
        mAverageVote = averageVote;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mTitle);
        dest.writeString(this.mOverview);
        dest.writeTypedArray(this.mGenres, flags);
        dest.writeDouble(this.mAverageVote);
        dest.writeString(this.mReleaseDate);
    }

    protected Movie(Parcel in) {
        this.mTitle = in.readString();
        this.mOverview = in.readString();
        this.mGenres = in.createTypedArray(Genre.CREATOR);
        this.mAverageVote = in.readDouble();
        this.mReleaseDate = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
