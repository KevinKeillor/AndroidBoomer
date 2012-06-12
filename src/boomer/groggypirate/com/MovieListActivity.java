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
                FileInputStream fis = openFileInput(movieArtistFile);
                ObjectInputStream is = new ObjectInputStream(fis);
                Artists = (Map<Integer,String>) is.readObject();
                is.close();

            } catch (FileNotFoundException e) {

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


            String movieInfoJson;
            String movieInfoFile = "movie_Info";
            try {
                FileInputStream fis = openFileInput(movieInfoFile);
                ObjectInputStream is = new ObjectInputStream(fis);
                movieInfoJson = (String) is.readObject();
                is.close();

            } catch (FileNotFoundException e) {

                movieInfoJson =  GetMovieInfo();

                FileOutputStream fos = openFileOutput(movieInfoFile, Context.MODE_PRIVATE);
                ObjectOutputStream os = new ObjectOutputStream(fos);
                os.writeObject(movieInfoJson);
                os.close();
            }

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



                String movieId = jsonObject.get("I").toString();
                String thumbFile = "movie_Thm" + movieId;
                Drawable img;
                try {
                    FileInputStream imgIn = openFileInput(thumbFile);
                    img = Drawable.createFromResourceStream(getResources(), null, imgIn, thumbFile);
                    imgIn.close();

                }catch (FileNotFoundException e){
                    img = LoadImageFromWebOperations("http://82.41.204.91:8732/AhabService/movie/thumb/" + movieId);
                    Bitmap image_saved= ((BitmapDrawable)img).getBitmap();
                    FileOutputStream imgOut = openFileOutput(thumbFile, Context.MODE_PRIVATE);
                    image_saved.compress(Bitmap.CompressFormat.PNG,100,imgOut);
                    imgOut.flush();
                    imgOut.close();
                }

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

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    // When clicked, show a toast with the TextView text
                    Intent movieDetail = new Intent(view.getContext(), MovieDetailActivity.class);
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
                    view.getContext().startActivity(movieDetail);
                }
            });

        } catch (Exception e){
            e.printStackTrace();
        }
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


