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
public class MoviePlayerActivity extends Activity implements
        IgnitedAsyncTaskContextHandler<Integer, String>{

    private static final String TAG = "Boomer_MoviePlayerActivity";


    private XBMCNotificationReceiver task;

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

        // Start the background task to handle player notifications.
        new XBMCNotificationReceiver().execute("192.168.1.14");

// try to obtain a reference to a task piped through from the previous
        // activity instance
        task = (XBMCNotificationReceiver) getLastNonConfigurationInstance();

        // if there was no previous instance, create the task anew
        if (task == null) {
            resetTask();
        }

        startPendingTask(null);

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


    @Override
    public Object onRetainNonConfigurationInstance() {
        // we leverage this method to "tunnel" the task object through to the next
        // incarnation of this activity in case of a configuration change
        return task;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // always disconnect the activity from the task here, in order to not risk
        // leaking a context reference
        task.disconnect();
    }

    public void resetTask() {
        task = new XBMCNotificationReceiver();
    }

    public void startPendingTask(View startButton) {
        // register this activity with the task
        task.connect(this);

        if (task.isPending()) {
            // task has not been started yet, start it
            task.execute("192.168.1.14");
        }
    }

    @Override
    public boolean onTaskStarted() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean onTaskProgress(Integer... progress) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean onTaskCompleted(String result) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean onTaskSuccess(String result) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean onTaskFailed(Exception error) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
