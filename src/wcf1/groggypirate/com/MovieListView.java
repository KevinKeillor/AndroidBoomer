package wcf1.groggypirate.com;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 28/05/12
 * Time: 23:47
 * To change this template use File | Settings | File Templates.
 */
public class MovieListView  extends ListActivity {

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setListAdapter(new ArrayAdapter<MovieInfo>(this, R.layout.movie_header_row, (List<MovieInfo>) MovieInfo.createMovieInfo(0,"IRON MAN","1900")));

        ListView lv = getListView();
        lv.setTextFilterEnabled(true);


    }

    public void Go() {

    }
}
