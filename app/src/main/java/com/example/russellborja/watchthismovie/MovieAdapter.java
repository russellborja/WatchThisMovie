package com.example.russellborja.watchthismovie;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.russellborja.watchthismovie.data.MovieContract;

/**
 * Created by russellborja on 2015-03-15.
 */
public class MovieAdapter extends CursorAdapter {

    private final String LOG_TAG = "MovieAdapter";

    public MovieAdapter(Context context, Cursor c, int flags){
        super(context, c, flags);
    }

    public static class ViewHolder{
        public final ImageView imageView;
        public final TextView titleTextView;
        public final TextView dateTextView;
        public final TextView ratingTextView;

        public ViewHolder(View view){
            imageView = (ImageView) view.findViewById(R.id.list_movies_imageview);
            titleTextView = (TextView) view.findViewById(R.id.list_movies_textview);
            dateTextView = (TextView) view.findViewById(R.id.list_movies_date_textview);
            ratingTextView = (TextView) view.findViewById(R.id.list_movies_rating_textview);
        }
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_movie, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        String movieTitle = cursor.getString(MovieContract.COL_MOVIE_TITLE);
        String releaseDate = cursor.getString(MovieContract.COL_RELEASE_DATE);
        double rating = cursor.getDouble(MovieContract.COL_RATING);



        Log.v(LOG_TAG, "Movie title: " + movieTitle);
        for(String column: cursor.getColumnNames()) {
            Log.v(LOG_TAG, column);
        }
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        viewHolder.titleTextView.setText(movieTitle);
        viewHolder.titleTextView.setTypeface(null, Typeface.BOLD);
        viewHolder.dateTextView.setText("Released: " + releaseDate);
        viewHolder.ratingTextView.setText("Rating: " + rating);
        if (cursor.getBlob(MovieContract.COL_IMAGE) != null){
            Bitmap moviePoster = Utils.getBitmapFromByteArray(cursor.getBlob(MovieContract.COL_IMAGE));
            viewHolder.imageView.setImageBitmap(moviePoster);
        }
        else{
            viewHolder.imageView.setImageResource(R.drawable.poster_not_available);
        }

    }
}
