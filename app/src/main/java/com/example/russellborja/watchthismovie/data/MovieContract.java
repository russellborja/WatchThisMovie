package com.example.russellborja.watchthismovie.data;

import android.provider.BaseColumns;

/**
 * Created by russellborja on 2015-03-23.
 */
public class MovieContract {

    public static final int COL_MOVIE_TITLE = 2;
    public static final int COL_RELEASE_DATE = 3;
    public static final int COL_RATING= 4;
    public static final int COL_IN_THEATRES = 5;
    public static final int COL_IS_SELECTED = 6;
    public static final int COL_IMAGE=7;

    public static final class MovieEntry implements BaseColumns {

        public static final String TABLE_NAME = "movies";

        public static final String COLUMN_MOVIE_TITLE = "movie_title";

        public static final String COLUMN_RATING = "rating";

        public static final String COLUMN_RELEASE_DATE = "release_date";

        public static final String COLUMN_MOVIE_POSTER = "movie_poster";

        public static final String COLUMN_IN_THEATRES = "in_theatres";

        public static final String COLUMN_IS_SELECTED = "is_selected";

    }
}
