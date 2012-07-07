package com.groggypirate.boomer;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import boomer.groggypirate.com.R;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.lang.Math;
import java.net.URLConnection;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MovieListActivity extends ListActivity {

    private static final String TAG = "Boomer_MovieListActivity";
    private MovieInfo m_Movie_Data[];
    private static final String m_movieInfoFile = "Movie_Info";

    Map<Integer,String> Artists = new HashMap<Integer,String>();

    // our handler
    Handler m_Handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1){
                // get the bundle and extract data by key
                Bundle b = msg.getData();
                String jsonString = b.getString("Response");
                BuildViewFromJson(jsonString);
            }
            else if (msg.what == 2){
                // get the bundle and extract data by key
               // Bundle b = msg.getData();
               // String jsonString = b.getString("Response");
               // Integer position = b.getInt("Position");
               // ListView lv = getListView();
               // View row = lv.getChildAt(position);


            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            /*
            String movieArtistFile = "movie_Artists";

            try {
                LoadArtistInfo(movieArtistFile);

            } catch (FileNotFoundException e) {

                GetArtistIDataA(movieArtistFile);
            }
             */

            try {
                LoadMovieInfo(m_movieInfoFile);

            } catch (FileNotFoundException e) {
                GetMovieInfo();
            }

            //BuildView(movieInfoJson);

        } catch (Exception e){
            e.printStackTrace();
        }

    }

    private void BuildViewFromJson(String movieInfoJson) {

        FileOutputStream fos = null;
        try {
            fos = openFileOutput(m_movieInfoFile, Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(movieInfoJson);
            os.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        JSONObject jsonContainer = null;
        try {
            jsonContainer = new JSONObject(movieInfoJson);
            jsonContainer = jsonContainer.getJSONObject("result");
            JSONArray jsonArray = jsonContainer.getJSONArray("movies");

            m_Movie_Data = new MovieInfo[jsonArray.length()];

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObject = jsonArray.getJSONObject(i);

                //Drawable img = getResources().getDrawable( R.drawable.play );
                Drawable img = BuildThumb(jsonObject.get("thumbnail").toString(), jsonObject.get("movieid").toString());

                MovieInfo info = MovieInfo.create(img, jsonObject.get("movieid").toString(),
                                        jsonObject.get("label").toString(),
                                        jsonObject.get("year").toString(),
                                        jsonObject.get("genre").toString(),
                                        jsonObject.get("runtime").toString(),
                                        "cast",jsonObject.get("thumbnail").toString());
                m_Movie_Data[i] = info;
            }
            MovieAdapter adapter = new MovieAdapter(this,
                    R.layout.movie_item_row, m_Movie_Data);

            adapter.refreshArray();

            setListAdapter(adapter);

            ListView lv = getListView();
            lv.setTextFilterEnabled(true);

            SetOnItemClick(lv);

            lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                // Start a movie player activity
                public boolean onItemLongClick(AdapterView<?> av, View view, int position, long id) {
                    Intent moviePlayer = new Intent(view.getContext(), MoviePlayerActivity.class);
                    IntentFillExtra(position, moviePlayer);
                    view.getContext().startActivity(moviePlayer);
                    return true;
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


    }

    private void SetOnItemClick(ListView lv) {
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // When clicked, move to the detail view
                Intent movieDetail = new Intent(view.getContext(), MovieDetailActivity.class);
                IntentFillExtra(position, movieDetail);
                view.getContext().startActivity(movieDetail);
            }
        });
    }

    private void IntentFillExtra(int position, Intent movieDetail) {
        MovieInfo currentMovie = m_Movie_Data[position];
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

    private Drawable BuildThumb(String thumbnail, String movieId) {
        String thumbFile = "movie_thb" + movieId;
        Drawable img = null;

        try {
            img = LoadMovieThumb(thumbFile);
        }catch (FileNotFoundException e){
            GetMovieThumb(thumbnail,movieId);
            img = getResources().getDrawable(R.drawable.nocover);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            img = getResources().getDrawable(R.drawable.nocover);
        }

        return img;
    }

    private void GetMovieThumb(final String thumbnail, final String movieId) {


        final Context context = this;

        new Thread(new Runnable() {
            public void run() {

                XBMCSettings xbmcSettings = XBMCSettings.getInstance(context);
                String path = "http://"+xbmcSettings.getIpAddress()+":"+xbmcSettings.getPort()+"/vfs/"+thumbnail;

                Drawable img = null;
                URL url = null;

                try {
                    url = new URL(path);
                    URLConnection  uc = url.openConnection();

                    uc.setDoOutput(true);
                    uc.setConnectTimeout(5000);
                    uc.setReadTimeout(5000);
                    uc.setRequestProperty("Connection", "close");

                    String authEncoded = EncodeAuth(xbmcSettings.getName()+":"+xbmcSettings.getPassword());

                    if (authEncoded != null) {
                        uc.setRequestProperty("Authorization", "Basic " + authEncoded);
                    }
                    InputStream stream = uc.getInputStream();
                    img =  Drawable.createFromStream(stream, "src name");
                    img = resize(img);
                    Bitmap image_saved= ((BitmapDrawable)img).getBitmap();
                    FileOutputStream imgOut = openFileOutput("movie_thb"+movieId, Context.MODE_PRIVATE);
                    image_saved.compress(Bitmap.CompressFormat.PNG,100,imgOut);
                    imgOut.flush();
                    imgOut.close();
                    //return img;
                    // our handler
                    Message Msg = new Message();
                    Bundle bndl = new Bundle();
                    bndl.putString("Response", "movie_thb"+movieId);
                    Msg.setData(bndl);
                    Msg.what = 2;
                    m_Handler.sendMessage(Msg);

                } catch (MalformedURLException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } catch (IOException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }


            }
        }).start();

    }

    private Drawable resize(Drawable image) {
        Bitmap d = ((BitmapDrawable)image).getBitmap();
        Bitmap bitmapOrig = Bitmap.createScaledBitmap(d, 100, 150, true);
        return new BitmapDrawable(bitmapOrig);
    }
    /**
     * Returns a base64 encoded version of the supplied authorisation code
     *
     * @param  auth the authorisation code to be encoded
     * @return      the base64 encoded authorisation code
     * @see         String
     */
    private String EncodeAuth(String auth) {
        String authEncoded;
        authEncoded = Base64.encodeBytes(auth.getBytes());
        return authEncoded;
    }

    private Drawable LoadMovieThumb(String thumbFile) throws IOException {
        Drawable img;FileInputStream imgIn = openFileInput(thumbFile);
        img = Drawable.createFromResourceStream(getResources(), null, imgIn, thumbFile);
        imgIn.close();
        return img;
    }

    private void LoadMovieInfo(String movieInfoFile) throws IOException, ClassNotFoundException {
        String movieInfoJson;FileInputStream fis = openFileInput(movieInfoFile);
        ObjectInputStream is = new ObjectInputStream(fis);
        movieInfoJson = (String) is.readObject();
        is.close();
        BuildViewFromJson(movieInfoJson);
    }

    private void LoadArtistInfo(String movieArtistFile) throws IOException, ClassNotFoundException {
        FileInputStream fis = openFileInput(movieArtistFile);
        ObjectInputStream is = new ObjectInputStream(fis);
        Artists = (Map<Integer,String>) is.readObject();
        is.close();
    }

    private void GetArtistIDataA(String movieArtistFile) throws JSONException, IOException {
        String artistInfoJson =  GetArtistInfo();
        JSONArray jsonArtistArray = new JSONArray(artistInfoJson);

        for (int x = 0; x < jsonArtistArray.length(); x++) {
            JSONObject jsonArtistObject = jsonArtistArray.getJSONObject(x);
            Artists.put(jsonArtistObject.getInt("I"),jsonArtistObject.get("N").toString());
        }

        FileOutputStream fos = openFileOutput(movieArtistFile, Context.MODE_PRIVATE);
        ObjectOutputStream os = new ObjectOutputStream(fos);
        os.writeObject(Artists);
        os.close();
    }

    public static Drawable LoadImageFromWebOperations(String url)
    {
        try
        {
            InputStream is = (InputStream) new URL(url).getContent();
            return Drawable.createFromStream(is, "src name");
        }
        catch (Exception e)
        {
            return null;
        }
    }

    public String GetArtistInfo() {
        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        XBMCSettings settings = XBMCSettings.getInstance(this);
        HttpGet httpGet = new HttpGet(

                "http://"+settings.getIpAddress()+":8732/AhabService/movie/artist");
        try {
            HttpResponse response = client.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
            }
        } catch (Exception e){
            return null;
        }
        return builder.toString();
    }

    public void GetMovieInfo() {

        XBMCJson json = new XBMCJson();
        JSONObject Params = new JSONObject();
        String Command = "VideoLibrary.GetMovies";
        JSONArray Properties = new JSONArray();
        try {
            JSONObject sort = new JSONObject();
            sort.put("method", "label");
            Properties.put("thumbnail");
            Properties.put("year");
            Properties.put("rating");
            Properties.put("genre");
            Properties.put("cast");
            Properties.put("runtime");
            Params.put("properties",Properties);
            Params.put("sort",sort);
        } catch (JSONException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        json.writeCommand(Command, Params, this, m_Handler);

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.xbmc_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.xbmcsettings:

                if (Build.VERSION.SDK_INT< Build.VERSION_CODES.HONEYCOMB) {
                    startActivity(new Intent(this, XBMCSettingsActivity.class));
                }
                else {
                    startActivity(new Intent(this, XBMCSettingsActivityHC.class));
                }

                return(true);
        }

        return true;
    }
}

/*

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
       // super.onListItemClick(l, v, position, id);
        Intent movieDetail = new Intent(v.getContext(),MovieDetailActivity.class);
        v.getContext().startActivity(movieDetail);

    }


*/


