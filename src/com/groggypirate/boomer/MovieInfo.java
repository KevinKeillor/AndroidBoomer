package com.groggypirate.boomer;


import android.graphics.drawable.Drawable;

/**
 * MovieInfo
 */
public class MovieInfo extends MovieRawInfo {
    public Drawable icon; // dvd cover

    public MovieInfo(){

    }
    private MovieInfo(Drawable Icon, String Id, String Title, String Year, String Genre, String Runtime, String Cast, String Thumb) {
        super();
        this.icon = Icon;
        this.id = Id;
        this.title = Title;
        this.year = Year;
        this.genre = Genre;
        this.runtime = Runtime;
        this.cast = Cast;
        this.thumb = Thumb;
    }

    /**
     *
     * @param Icon
     * @param Id
     * @param Title
     * @param Year
     * @param Genre
     * @param Runtime
     * @param Cast
     * @return
     */
    public static MovieInfo create(Drawable Icon, String Id, String Title, String Year, String Genre, String Runtime, String Cast , String Thumb) {
        return new MovieInfo(Icon, Id, Title, Year, Genre, Runtime, Cast, Thumb);
    }
}

