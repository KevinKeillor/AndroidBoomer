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
        IgnitedAsyncTaskContextHandler<Integer, String> , SeekBar.OnSeekBarChangeListener {

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
        seekbar.setOnSeekBarChangeListener(this);
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

        // Start the movie
        //{ "jsonrpc": "2.0", "id": "7", "method": "Player.Open", "params": { "item": { "movieid": 14 } } }
        try {
            XBMCJson json = new XBMCJson();
            JSONObject Params = new JSONObject();
            JSONObject item = new JSONObject();

            String currentMovieId = (String) getIntent().getSerializableExtra("current_movie_id");
            item.put("movieid",Integer.valueOf(currentMovieId));
            Params.put("item",item);
            String Command = "Player.Open";
            json.writeCommand(Command, Params, this);
        } catch (JSONException e) {
            Log.e(TAG,"Failed to create Player.Open Json command",e);
        }

    }

    /**
     *
     * @param seekBar
     * @param progress
     * @param fromUser
     */
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress,
                                  boolean fromUser) {
        if (fromUser){ // Ignore changes from the timer
            XBMCJson json = new XBMCJson();
            JSONObject Params = new JSONObject();
            try {
                Params.put("playerid",1);
                double position = seekBar.getMax();
                position = (progress/position) * 100.0;
                Params.put("value", position);
                String Command = "Player.Seek";
                json.writeCommand(Command, Params, this);
                m_RefreshHandler.setOffset();
                m_StartTime = System.currentTimeMillis() - (progress*1000) + m_RefreshHandler.m_Offset;

            } catch (JSONException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

        }

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    //Play button

    /**
     * The play button has been clicked
     * @param view
     * @throws IOException
     * @throws JSONException
     */
    public void PlayPauseClick(View view) {

        XBMCJson json = new XBMCJson();
        JSONObject Params = new JSONObject();
        try {
            Params.put("playerid",1);
        } catch (JSONException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        String Command = "Player.PlayPause";
        json.writeCommand(Command, Params, this);
        ImageButton ImgButton = (ImageButton)view;

        if(m_Playing){
            m_SeekTimer.cancel();
            m_RefreshHandler.setOffset();
            ImgButton.setImageResource(R.drawable.play);
        } else {
            m_SeekTimer = new Timer();
            m_SeekTimer.schedule(new UpdateTimeTask(), 0, 1000);
            ImgButton.setImageResource(R.drawable.pause);
        }
        m_Playing = !m_Playing;

    }

    //Stop button
    public void StopClick(View view) {

        XBMCJson json = new XBMCJson();
        JSONObject Params = new JSONObject();
        try {
            Params.put("playerid",1);
            String Command = "Player.Stop";
            json.writeCommand(Command, Params, this);
        } catch (JSONException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } finally {
            finish();
        }
    }

    //Forward button
    public void ForwardClick(View view) {
        XBMCJson json = new XBMCJson();
        JSONObject Params = new JSONObject();
        try {
            Params.put("playerid",1);
            Params.put("speed","increment");
            String Command = "Player.SetSpeed";
            json.writeCommand(Command, Params, this);
        } catch (JSONException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    //Reverse button
    public void ReverseClick(View view) {
        XBMCJson json = new XBMCJson();
        JSONObject Params = new JSONObject();
        try{
            Params.put("playerid",1);
            Params.put("speed","decrement");
            String Command = "Player.SetSpeed";
            json.writeCommand(Command, Params, this);
        } catch (JSONException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    //Reverse button
    public void UpClick(View view) {
        XBMCJson json = new XBMCJson();
        JSONObject Params = new JSONObject();
        String Command = "Input.Up";
        json.writeCommand(Command, Params, this);
    }

    //Reverse button
    public void LeftClick(View view) {
        XBMCJson json = new XBMCJson();
        JSONObject Params = new JSONObject();
        String Command = "Input.Left";
        json.writeCommand(Command, Params, this);
    }

    public void RightClick(View view) {
        XBMCJson json = new XBMCJson();
        JSONObject Params = new JSONObject();
        String Command = "Input.Right";
        json.writeCommand(Command, Params, this);
    }
    //Reverse button
    public void DownClick(View view) {
        XBMCJson json = new XBMCJson();
        JSONObject Params = new JSONObject();
        String Command = "Input.Down";
        json.writeCommand(Command, Params, this);
    }

    //Select button
    public void SelectClick(View view) {
        XBMCJson json = new XBMCJson();
        JSONObject Params = new JSONObject();
        String Command = "Input.Select";
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
        seekbar.setOnSeekBarChangeListener(this);
    }
}

