package com.example.russellborja.watchthismovie.fragments;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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


public class MovieDVDFragment extends Fragment {
    static final String LOG_TAG = "DVDFragment";
    private MovieAdapter mMovieAdapter;
    private onDVDClickedListener mCallback;




    public MovieDVDFragment() {
        // Required empty public constructor
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
        Cursor cursor = db.rawQuery("select rowid _id,* from movies where in_theatres=0", null);
        mMovieAdapter = new MovieAdapter(getActivity(), cursor, 0);
        View rootView = inflater.inflate(R.layout.fragment_movie_dvd, container, false);
        ListView listView = (ListView) rootView.findViewById(R.id.listview_movies_dvd);
        listView.setAdapter(mMovieAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView adapterView, View view, int position, long l){
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

        //updateMovieList();
        db.close();
        return rootView;

    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (onDVDClickedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }


    public void updateMovieList(){
        FetchMovieData fetchMovieData = new FetchMovieData(this, getActivity().getApplicationContext());
        fetchMovieData.execute("DVD");
    }

    public void update(Cursor results) {
        //mMovieAdapter.clear();
        // so notifyDataSetChanged() doesn't get called too often
        //mMovieAdapter.setNotifyOnChange(false);
        if(results != null) {
            mMovieAdapter.changeCursor(results);

        }
//        mMovieAdapter.notifyDataSetChanged();
//        mMovieAdapter.setNotifyOnChange(true);
    }

    public void updateSelection(Bitmap poster){
        mCallback.onDVDClicked(poster);
    }


    public interface onDVDClickedListener {
        public void onDVDClicked(Bitmap poster);
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
            updateMovieList();
            return true;
        }



        return super.onOptionsItemSelected(item);
    }

}
