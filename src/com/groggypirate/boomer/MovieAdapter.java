package com.groggypirate.boomer;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import boomer.groggypirate.com.R;

import java.util.Comparator;

public class MovieAdapter extends ArrayAdapter<MovieInfo>
{

    Context context;
    int layoutResourceId;
    MovieInfo data[] = null;

    public MovieAdapter(Context context, int layoutResourceId, MovieInfo[] data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        MovieHolder holder;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new MovieHolder();
            holder.movieImg = (ImageView)row.findViewById(R.id.movieImg);
            holder.movieTitle = (TextView)row.findViewById(R.id.movieTitle);
            holder.movieYear = (TextView)row.findViewById(R.id.movieYear);
            holder.movieRuntime = (TextView)row.findViewById(R.id.movieDuration);
            holder.movieGenre = (TextView)row.findViewById(R.id.movieGenre);
            holder.movieCast = (TextView)row.findViewById(R.id.movieCast);

            row.setTag(holder);
        }
        else
        {
            holder = (MovieHolder)row.getTag();
        }

        MovieInfo movie = data[position];
        holder.movieImg.setImageDrawable(movie.icon);
        holder.movieTitle.setText(movie.title);
        holder.movieYear.setText(movie.year);
        holder.movieRuntime.setText(movie.runtime);
        holder.movieGenre.setText(movie.genre);
        holder.movieCast.setText(movie.cast);
        return row;
    }
    public void refreshArray(){
        this.sort(new Comparator<MovieInfo>() {
            public int compare(MovieInfo object1, MovieInfo object2) {
                return object1.title.compareTo(object2.title);
            }
        });
        this.notifyDataSetChanged();
    }


    static class MovieHolder
    {
        ImageView movieImg;
        TextView movieTitle;
        TextView movieYear;
        TextView movieRuntime;
        TextView movieGenre;
        TextView movieCast;
    }
}
