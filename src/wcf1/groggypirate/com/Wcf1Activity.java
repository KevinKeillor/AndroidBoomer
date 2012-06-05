package wcf1.groggypirate.com;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ListView;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class Wcf1Activity extends Activity {

    private ListView movieListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        try {



            String movieInfoJson =  GetMovieInfo();
            JSONArray jsonArray = new JSONArray(movieInfoJson);
            MovieInfo movie_data[] = new MovieInfo[jsonArray.length()];
            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Drawable img = LoadImageFromWebOperations("http://82.41.204.91:8732/AhabService/movie/thumb/" + jsonObject.get("I"));
                MovieInfo info = MovieInfo.create(img, jsonObject.get("I").toString(),
                                        jsonObject.get("N").toString(),
                                        jsonObject.get("Y").toString(),
                                        jsonObject.get("G").toString(),
                                        jsonObject.get("T").toString(),
                                        jsonObject.get("R").toString());
                movie_data[i] = info;
            }

            MovieAdapter adapter = new MovieAdapter(this,
                    R.layout.movie_item_row, movie_data);

            adapter.refreshArray();

            movieListView = (ListView)findViewById(R.id.movieListView);

            View header = (View)getLayoutInflater().inflate(R.layout.movie_header_row, null);
            movieListView.addHeaderView(header);

            movieListView.setAdapter(adapter);
        } catch (Exception e){
        }
    }

    public static Drawable LoadImageFromWebOperations(String url)
    {
        try
        {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        }
        catch (Exception e)
        {
            return null;
        }
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
}

/*
public void Go() {
RestClient client = new RestClient("http://192.168.1.14:8732/Service1/data/1");

try {
client.Execute(RestClient.RequestMethod.GET);
} catch (Exception e) {
e.printStackTrace();
}
String response = client.getResponse();
Log.d(response, response);
}
*/


