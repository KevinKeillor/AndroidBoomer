package boomer.groggypirate.com;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MovieDetailActivity extends Activity {


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

}
