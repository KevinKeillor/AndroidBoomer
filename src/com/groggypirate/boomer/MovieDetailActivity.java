package com.groggypirate.boomer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import boomer.groggypirate.com.R;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MovieDetailActivity extends Activity {

    MovieInfo m_Movie_Data;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail);

        ImageView view = (ImageView) findViewById(R.id.movieImg);
        String thumbFile = "movie_Thm" + getIntent().getSerializableExtra("current_movie_id");
        try {
            FileInputStream imgIn = openFileInput(thumbFile);
            Drawable img = Drawable.createFromResourceStream(getResources(), null, imgIn, thumbFile);
            imgIn.close();
            view.setImageDrawable(img);

        }catch (FileNotFoundException e){
           // TODO use default for missing img and attempt download
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        m_Movie_Data = new MovieInfo();
        IntentGetExtra();

        setText("current_movie_title", R.id.movieTitle);
        setText("current_movie_year", R.id.movieYear);
        setText("current_movie_runtime", R.id.movieDuration);
        setText("current_movie_rating", R.id.movieRating);
        setText("current_movie_cast", R.id.movieCast);
        setText("current_movie_director", R.id.movieDirector);
        setText("current_movie_genre", R.id.movieGenre);
        setText("current_movie_hd", R.id.movieHd);
        setText("current_movie_plot", R.id.moviePlot);
        setText("current_movie_tag", R.id.movieTag);
        setText("current_movie_writer", R.id.movieWriter);
    }

    void setText( String Extra, int Id){
        String currentMovieText= (String) getIntent().getSerializableExtra(Extra);
        //
        TextView view = (TextView) findViewById(Id);
        view.setText(currentMovieText);

    }

    public void PlayPauseClick(View view)  {
        // Start a movie player activity

        Intent moviePlayer = new Intent(view.getContext(), MoviePlayerActivity.class);
        IntentFillExtra(moviePlayer);
        view.getContext().startActivity(moviePlayer);
    }

    private void IntentGetExtra() {
        m_Movie_Data.id = (String) getIntent().getSerializableExtra("current_movie_id");
        m_Movie_Data.title = (String) getIntent().getSerializableExtra("current_movie_title");
        m_Movie_Data.year = (String) getIntent().getSerializableExtra("current_movie_year");
        m_Movie_Data.runtime = (String) getIntent().getSerializableExtra("current_movie_runtime");
        m_Movie_Data.rating = (Integer) getIntent().getSerializableExtra("current_movie_rating");
        m_Movie_Data.cast = (String) getIntent().getSerializableExtra("current_movie_cast");
        m_Movie_Data.director = (String) getIntent().getSerializableExtra("current_movie_director");
        m_Movie_Data.genre = (String) getIntent().getSerializableExtra("current_movie_genre");
        m_Movie_Data.hd = (Integer) getIntent().getSerializableExtra("current_movie_hd");
        m_Movie_Data.plot = (String) getIntent().getSerializableExtra("current_movie_plot");
        m_Movie_Data.tag = (String) getIntent().getSerializableExtra("current_movie_tag");
        m_Movie_Data.writer = (String) getIntent().getSerializableExtra("current_movie_writer");
    }

    private void IntentFillExtra(Intent movieDetail) {
        MovieInfo currentMovie = m_Movie_Data;
        movieDetail.putExtra("current_movie_id", currentMovie.id);
        movieDetail.putExtra("current_movie_title", currentMovie.title);
        movieDetail.putExtra("current_movie_year", currentMovie.year);
        movieDetail.putExtra("current_movie_runtime", currentMovie.runtime);
        movieDetail.putExtra("current_movie_rating", currentMovie.rating);
        movieDetail.putExtra("current_movie_cast", currentMovie.cast);
        movieDetail.putExtra("current_movie_director", currentMovie.director);
        movieDetail.putExtra("current_movie_genre", currentMovie.genre);
        movieDetail.putExtra("current_movie_hd", currentMovie.hd);
        movieDetail.putExtra("current_movie_plot", currentMovie.plot);
        movieDetail.putExtra("current_movie_tag", currentMovie.tag);
        movieDetail.putExtra("current_movie_writer", currentMovie.writer);
    }

}
