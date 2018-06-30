package za.co.samtakie.samtakieradio.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

import za.co.samtakie.samtakieradio.R;

public class privacy_policy extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);

        String url = getIntent().getExtras().getString("url");
        String title = getIntent().getExtras().getString("title");

        setTitle(title);

        WebView myWebView = (WebView) findViewById(R.id.webview);
        myWebView.loadUrl(url);
    }
}