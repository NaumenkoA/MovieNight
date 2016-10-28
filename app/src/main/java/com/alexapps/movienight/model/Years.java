package com.alexapps.movienight.model;

import java.util.ArrayList;
import java.util.List;

public class Years {
    private ArrayList<String> mYears;

    public ArrayList<String> getYears() {
        return mYears;
    }

    public void setYears(ArrayList<String> years) {
        mYears = years;
    }

    public Years () {
        mYears = new ArrayList<>();
        int count =0;
        for (int i=2016; i>1889; i--) {
            mYears.add (i+"");
        }
        }
}
