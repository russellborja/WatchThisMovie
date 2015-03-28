package com.example.russellborja.watchthismovie;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.russellborja.watchthismovie.fragments.MovieDVDFragment;
import com.example.russellborja.watchthismovie.fragments.MovieTheatresFragment;

/**
 * Created by russellborja on 2015-03-22.
 */
public class PagerAdapter extends FragmentPagerAdapter{
    private final String IN_THEATRES = "In Theatres";
    private final String ON_DVD = "On DVD";

    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int arg0){
        switch (arg0){
            case 0:
                return new MovieTheatresFragment();
            case 1:
                return new MovieDVDFragment();
            default:
                break;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle (int position) {
        switch(position){
            case 0:
                return IN_THEATRES;
            case 1:
                return ON_DVD;
            default:
                break;

        }
        return null;
    }
}
