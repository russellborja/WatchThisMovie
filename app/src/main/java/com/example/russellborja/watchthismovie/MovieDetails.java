package com.example.russellborja.watchthismovie;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by russellborja on 2015-03-15.
 */
public class MovieDetails {

    private String mTitle;
    private String mReleaseDate;
    private double mRating;
    private Bitmap moviePoster;
    private Boolean mSelected;

    public Boolean getmSelected() {
        return mSelected;
    }

    public void setmSelected(Boolean mSelected) {
        this.mSelected = mSelected;
    }

    public MovieDetails(){
        mSelected = false;
    }

    public MovieDetails(String title, String date, double rating){
        mTitle = title;
        mReleaseDate = date;
        mRating = rating;
        mSelected = false;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmReleaseDate() {
        return mReleaseDate;
    }

    public void setmReleaseDate(String mReleaseDate) {
        this.mReleaseDate = mReleaseDate;
    }

    public double getmRating() {
        return mRating;
    }

    public void setmRating(double mRating) {
        this.mRating = mRating;
    }

    public void setMoviePoster(String urlString){
        if(urlString != null) {
            try {
                InputStream is = new URL(urlString).openStream();
                moviePoster = BitmapFactory.decodeStream(is);
            } catch (IOException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
        }
        else{
            moviePoster = null;
        }
    }

    public Bitmap getMoviePoster(){
        return moviePoster;
    }

}
