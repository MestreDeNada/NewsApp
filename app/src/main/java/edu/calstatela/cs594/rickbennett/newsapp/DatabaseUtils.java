package edu.calstatela.cs594.rickbennett.newsapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import static edu.calstatela.cs594.rickbennett.newsapp.DatabaseContract.Articles.*;

/**
 * Created by Rick on 7/30/17.
 */

// Homework 4: Created this Database Utils class to make MainActivity and NewsAdapter cleaner.

public class DatabaseUtils {

    public static Cursor getAllFromDatabase(SQLiteDatabase db) {
        Cursor cursor = db.query(
                TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );
        return cursor;
    }

    public static void deleteAllFromDatabase(SQLiteDatabase db) {
        db.delete(TABLE_NAME, null, null);
    }

    public static void insertArrayListToDataBase(SQLiteDatabase db, ArrayList<NewsItem> articles) {

        db.beginTransaction();
        try {
            for (NewsItem item : articles) {
                ContentValues cv = new ContentValues();
                cv.put(COLUMN_AUTHOR, item.getAuthor());
                cv.put(COLUMN_DESCRIPTION, item.getDescription());
                cv.put(COLUMN_TITLE, item.getTitle());
                cv.put(COLUMN_URL, item.getUrl());
                cv.put(COLUMN_URL_TO_IMAGE, item.getUrlToImage());
                cv.put(COLUMN_PUBLISHED_AT, item.getTimePublished());
                db.insert(TABLE_NAME, null, cv);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public static void refreshDatabase(Context context) {
        ArrayList<NewsItem> newsStories;
        URL url = NetworkUtils.buildUrl();

        SQLiteDatabase db = new DatabaseHelper(context).getWritableDatabase();

        try {
            deleteAllFromDatabase(db);
            String json = NetworkUtils.getResponseFromHttpUrl(url);
            newsStories = NetworkUtils.parseJSON(json);
            insertArrayListToDataBase(db, newsStories);

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        db.close();
    }
}