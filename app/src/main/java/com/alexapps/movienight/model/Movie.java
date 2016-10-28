package com.alexapps.movienight.model;

public class Movie {

    private String mTitle;
    private String mOverview;
    private int [] mGenreId;
    private int mAverageVote;
    private String mReleaseDate;

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

    public int[] getGenreId() {
        return mGenreId;
    }

    public void setGenreId(int[] genreId) {
        mGenreId = genreId;
    }

    public int getAverageVote() {
        return mAverageVote;
    }

    public void setAverageVote(int averageVote) {
        mAverageVote = averageVote;
    }
}
