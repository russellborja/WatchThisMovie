package com.example.russellborja.watchthismovie.fragments;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.russellborja.watchthismovie.FetchMovieData;
import com.example.russellborja.watchthismovie.MovieAdapter;
import com.example.russellborja.watchthismovie.R;
import com.example.russellborja.watchthismovie.Utils;
import com.example.russellborja.watchthismovie.data.MovieContract;
import com.example.russellborja.watchthismovie.data.MovieDbHelper;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieTheatresFragment extends Fragment {

    static final String LOG_TAG = "MovieFragment";
    private MovieAdapter mMovieAdapter;
    private onMovieClickedListener mCallback;

    public MovieTheatresFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MovieDbHelper mDbHelper = new MovieDbHelper(getActivity().getApplicationContext());

        final SQLiteDatabase db = mDbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("select rowid _id,* from movies where in_theatres=1", null);

        //debugging
        int rows = cursor.getCount();
        Log.v(LOG_TAG, "Number of rows in theatres: " + rows);

        mMovieAdapter = new MovieAdapter(getActivity(), cursor, 0);
        View rootView = inflater.inflate(R.layout.fragment_movie_theatre, container, false);
        ListView listView = (ListView) rootView.findViewById(R.id.listview_movies);
        listView.setAdapter(mMovieAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView adapterView, View view, int position, long l){
                //MovieDetails item = (MovieDetails) adapterView.getItemAtPosition(position);
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                if(cursor.getInt(MovieContract.COL_IS_SELECTED) == 0) {
                    updateSelection(Utils.getBitmapFromByteArray(cursor.getBlob(MovieContract.COL_IMAGE)));
                    ContentValues cv = new ContentValues();
                    cv.put(MovieContract.MovieEntry.COLUMN_IS_SELECTED, 1);
                    db.update(MovieContract.MovieEntry.TABLE_NAME, cv, MovieContract.MovieEntry.COLUMN_MOVIE_TITLE
                            + "= ?", new String[]{cursor.getString(MovieContract.COL_MOVIE_TITLE)});
                    //item.setmSelected(true);
                    //mMovieAdapter.add(item);
                }
                else{
                    Toast.makeText(getActivity().getApplicationContext(), "Already Selected",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        //updateMovieList();
        //MovieDbHelper dbHelper = new MovieDbHelper(getActivity().getApplicationContext());
        //dbHelper.getMovieTitle();

        db.close();
        return rootView;

    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);

        // ensures parent activity is implementing the interface
        try{
            mCallback = (onMovieClickedListener) activity;
        }
        catch(ClassCastException e){
            Log.e(LOG_TAG, activity.toString() + " must implement onMovieClickedListener." + e);
        }
    }

    public void updateMovieList(){
        FetchMovieData fetchMovieData = new FetchMovieData(this, getActivity().getApplicationContext());
        fetchMovieData.execute("Theatre");
    }

    public void update(Cursor results) {
        //mMovieAdapter.clear();
        // so notifyDataSetChanged() doesn't get called too often
        //mMovieAdapter.setNotifyOnChange(false);
        if(results != null) {
            Log.v(LOG_TAG, "Results cursor has rows: " + results.getCount());
            mMovieAdapter.changeCursor(results);

        }
        mMovieAdapter.notifyDataSetChanged();

        //mMovieAdapter.setNotifyOnChange(true);
    }

    public void updateSelection(Bitmap poster){
        mCallback.onMovieClicked(poster);
    }

    public interface onMovieClickedListener{
        public void onMovieClicked(Bitmap poster);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            Log.v(LOG_TAG, "hit refresh");
            updateMovieList();
            return true;
        }



        return super.onOptionsItemSelected(item);
    }
}
