package wcf1.groggypirate.com;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 01/06/12
 * Time: 22:15
 * To change this template use File | Settings | File Templates.
 */
public class MovieAdapter extends ArrayAdapter<MovieInfo>
{

    Context context;
    int layoutResourceId;
    MovieInfo data[] = null;

    public MovieAdapter(Context context, int layoutResourceId, MovieInfo[] data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        MovieHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new MovieHolder();
            holder.imgIcon = (ImageView)row.findViewById(R.id.movieImg);
            holder.txtTitle = (TextView)row.findViewById(R.id.txtTitle);

            row.setTag(holder);
        }
        else
        {
            holder = (MovieHolder)row.getTag();
        }

        MovieInfo movie = data[position];
        holder.txtTitle.setText(movie.title);
        holder.imgIcon.setImageDrawable(movie.icon);

        return row;
    }

    static class MovieHolder
    {
        ImageView imgIcon;
        TextView txtTitle;
    }
}
