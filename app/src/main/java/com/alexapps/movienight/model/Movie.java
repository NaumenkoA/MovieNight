package com.alexapps.movienight.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.DecimalFormat;
import java.util.Arrays;

public class Movie implements Parcelable {

    private String mTitle;
    private String mOverview;
    private String mGenres;
    private double mAverageVote;
    private String mReleaseDate;
    public final static String GENRES = "GENRES";

    public Movie () {}

    public Movie (String title, String overview, String genres, double averageVote, String releaseDate) {
        mTitle = title;
        mOverview = overview;
        mGenres = genres;
        mAverageVote = averageVote;
        mReleaseDate = releaseDate;
    }

    public String getVoteAsString () {
        return new DecimalFormat("#0.0").format(mAverageVote);
            }

    public String getReleaseYearAsString () {
        return mReleaseDate.substring(0,3);
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

    public String getGenres() {
        return mGenres;
    }

    public void setGenres(String genres) {
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
        dest.writeString(this.mGenres);
        dest.writeDouble(this.mAverageVote);
        dest.writeString(this.mReleaseDate);
    }

    protected Movie(Parcel in) {
        this.mTitle = in.readString();
        this.mOverview = in.readString();
        this.mGenres = in.readString();
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
