package com.alexapps.movienight.model;

public class Movie {

    private String mTitle;
    private String mOverview;
    private int [] mGenreId;
    private double mAverageVote;
    private int mReleaseDate;

    public Movie (String title, String overview, int [] genreId, double averageVote, int releaseDate) {
        mTitle = title;
        mOverview = overview;
        mGenreId = genreId;
        mAverageVote = averageVote;
        mReleaseDate = releaseDate;
    }

    public int getReleaseDate() {
        return mReleaseDate;
    }

    public void setReleaseDate(int releaseDate) {
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

    public double getAverageVote() {
        return mAverageVote;
    }

    public void setAverageVote(double averageVote) {
        mAverageVote = averageVote;
    }
}
