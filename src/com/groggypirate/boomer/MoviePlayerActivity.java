package com.groggypirate.boomer;


import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;


import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.*;
import boomer.groggypirate.com.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;


/**
 * MoviePlayerActivity
 */
public class MoviePlayerActivity extends Activity implements
        IgnitedAsyncTaskContextHandler<Integer, String> {

    private static final String TAG = "Boomer_MoviePlayerActivity";


    private XBMCNotificationReceiver task;
    // Timer for the seekbar
    private Timer m_SeekTimer;
    private int m_SeekPosition;
    private int m_SeekMax;

    long m_StartTime;
    static boolean m_Playing = true;

    private RefreshHandler m_RefreshHandler = new RefreshHandler();

    /**
     *
     */
    class RefreshHandler extends Handler {
        public void setOffset() {
            this.m_Offset += System.currentTimeMillis() - m_StartTime ;
        }

        long m_Offset;
        RefreshHandler() {
            m_Offset = 0;
        }

        /**
         * Handle the update and refresh the progressbar
         * @param msg  Message
         */
        @Override
        public void handleMessage(Message msg) {
            long millis = m_Offset + System.currentTimeMillis() - m_StartTime;
            // Was going to add time but its not going to be accurate
            /*
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            int hours = minutes / 60;
            minutes = minutes % 60;
            seconds     = seconds % 60;
            TextView view = (TextView) findViewById(R.id.movieTime);
            view.setText(String.format("%d:%02d:%02d",hours, minutes, seconds));
            */
            SeekBar seekbar = (SeekBar) findViewById(R.id.seekBar);
            int seekPosition = (int) (millis / 1000);
            seekbar.setProgress(seekPosition);
            m_SeekPosition = seekPosition;
        }
    }

    /**
     *
     */
    class UpdateTimeTask extends TimerTask {
        UpdateTimeTask(){
            m_StartTime = System.currentTimeMillis();
        }
        public void run() {
            m_RefreshHandler.sendEmptyMessage(0);
        }
    }


    /**
     *  Called when the activity is first created.
     * @param savedInstanceState Bundle
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_player);

        m_StartTime = System.currentTimeMillis();
        SeekBar seekbar = (SeekBar) findViewById(R.id.seekBar);
        String currentMovieRuntime = (String) getIntent().getSerializableExtra("current_movie_runtime");
        DateFormat sdf;
        String parseFormat;
        if (currentMovieRuntime.contains("h")){
            parseFormat = "hh'h' mm'min'";
        } else {
            parseFormat = "mm'min'";
        }
        sdf = new SimpleDateFormat(parseFormat);

        Date runtime = new Date(0);
        try {
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            runtime = sdf.parse(currentMovieRuntime);
        } catch (ParseException e) {
            Log.e(TAG,"Parse Error: Trying to get the ",e);
        }
        int mins = (int) (runtime.getTime()/1000);
        seekbar.setMax(mins);
        m_SeekMax = mins;
                      /*
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
                        */
        // Set the movie title
        setText("current_movie_title", R.id.movieTitle);

        // Start the background task to handle player notifications.
        XBMCSettings xbmcSettings = XBMCSettings.getInstance(this);
        new XBMCNotificationReceiver().execute(xbmcSettings.getIpAddress());

        // try to obtain a reference to a task piped through from the previous
        // activity instance
        task = (XBMCNotificationReceiver) getLastNonConfigurationInstance();

        // if there was no previous instance, create the task anew
        if (task == null) {
            resetTask();
        }

        startPendingTask(null);
        m_SeekTimer = new Timer();
        m_SeekTimer.schedule(new UpdateTimeTask(), 0, 1000);

    }

    //Play button

    /**
     * The play button has been clicked
     * @param view
     * @throws IOException
     * @throws JSONException
     */
    public void PlayPauseClick(View view) throws IOException, JSONException {

        XBMCJson json = new XBMCJson();
        JSONObject Params = new JSONObject();
        Params.put("playerid",1);
        String Command = "Player.PlayPause";
        json.writeCommand(Command, Params, this);
        if(m_Playing){
            m_SeekTimer.cancel();
            m_RefreshHandler.setOffset();
        } else {
            m_SeekTimer = new Timer();
            m_SeekTimer.schedule(new UpdateTimeTask(), 0, 1000);
        }
        m_Playing = !m_Playing;
    }

    //Stop button
    public void StopClick(View view) throws IOException, JSONException {
        XBMCJson json = new XBMCJson();
        JSONObject Params = new JSONObject();
        Params.put("playerid",1);
        String Command = "Player.Stop";
        json.writeCommand(Command, Params, this);
    }

    //Play button
    public void ForwardClick(View view) throws IOException, JSONException {
        XBMCJson json = new XBMCJson();
        JSONObject Params = new JSONObject();
        Params.put("playerid",1);
        String Command = "Player.PlayPause";
        json.writeCommand(Command, Params, this);
    }

    //Play button
    public void ReverseClick(View view) throws IOException, JSONException {
        XBMCJson json = new XBMCJson();
        JSONObject Params = new JSONObject();
        Params.put("playerid",1);
        String Command = "Player.PlayPause";
        json.writeCommand(Command, Params, this);
    }

    public void setText( String Extra, int Id){
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
            XBMCSettings xbmcSettings = XBMCSettings.getInstance(this);
            task.execute(xbmcSettings.getIpAddress());
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

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setContentView(R.layout.movie_player);

        // Set the movie title
        setText("current_movie_title", R.id.movieTitle);

        SeekBar seekbar = (SeekBar) findViewById(R.id.seekBar);
        seekbar.setMax(m_SeekMax);
        seekbar.setProgress(m_SeekPosition);
    }
}

