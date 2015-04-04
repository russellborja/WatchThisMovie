package com.example.russellborja.watchthismovie;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by russellborja on 2015-03-28.
 */
public class Utils {

    public static String[] selectedViews = {"","","",""};

    // array that keeps track of movies selected, returns true if valid selection
    public static boolean addSelections(String title){
        for(int i=0; i< selectedViews.length; i++){
            if(selectedViews[i].equals(title)){
                return false;
            }
            else if(selectedViews[i].equals("")) {
                selectedViews[i] = title;
                return true;
            }
        }
        return false;
    }

    public static boolean removeAllSelections(){
        for(int i=0; i< selectedViews.length; i++){
            selectedViews[i]="";
        }
        return true;
    }

    public static int getIntSelected(){
        int counter=0;
        for(int i=0; i< selectedViews.length; i++){
            if(!(selectedViews[i].equals(""))){
                counter++;
            }
        }
        return counter;
    }


    public static byte[] getByteArrayFromUrl(String urlString){
        Bitmap bitmap = null;
        byte[] data = null;
        if(urlString != null) {
            try {
                InputStream is = new URL(urlString).openStream();
                bitmap = BitmapFactory.decodeStream(is);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                data = baos.toByteArray();
            } catch (IOException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
        }
        return data;
    }

    public static Bitmap getBitmapFromByteArray(byte[] byteArray){
        if(byteArray != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            return bitmap;
        }
        return null;
    }

    public static String getSortByString(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(context.getString(R.string.pref_units_key),
                context.getString(R.string.pref_units_default));
    }

}
