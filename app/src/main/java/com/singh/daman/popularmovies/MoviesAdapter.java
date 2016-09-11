package com.singh.daman.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by daman on 11/9/16.
 */
public class MoviesAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<String> moviesposter, overview, date, title, vote;

    public MoviesAdapter(Context c,ArrayList<String> moviesposter, ArrayList<String> overview,
                         ArrayList<String>  date,ArrayList<String> title,ArrayList<String> vote ) {
        mContext = c;
        this.moviesposter = moviesposter;
        this.overview = overview;
        this.date = date;
        this.title = title;
        this.vote = vote;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return moviesposter.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View grid;
        ImageView imageView;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            grid = new View(mContext);
            grid = inflater.inflate(R.layout.movie, null);

        } else {
            grid = (View) convertView;
        }

        imageView = (ImageView)grid.findViewById(R.id.grid_image);
        Picasso.with(mContext).load(moviesposter.get(position)).placeholder(R.mipmap.ic_launcher).fit().into(imageView);

        return grid;
    }
}
