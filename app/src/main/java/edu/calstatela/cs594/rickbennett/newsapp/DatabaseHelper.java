package edu.calstatela.cs594.rickbennett.newsapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Rick on 7/29/17.
 */

// Homework 4: Database helper class has sql queries to create table, and drop table if new version.

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "newsapp.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_NEWS_ARTICLE_TABLE = "CREATE TABLE " +
                DatabaseContract.Articles.TABLE_NAME + " (" +
                DatabaseContract.Articles._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                DatabaseContract.Articles.COLUMN_AUTHOR + " TEXT," +
                DatabaseContract.Articles.COLUMN_DESCRIPTION + " TEXT," +
                DatabaseContract.Articles.COLUMN_TITLE + " TEXT," +
                DatabaseContract.Articles.COLUMN_URL + " TEXT," +
                DatabaseContract.Articles.COLUMN_URL_TO_IMAGE + " TEXT," +
                DatabaseContract.Articles.COLUMN_PUBLISHED_AT + " TEXT" +
                ");";

        db.execSQL(CREATE_NEWS_ARTICLE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.Articles.TABLE_NAME);
        onCreate(db);
    }
}
