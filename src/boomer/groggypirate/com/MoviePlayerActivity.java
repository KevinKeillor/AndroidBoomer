package boomer.groggypirate.com;


import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;


import android.view.View;
import android.widget.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * MoviePlayerActivity
 */
public class MoviePlayerActivity extends Activity {

    private static final String TAG = "Boomer_MoviePlayerActivity";

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_player);

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


}

    //Play button
    public void PlayPauseClick(View view) throws IOException, JSONException {
        XBMCJson json = new XBMCJson();
        JSONObject Params = new JSONObject();
        Params.put("playerid",1);
        String Command = "Player.PlayPause";
        json.writeCommand(Command, Params);
    }

    //Stop button
    public void StopClick(View view) throws IOException, JSONException {
        XBMCJson json = new XBMCJson();
        JSONObject Params = new JSONObject();
        Params.put("playerid",1);
        String Command = "Player.Stop";
        json.writeCommand(Command, Params);
    }

    //Play button
    public void ForwardClick(View view) throws IOException, JSONException {
        XBMCJson json = new XBMCJson();
        JSONObject Params = new JSONObject();
        Params.put("playerid",1);
        String Command = "Player.PlayPause";
        json.writeCommand(Command, Params);
    }

    //Play button
    public void ReverseClick(View view) throws IOException, JSONException {
        XBMCJson json = new XBMCJson();
        JSONObject Params = new JSONObject();
        Params.put("playerid",1);
        String Command = "Player.PlayPause";
        json.writeCommand(Command, Params);
    }

    void setText( String Extra, int Id){
        String currentMovieText= (String) getIntent().getSerializableExtra(Extra);
        //
        TextView view = (TextView) findViewById(Id);
        view.setText(currentMovieText);

    }
}
