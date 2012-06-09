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
    public String id;
    public String title;
    public String year;
    public String genre;
    public String tag;
    public String runtime;
    public String cast;

    private MovieInfo(Drawable Icon, String Id, String Title, String Year, String Genre, String Tag, String Runtime, String Cast) {
        super();
        this.icon = Icon;
        this.id = Id;
        this.title = Title;
        this.year = Year;
        this.genre = Genre;
        this.tag = Tag;
        this.runtime = Runtime;
        this.cast = Cast;

    }

    public static MovieInfo create(Drawable Icon, String Id, String Title, String Year, String Genre, String Tag, String Runtime , String Cast) {
        return new MovieInfo(Icon, Id, Title, Year, Genre, Tag, Runtime, Cast);
    }
}
