package edu.calstatela.cs594.rickbennett.newsapp;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;


/**
 * Created by Rick on 6/28/17.
 */
public class NetworkUtils {

    private static final String NEWSAPI_BASE_URL = "https://newsapi.org/v1/articles";

    private static final String PARAM_SOURCE = "source";
    private static final String source = "the-next-web";

    private static final String PARAM_SORT = "sortBy";
    private static final String sortBy ="latest";

    private static final String PARAM_APIKEY = "apiKey";
    private static final String key = "9b34213d4efa4824903dd8c2212b85ee"; //Enter your own API key

    public static URL buildUrl() {
        Uri builtUri = Uri.parse(NEWSAPI_BASE_URL).buildUpon()
                .appendQueryParameter(PARAM_SOURCE, source)
                .appendQueryParameter(PARAM_SORT, sortBy)
                .appendQueryParameter(PARAM_APIKEY, key)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");
            return (scanner.hasNext()) ? scanner.next() : null;
        } finally {
            urlConnection.disconnect();
        }
    }

    public static ArrayList<NewsItem> parseJSON(String json) throws JSONException {
        ArrayList<NewsItem> itemList = new ArrayList<>();
        JSONObject metadata = new JSONObject(json);
        JSONArray articles = metadata.getJSONArray("articles");

        for(int i = 0; i < articles.length(); i++){
            JSONObject article = articles.getJSONObject(i);
            String author = article.getString("author");
            String description = article.getString("description");
            String title = article.getString("title");
            String url = article.getString("url");
            String urlToImage = article.getString("urlToImage");
            String timePublished = article.getString("publishedAt");
            NewsItem newsItem = new NewsItem(author, description, title, url, urlToImage, timePublished);
            itemList.add(newsItem);
        }
        return itemList;
    }

    //Homework 4 firebase: Added this method for Firebase job dispatcher.
    //Update news every minute.
    private static final int SCHEDULE_INTERVAL_SECONDS = 60;
    private static final int SYNC_FLEXTIME_SECONDS = 60;
    private static final String NEWS_JOB_TAG = "news_job_tag";
    private static boolean scheduleInitialized;

    synchronized public static void scheduleNewsUpdate(final Context context) {
        Log.d("NetworkUtils", "scheduleNewsUpdate called");
        if(scheduleInitialized) return;

        Log.d("NetworkUtils", "Firebase dispatcher running");
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(context));

        Log.d("NetworkUtil", "updateNewsJob initiated");
        Job updateNewsJob = dispatcher.newJobBuilder()
                .setService(NewsJob.class)
                .setTag(NEWS_JOB_TAG)
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow(SCHEDULE_INTERVAL_SECONDS,
                        SCHEDULE_INTERVAL_SECONDS + SYNC_FLEXTIME_SECONDS))
                .setReplaceCurrent(true)
                .build();

        Log.d("NetworkUtil", "updateNewsJob built");
        dispatcher.schedule(updateNewsJob);
        Log.d("NetworkUtil", "updateNewsJob scheduled");
        Toast.makeText(context, "updateNewsJob scheduled", Toast.LENGTH_LONG).show();
        scheduleInitialized = true;
    }
}
