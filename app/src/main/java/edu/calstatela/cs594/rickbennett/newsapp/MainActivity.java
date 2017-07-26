package edu.calstatela.cs594.rickbennett.newsapp;

import android.support.v4.app.LoaderManager;
import android.content.Intent;
//import android.content.Loader;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

//Homework 4: implement LoaderManager.LoaderCallbacks<ArrayList> for AsyncTaskLoader. Let Android Studio override the three callbacks.
public class MainActivity extends AppCompatActivity implements NewsAdapter.NewsItemClickListener, LoaderManager.LoaderCallbacks<ArrayList> {

    //Homework 4: Loader identification integer.
    private static final int FETCH_NEWS_LOADER = 1;

    //Homework 4: String key value pair for use in creating the Bundle.
    private static final String NEWS_SEARCH_URL_EXTRA = "query";

    private RecyclerView mRecyclerView;

    private NewsAdapter mNewsAdapter;

    private ProgressBar mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_news);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);

        mNewsAdapter = new NewsAdapter(this);

        mRecyclerView.setAdapter(mNewsAdapter);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        makeNewsSearchQuery();
    }

    @Override
    public void onClick(int clickedItemIndex) {
        String url = mNewsAdapter.getNewsItem(clickedItemIndex).getUrl();
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
            mNewsAdapter.setNewsData(null);
            makeNewsSearchQuery();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Homework 4: Override the following three callbacks for AsyncTaskLoader.
    @Override
    public Loader<ArrayList> onCreateLoader(int id, final Bundle args) {
        //Homework 4: return a new AsyncTaskLoader of type ArrayList and context this as anonymous inner class.
        //Override onStartLoading and loadInBackground.
        return new AsyncTaskLoader<ArrayList>(this) {
            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                //If Bundle called args is null, just return.
                //Must set args parameter in onCreateLoader to final to access here in inner class.
                if (args == null) return;
                //Logic from onPreExecute goes here.
                mLoadingIndicator.setVisibility(View.VISIBLE);
                //Must forceLoad or will hang here.
                forceLoad();
            }
            @Override
            public ArrayList<NewsItem> loadInBackground() {
                //Get news search URL string from the args parameter.
                String newsSearchUrlString = args.getString(NEWS_SEARCH_URL_EXTRA);
                //If String is null or empty, return null.
                if (newsSearchUrlString == null || TextUtils.isEmpty(newsSearchUrlString)) return null;
                //Logic from doInBackground goes here.
                try {
                    //Change string to URL, send to NetworkUtils, and get back a JSON string.
                    String json = NetworkUtils.getResponseFromHttpUrl(new URL(newsSearchUrlString));
                    //Return the retrieved JSON string as an ArrayList of NewsItems.
                    return NetworkUtils.parseJSON(json);
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<ArrayList> loader, ArrayList news) {
        //Homework 4: Logic from onPostExecute goes here.
        mLoadingIndicator.setVisibility(View.GONE);
        //Set newsAdapter with array list.
        if (news != null)  mNewsAdapter.setNewsData(news);
    }

    //Homework 4: This method is not used but is part of the interface implemented.
    @Override
    public void onLoaderReset(Loader<ArrayList> loader) {
    }

    //Homework 4: Delete the AsyncTask class.
/*    public class FetchNewsTask extends AsyncTask<URL, Void, ArrayList<NewsItem>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<NewsItem> doInBackground(URL... params) {
            URL searchUrl = params[0];
            ArrayList<NewsItem> newsSearchResults = null;
            try {
                String json = NetworkUtils.getResponseFromHttpUrl(searchUrl);
                newsSearchResults = NetworkUtils.parseJSON(json);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return newsSearchResults;
        }

        @Override
        protected void onPostExecute(ArrayList<NewsItem> news) {
            mLoadingIndicator.setVisibility(View.GONE);
            if (news != null) { mNewsAdapter.setNewsData(news); }
        }

    }
*/
    private void makeNewsSearchQuery() {
        URL newsSearchUrl = NetworkUtils.buildUrl();
        //Homework 4: Remove call to AsyncTask.
        //new FetchNewsTask().execute(newsSearchUrl);

        //Homework 4:
        //Create a Bundle that contains the searchUrl as a String.
        Bundle queryBundle = new Bundle();
        queryBundle.putString(NEWS_SEARCH_URL_EXTRA, newsSearchUrl.toString());
        //Call getSupportLoaderManager and store it in a variable called loaderManager.
        LoaderManager loaderManager = getSupportLoaderManager();
        //Call getLoader and pass it the loader identification.
        Loader<ArrayList> newsLoader = loaderManager.getLoader(FETCH_NEWS_LOADER);
        //Initialize loader.
        if (newsLoader == null) loaderManager.initLoader(FETCH_NEWS_LOADER, queryBundle, this);
    }
}
