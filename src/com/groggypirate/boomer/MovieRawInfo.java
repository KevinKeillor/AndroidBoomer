package com.groggypirate.boomer;
import java.io.Serializable;

public class MovieRawInfo implements Serializable {

    public String id; // numeric id
    public String title; // name
    public String year; // year of release
    public String genre; // list of genres / delimited
    public String tag; // Tagline / short description
    public String runtime; // duration in xh xxmins format
    public String cast; // Json information of cast
    public Integer rating; // x10
    public Integer hd; // 0: SD 1: HD
    public String director;
    public String writer;
    public String plot;
}