package com.example.russellborja.watchthismovie;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by russellborja on 2015-03-28.
 */
public class Utils {

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
        //Log.v(LOG_TAG, data.toString());
        return data;
    }

    public static Bitmap getBitmapFromByteArray(byte[] byteArray){
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray,0, byteArray.length);
        return bitmap;
    }
}
