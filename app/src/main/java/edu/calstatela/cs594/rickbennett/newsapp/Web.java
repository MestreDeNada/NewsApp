package edu.calstatela.cs594.rickbennett.newsapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

/**
 * Created by Rick on 7/16/17.
 */
public class Web extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        Intent intent = getIntent();
        if (intent.hasExtra(Intent.EXTRA_TEXT)) {
            String url = intent.getStringExtra(Intent.EXTRA_TEXT);
            WebView webView = (WebView) findViewById(R.id.wv_webview);
            webView.loadUrl(url);
        }
    }
}
