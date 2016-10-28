package com.alexapps.movienight.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

import com.alexapps.movienight.R;
import com.alexapps.movienight.model.Genre;

public class GenreAdapter extends BaseAdapter {
    private Context mContext;
    private Genre[] mGenre;

    public GenreAdapter (Context context, Genre [] genre) {
        mContext = context;
        mGenre = genre;
    }

    @Override
    public int getCount() {
        return mGenre.length;
    }

    @Override
    public Object getItem(int position) {
        return mGenre [position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.genre_list_item, null);
            holder.genreCheckbox = (CheckBox) convertView.findViewById(R.id.genreItemCheckBox);
            convertView.setTag (holder);
        } else {
        holder = (ViewHolder) convertView.getTag();
        }
        holder.genreCheckbox.setText (mGenre[position].getName());
        return  convertView;
}
    public static class ViewHolder {
        CheckBox genreCheckbox;
    }
}
