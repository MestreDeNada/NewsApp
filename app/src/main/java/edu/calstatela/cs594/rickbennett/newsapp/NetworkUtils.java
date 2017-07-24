package edu.calstatela.cs594.rickbennett.newsapp;

import android.net.Uri;

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
    private static final String key = ""; //Enter your own API key

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
}
