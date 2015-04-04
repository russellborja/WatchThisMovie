package com.example.russellborja.watchthismovie.fragments;

import android.app.Activity;
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

        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("select rowid _id,* from movies where in_theatres=1", null);

        int rows = cursor.getCount();
        if(rows == 0){
            updateMovieList(Utils.getSortByString(getActivity())); //initialize database on startup
        }

        mMovieAdapter = new MovieAdapter(getActivity(), cursor, 0);
        View rootView = inflater.inflate(R.layout.fragment_movie_theatre, container, false);
        ListView listView = (ListView) rootView.findViewById(R.id.listview_movies);
        listView.setAdapter(mMovieAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView adapterView, View view, int position, long l){
                //MovieDetails item = (MovieDetails) adapterView.getItemAtPosition(position);
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);

                if(Utils.addSelections(cursor.getString(MovieContract.COL_MOVIE_TITLE))){
                    updateSelection(Utils.getBitmapFromByteArray(cursor.getBlob(MovieContract.COL_IMAGE)));

                }
                else{
                    Toast.makeText(getActivity().getApplicationContext(), "Already Selected",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });


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

    // respond to changes in sort by setting
    public void onSortByChanged(String sortby){

    }

    // update the movies in the list
    public void updateMovieList(String sortby){
        FetchMovieData fetchMovieData = new FetchMovieData(this, getActivity().getApplicationContext());
        fetchMovieData.execute("Theatre", sortby);
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

    // update selection boxes in selection fragment
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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            updateMovieList(Utils.getSortByString(getActivity()));
            return true;
        }



        return super.onOptionsItemSelected(item);
    }
}
