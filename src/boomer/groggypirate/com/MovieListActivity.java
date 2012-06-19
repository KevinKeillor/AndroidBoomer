package boomer.groggypirate.com;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
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
import java.net.URL;
import java.lang.Math;
import java.util.HashMap;
import java.util.Map;

public class MovieListActivity extends ListActivity {

    private MovieInfo m_Movie_Data[];

    Map<Integer,String> Artists = new HashMap<Integer,String>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            String movieArtistFile = "movie_Artists";

            try {
                LoadArtistInfo(movieArtistFile);

            } catch (FileNotFoundException e) {

                GetArtistIDataA(movieArtistFile);
            }

            String movieInfoJson;
            String movieInfoFile = "movie_Info";
            try {
                movieInfoJson = LoadMovieInfo(movieInfoFile);

            } catch (FileNotFoundException e) {

                movieInfoJson = GetMovieInfoA(movieInfoFile);
            }

            BuildView(movieInfoJson);

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void BuildView(String movieInfoJson) throws JSONException, IOException {
        JSONArray jsonArray = new JSONArray(movieInfoJson);
        m_Movie_Data = new MovieInfo[jsonArray.length()];

        for (int i = 0; i < jsonArray.length(); i++) {

            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String castString = "";
            JSONArray castArray = new JSONArray(jsonObject.get("C").toString());

            for (int j = 0; j < Math.min(castArray.length(),2); j++) {
                JSONObject castObject = castArray.getJSONObject(j);
                Integer artistId = castObject.getInt("I");
                if (j > 0 ){
                    castString += ", ";
                }
                castString += Artists.get(artistId);
                castString += " as " + castObject.get("N").toString();
            }


            Drawable img = BuildThumb(jsonObject);

            MovieInfo info = MovieInfo.create(img, jsonObject.get("I").toString(),
                                    jsonObject.get("N").toString(),
                                    jsonObject.get("Y").toString(),
                                    jsonObject.get("G").toString(),
                                    jsonObject.get("T").toString(),
                                    jsonObject.get("R").toString(),
                                    castString,
                                    jsonObject.get("P").toString(),
                                    jsonObject.get("D").toString(),
                                    jsonObject.get("W").toString());
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

    private Drawable BuildThumb(JSONObject jsonObject) throws JSONException, IOException {
        String movieId = jsonObject.get("I").toString();
        String thumbFile = "movie_Thm" + movieId;
        Drawable img;
        try {
            img = LoadMovieThumb(thumbFile);

        }catch (FileNotFoundException e){
            img = GetMovieThumb(movieId, thumbFile);
        }
        return img;
    }

    private Drawable GetMovieThumb(String movieId, String thumbFile) throws IOException {
        Drawable img;
        img = LoadImageFromWebOperations("http://82.41.204.91:8732/AhabService/movie/thumb/" + movieId);
        Bitmap image_saved= ((BitmapDrawable)img).getBitmap();
        FileOutputStream imgOut = openFileOutput(thumbFile, Context.MODE_PRIVATE);
        image_saved.compress(Bitmap.CompressFormat.PNG,100,imgOut);
        imgOut.flush();
        imgOut.close();
        return img;
    }

    private Drawable LoadMovieThumb(String thumbFile) throws IOException {
        Drawable img;FileInputStream imgIn = openFileInput(thumbFile);
        img = Drawable.createFromResourceStream(getResources(), null, imgIn, thumbFile);
        imgIn.close();
        return img;
    }

    private String GetMovieInfoA(String movieInfoFile) throws IOException {
        String movieInfoJson;
        movieInfoJson =  GetMovieInfo();

        FileOutputStream fos = openFileOutput(movieInfoFile, Context.MODE_PRIVATE);
        ObjectOutputStream os = new ObjectOutputStream(fos);
        os.writeObject(movieInfoJson);
        os.close();
        return movieInfoJson;
    }

    private String LoadMovieInfo(String movieInfoFile) throws IOException, ClassNotFoundException {
        String movieInfoJson;FileInputStream fis = openFileInput(movieInfoFile);
        ObjectInputStream is = new ObjectInputStream(fis);
        movieInfoJson = (String) is.readObject();
        is.close();
        return movieInfoJson;
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
        HttpGet httpGet = new HttpGet(
                "http://82.41.204.91:8732/AhabService/movie/artist");
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

    public String GetMovieInfo() {
        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(
                "http://82.41.204.91:8732/AhabService/movie/info");
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

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);


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


