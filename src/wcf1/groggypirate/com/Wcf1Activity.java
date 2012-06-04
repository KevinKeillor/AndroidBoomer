package wcf1.groggypirate.com;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ListView;

import java.io.InputStream;
import java.net.URL;

public class Wcf1Activity extends Activity {

    private ListView movieListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Drawable img =  LoadImageFromWebOperations("http://82.41.204.91:8732/AhabService/movie/thumb/1");
        MovieInfo movie_data[] = new MovieInfo[]
                {
                        MovieInfo.create(img, "Cloudy","1900"),
                        MovieInfo.create(img, "Showers","1901"),
                        MovieInfo.create(img, "Cloudy","1900"),
                        MovieInfo.create(img, "Showers","1901"),
                        MovieInfo.create(img, "Cloudy","1900"),
                        MovieInfo.create(img, "Showers","1901"),
                        MovieInfo.create(img, "Cloudy","1900"),
                        MovieInfo.create(img, "Showers","1901"),
                        MovieInfo.create(img, "Cloudy","1900"),
                        MovieInfo.create(img, "Showers","1901")
                };

        MovieAdapter adapter = new MovieAdapter(this,
                R.layout.movie_item_row, movie_data);


        movieListView = (ListView)findViewById(R.id.movieListView);

        View header = (View)getLayoutInflater().inflate(R.layout.movie_header_row, null);
        movieListView.addHeaderView(header);

        movieListView.setAdapter(adapter);
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


