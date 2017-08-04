package edu.calstatela.cs594.rickbennett.newsapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.LoaderManager;
import android.content.Intent;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

//Homework 4: implement LoaderManager.LoaderCallbacks<ArrayList> for AsyncTaskLoader. Let Android Studio override the three callbacks.
public class MainActivity extends AppCompatActivity implements NewsAdapter.NewsItemClickListener, LoaderManager.LoaderCallbacks<Void> {

    //Homework 4: Loader identification integer.
    private static final int FETCH_NEWS_LOADER = 1;

    //Homework 4 database: Do not need this string anymore since not using a bundle.
    //Homework 4: String key value pair for use in creating the Bundle.
    //private static final String NEWS_SEARCH_URL_EXTRA = "query";

    private RecyclerView mRecyclerView;

    private NewsAdapter mNewsAdapter;

    private ProgressBar mLoadingIndicator;

    //Homework 4 database: Declare a variable of type SQLiteDatabase.
    private SQLiteDatabase mDb;

    //Homework 4 database: Declare a cursor variable.
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_news);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);

        //Homework 4 database: move these two items to the AsyncTaskLoader.
        //mNewsAdapter = new NewsAdapter(cursor, this);
        //mRecyclerView.setAdapter(mNewsAdapter);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        loadNews();
    }

    //Homework 4 database: Add cursor and change code to get url from database.
    @Override
    public void onClick(Cursor cursor, int clickedItemIndex) {
        cursor.moveToPosition(clickedItemIndex);
        String url = cursor.getString(cursor.getColumnIndex(DatabaseContract.Articles.COLUMN_URL));
        Intent intent = new Intent(MainActivity.this, Web.class);
        intent.putExtra(Intent.EXTRA_TEXT, url);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_refresh) {
            loadNews();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Homework 4 database: Clean up the Loader for use with database using DatabaseUtils.
    // Instead of returning array list, return Void. The array list is dealt with in utils.
    //Homework 4: Override the following three callbacks for AsyncTaskLoader.
    @Override
    public Loader<Void> onCreateLoader(int id, final Bundle args) {
        //Homework 4: return a new AsyncTaskLoader of type ArrayList and context this as anonymous inner class.
        //Override onStartLoading and loadInBackground.
        Log.d("test", "loader initialized");
        return new AsyncTaskLoader<Void>(this) {
            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                mLoadingIndicator.setVisibility(View.VISIBLE);
            }
            @Override
            public Void loadInBackground() {
                Log.d("test", "loading");
                //Homework 4 database: DatabaseUtils has function to load the database.
                DatabaseUtils.refreshDatabase(MainActivity.this);
                return null;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Void> loader, Void news) {
        mLoadingIndicator.setVisibility(View.GONE);
        //Homework 4 database: Create DatabaseHelper instance, get readable database reference, store in database variable.
        // Store all from database in cursor.
        // Adapter and set adapter moved here from on create.
        mDb = new DatabaseHelper(this).getReadableDatabase();
        cursor = DatabaseUtils.getAllFromDatabase(mDb);
        mNewsAdapter = new NewsAdapter(cursor, this);
        mRecyclerView.setAdapter(mNewsAdapter);
        mNewsAdapter.notifyDataSetChanged();
    }

    //Homework 4: This method is not used but is part of the interface implemented.
    @Override
    public void onLoaderReset(Loader<Void> loader) {
    }

    //Homework 4 database: Remove code for bundle.
    private void loadNews() {
        //Call getSupportLoaderManager and store it in a variable called loaderManager.
        LoaderManager loaderManager = getSupportLoaderManager();
        loaderManager.restartLoader(FETCH_NEWS_LOADER, null, this).forceLoad();
    }
}
