package edu.calstatela.cs594.rickbennett.newsapp;

import android.provider.BaseColumns;

/**
 * Created by Rick on 7/28/17.
 */

// Homework 4: Contract class for database. Table name and column names match NewsAPI.org JSON tags

public class DatabaseContract {

    public static final class Articles implements BaseColumns {
        public static final String TABLE_NAME = "articles";
        public static final String COLUMN_AUTHOR = "author";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_URL = "url";
        public static final String COLUMN_URL_TO_IMAGE = "urlToImage";
        public static final String COLUMN_PUBLISHED_AT = "publishedAt";
    }
}
