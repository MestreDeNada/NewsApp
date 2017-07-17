package edu.calstatela.cs594.rickbennett.newsapp;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements NewsAdapter.NewsItemClickListener {

    private RecyclerView mRecyclerView;

    private NewsAdapter mNewsAdapter;

    private ProgressBar mLoadingIndicator;

    public final static String URLKEY = "url";

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
            intent.putExtra(URLKEY, url);
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

    public class FetchNewsTask extends AsyncTask<URL, Void, ArrayList<NewsItem>> {

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

    private void makeNewsSearchQuery() {
        URL newsSearchUrl = NetworkUtils.buildUrl();
        new FetchNewsTask().execute(newsSearchUrl);
    }
}
