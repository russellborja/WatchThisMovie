package com.example.russellborja.watchthismovie;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.russellborja.watchthismovie.data.MovieContract;
import com.example.russellborja.watchthismovie.data.MovieDbHelper;
import com.example.russellborja.watchthismovie.fragments.MovieDVDFragment;
import com.example.russellborja.watchthismovie.fragments.MovieTheatresFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by russellborja on 2015-03-09.
 */
public class FetchMovieData extends AsyncTask<String, Void, Cursor> {

    static final String LOG_TAG = "Fetch_Movie_Data";
    private final Context mContext;
    private MovieTheatresFragment mTheatreFragment = null;
    private MovieDVDFragment mDVDFragment = null;
    private boolean isInTheatres;


    public FetchMovieData(MovieTheatresFragment fragment, Context context){
        mTheatreFragment = fragment;
        mContext = context;
    }

    public FetchMovieData(MovieDVDFragment fragment, Context context){
        mDVDFragment = fragment;
        mContext = context;
    }

    @Override
    protected Cursor doInBackground(String... params){
        String movieJsonString = null;
        HttpURLConnection urlConnection = null;
        BufferedReader br = null;
        List<MovieDetails> movieDetailsList;
        Cursor cursor;
        String movieType = params[0];
        String sortby = params[1];

        try {
            final String TMDB_BASE_URL = "https://api.themoviedb.org/3/";
            final String API_KEY = "f0641977082c93bacaca71d7f8f2efde";
            final String API_PARAM = "api_key";
            final String DISCOVER_MOVIE = "discover/movie";
            final String RELEASE_DATE_START = "primary_release_date.gte";
            final String RELEASE_DATE_FINISH = "primary_release_date.lte";
            final String SORT_BY = "sort_by";

            String dateStartStr, dateEndStr, sortByParam;

            //get sortby param
            switch(sortby){
                case "Popularity":
                    sortByParam = "popularity.desc";
                    break;
                case "Rating":
                    sortByParam = "vote_average.desc";
                    break;
                case "Release Date":
                    sortByParam = "primary_release_date.desc";
                    break;
                case "Box Office":
                    sortByParam = "revenue.desc";
                    break;
                case "Title":
                    sortByParam = "original_title.desc";
                    break;
                default:
                    sortByParam = "popularity.desc";
                    break;
            }

            //Get current date and previous month's date
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Calendar currentDate = Calendar.getInstance();
            String currentDateString = dateFormat.format(currentDate.getTime());

            //Dates for movies still in theatres
            if(movieType.equals("Theatre")) {
                dateEndStr = currentDateString;
                currentDate.add(Calendar.MONTH, -1);
                dateStartStr = dateFormat.format(currentDate.getTime());
                Log.v(LOG_TAG, dateStartStr);
                isInTheatres = true;
            }
            else{
                currentDate.add(Calendar.MONTH, -4);
                dateEndStr = dateFormat.format(currentDate.getTime());
                currentDate.add(Calendar.MONTH, -6);
                dateStartStr = dateFormat.format(currentDate.getTime());
                isInTheatres = false;
            }




            Uri builtUri = Uri.parse(TMDB_BASE_URL + DISCOVER_MOVIE).buildUpon()
                    .appendQueryParameter(RELEASE_DATE_START, dateStartStr)
                    .appendQueryParameter(RELEASE_DATE_FINISH, dateEndStr)
                    .appendQueryParameter(SORT_BY, sortByParam)
                    .appendQueryParameter(API_PARAM, API_KEY).build();

            Log.v(LOG_TAG, builtUri.toString());

            URL url = new URL(builtUri.toString());



            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            br = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while((line = br.readLine()) !=null){
                buffer.append(line + "/n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }

            movieJsonString = buffer.toString();
            Log.v(LOG_TAG, movieJsonString);
            cursor = getMovieDataFromJSON(movieJsonString);
            return cursor;
        }
        catch(JSONException e){
            Log.e(LOG_TAG, "JSONException: " +e);
        }
        catch(IOException e){
            Log.e(LOG_TAG, "Error: " +e);

        }finally{
            if(urlConnection != null)
                urlConnection.disconnect();
            if(br != null)
                try {
                    br.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
        }

        return null;

    }

    @Override
    protected void onPostExecute(Cursor results){
        if(mTheatreFragment != null)
            mTheatreFragment.update(results, mContext);
        if(mDVDFragment != null)
            mDVDFragment.update(results, mContext);
    }

    private Cursor getMovieDataFromJSON(String jsonString) throws JSONException{
        final String RESULTS = "results";
        final String MOVIE_TITLE = "original_title";
        final String RELEASE_DATE = "release_date";
        final String RATING = "vote_average";
        final String POSTER_PATH = "poster_path";
        final String TMDB_IMAGE_URL = "http://image.tmdb.org/t/p/w185";
        MovieDbHelper mDbHelper = new MovieDbHelper(mContext);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Cursor result;


            JSONObject rawData = new JSONObject(jsonString);
            JSONArray resultsList = rawData.getJSONArray(RESULTS);
            //List<MovieDetails> movieDetailsList = new ArrayList<MovieDetails>(resultsList.length());

            db.delete(MovieContract.MovieEntry.TABLE_NAME, null, null);
            db.execSQL("VACUUM");
//            db.delete(MovieContract.MovieEntry.TABLE_NAME,
//                    MovieContract.MovieEntry.COLUMN_IN_THEATRES + "=" + (isInTheatres ? 1 : 0), null
//                );

            for (int i = 0; i < resultsList.length(); i++) {
                String title, releaseDate, bitmapUrl;
                double rating;
                MovieDetails movieDetails = new MovieDetails();

                //Get movie info
                JSONObject movieInfo = resultsList.getJSONObject(i);
                title = movieInfo.getString(MOVIE_TITLE);
                releaseDate = movieInfo.getString(RELEASE_DATE);
                rating = movieInfo.getDouble(RATING);
                bitmapUrl = movieInfo.getString(POSTER_PATH);
                Log.v(LOG_TAG, bitmapUrl);

                //Check if it's already in the table
                String queryStr = "SELECT " + MovieContract.MovieEntry.COLUMN_MOVIE_TITLE + " FROM "
                        + MovieContract.MovieEntry.TABLE_NAME + " WHERE movie_title=\"" + title +"\"";
                Cursor q = db.rawQuery(queryStr,null);



                if(q.getCount() == 0) {
                    // populate database with movie details
                    ContentValues values = new ContentValues();
                    values.put(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE, title);
                    values.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, releaseDate);
                    values.put(MovieContract.MovieEntry.COLUMN_RATING, rating);
                    values.put(MovieContract.MovieEntry.COLUMN_IN_THEATRES, (isInTheatres ? 1 : 0));
                    values.put(MovieContract.MovieEntry.COLUMN_IS_SELECTED, 0);
                    values.put(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER, Utils.getByteArrayFromUrl(TMDB_IMAGE_URL + bitmapUrl));

                    // Insert the new row, returning the primary key value of the new row
                    long newRowId;


                    newRowId = db.insert(
                            MovieContract.MovieEntry.TABLE_NAME,
                            null,
                            values);
                    //db.close();
                    Log.v(LOG_TAG, "Row id: " + newRowId);
                }



            }

        //db.close();
        if(!isInTheatres) {
            result = db.rawQuery("select rowid _id,* from " + MovieContract.MovieEntry.TABLE_NAME +
                    " where " + MovieContract.MovieEntry.COLUMN_IN_THEATRES + "=0", null);
        }
        else{
            result = db.rawQuery("select rowid _id,* from " + MovieContract.MovieEntry.TABLE_NAME +
                    " where " + MovieContract.MovieEntry.COLUMN_IN_THEATRES + "=1", null);
            Log.v(LOG_TAG, "Number of rows in result cursor:" +result.getCount());
        }
        return result;
    }


}
