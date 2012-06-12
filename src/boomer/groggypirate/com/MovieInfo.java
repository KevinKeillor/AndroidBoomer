package boomer.groggypirate.com;


import android.graphics.drawable.Drawable;

public class MovieInfo extends MovieRawInfo {
    public Drawable icon; // dvd cover

    private MovieInfo(Drawable Icon, String Id, String Title, String Year, String Genre, String Tag, String Runtime, String Cast,
                      String Plot, String Director, String Writer ) {
        super();
        this.icon = Icon;
        this.id = Id;
        this.title = Title;
        this.year = Year;
        this.genre = Genre;
        this.tag = Tag;
        this.runtime = Runtime;
        this.cast = Cast;
        this.plot = Plot;
        this.director = Director;
        this.writer = Writer;

    }

    public static MovieInfo create(Drawable Icon, String Id, String Title, String Year, String Genre, String Tag, String Runtime , String Cast,
         String Plot, String Director, String Writer ) {
        return new MovieInfo(Icon, Id, Title, Year, Genre, Tag, Runtime, Cast, Plot, Director, Writer);
    }
}

