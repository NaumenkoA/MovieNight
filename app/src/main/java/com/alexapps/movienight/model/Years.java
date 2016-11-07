package com.alexapps.movienight.model;

import java.util.ArrayList;
import java.util.List;

public class Years {
    private String[] mYears;

    public String[] getYears() {
        return mYears;
    }

    public void setYears(String[] years) {
        mYears = years;
    }

    public Years () {
        mYears = new String[2016-1889];
        int count =0;
        for (int i=2016; i>1889; i--) {
            mYears[count] = i + "";
            count++;
        }
        }
}
