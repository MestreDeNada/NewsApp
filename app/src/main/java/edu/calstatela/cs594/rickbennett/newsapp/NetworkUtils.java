package edu.calstatela.cs594.rickbennett.newsapp;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Rick on 6/28/17.
 */
public class NetworkUtils {

    private static final String NEWSAPI_BASE_URL = "https://newsapi.org/v1/articles";

    private static final String PARAM_SOURCE = "source=the-next-web";

    private static final String PARAM_SORT = "sortBy=latest";

    private static final String PARAM_APIKEY = "9b34213d4efa4824903dd8c2212b85ee";

    public static URL buildUrl(String newsQuery, String source, String sortBy, String apiKey) {
        Uri builtUri = Uri.parse(NEWSAPI_BASE_URL).buildUpon()
                .appendQueryParameter(PARAM_SOURCE, source)
                .appendQueryParameter(PARAM_SORT, sortBy)
                .appendQueryParameter(PARAM_APIKEY, apiKey)
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

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
