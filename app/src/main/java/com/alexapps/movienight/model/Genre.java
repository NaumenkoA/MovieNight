package com.alexapps.movienight.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Genre implements Parcelable {
    private String mName;
    private int mId;

    public boolean isSelected() {
        return mIsSelected;
    }

    public void setSelected(boolean selected) {
        mIsSelected = selected;
    }

    private boolean mIsSelected;

    public Genre () {}

    public Genre (String name, int id) {
        mName = name;
        mId = id;
        mIsSelected = true;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mName);
        dest.writeInt(this.mId);
        dest.writeByte(this.mIsSelected ? (byte) 1 : (byte) 0);
    }

    protected Genre(Parcel in) {
        this.mName = in.readString();
        this.mId = in.readInt();
        this.mIsSelected = in.readByte() != 0;
    }

    public static final Creator<Genre> CREATOR = new Creator<Genre>() {
        @Override
        public Genre createFromParcel(Parcel source) {
            return new Genre(source);
        }

        @Override
        public Genre[] newArray(int size) {
            return new Genre[size];
        }
    };
}
