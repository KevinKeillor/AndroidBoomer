package wcf1.groggypirate.com;

import android.graphics.drawable.Drawable;

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 29/05/12
 * Time: 00:42
 * To change this template use File | Settings | File Templates.
 */
public class MovieInfo {
    public Drawable icon;
    public String title;
    public String year;
    private MovieInfo(Drawable icon, String title, String year) {
        super();
        this.icon = icon;
        this.title = title;
        this.year = year;
    }

    public static MovieInfo create(Drawable icon, String title, String Year) {
        return new MovieInfo(icon, title, Year);
        //baalz
    }
}
