package com.example.russellborja.watchthismovie;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.russellborja.watchthismovie.fragments.MovieDVDFragment;
import com.example.russellborja.watchthismovie.fragments.MovieTheatresFragment;
import com.example.russellborja.watchthismovie.fragments.SelectionFragment;


public class MainActivity extends ActionBarActivity implements
        SelectionFragment.OnSelectionClickedListener, MovieTheatresFragment.onMovieClickedListener,
        MovieDVDFragment.onDVDClickedListener{

    ViewPager viewpager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        viewpager = (ViewPager) findViewById(R.id.pager);



        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        viewpager.setAdapter(pagerAdapter);


        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            //transaction.add(R.id.list, new MovieTheatresFragment());
            transaction.add(R.id.selection, new SelectionFragment());
            transaction.commit();

        }
    }





    @Override
    protected void onDestroy(){
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    public void onSelectionClicked(Uri uri){
        Log.v("Fragment Transaction: ", uri.toString());
    }

    public void onMovieClicked(Bitmap poster){
        SelectionFragment sf = (SelectionFragment) getSupportFragmentManager().findFragmentById(R.id.selection);
        if (sf != null) {
            sf.updateSelectionContainers(poster);
        }
    }

    public void onDVDClicked(Bitmap poster){
        SelectionFragment sf = (SelectionFragment) getSupportFragmentManager().findFragmentById(R.id.selection);
        if (sf != null) {
            sf.updateSelectionContainers(poster);
        }
    }



}
