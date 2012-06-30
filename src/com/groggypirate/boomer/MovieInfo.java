package com.groggypirate.boomer;


import android.graphics.drawable.Drawable;

/**
 * MovieInfo
 */
public class MovieInfo extends MovieRawInfo {
    public Drawable icon; // dvd cover

    /**
     *
     * @param Icon
     * @param Id
     * @param Title
     * @param Year
     * @param Genre
     * @param Tag
     * @param Runtime
     * @param Cast
     * @param Plot
     * @param Director
     * @param Writer
     */
    public MovieInfo(){

    }
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

    /**
     *
     * @param Icon
     * @param Id
     * @param Title
     * @param Year
     * @param Genre
     * @param Tag
     * @param Runtime
     * @param Cast
     * @param Plot
     * @param Director
     * @param Writer
     * @return
     */
    public static MovieInfo create(Drawable Icon, String Id, String Title, String Year, String Genre, String Tag, String Runtime , String Cast,
         String Plot, String Director, String Writer ) {
        return new MovieInfo(Icon, Id, Title, Year, Genre, Tag, Runtime, Cast, Plot, Director, Writer);
    }
}

