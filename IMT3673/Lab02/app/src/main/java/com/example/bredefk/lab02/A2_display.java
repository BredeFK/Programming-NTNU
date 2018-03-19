package com.example.bredefk.lab02;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class A2_display extends AppCompatActivity {

    private static String subURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a2_display);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            subURL = extras.getString("rssURL");
        }

        WebView w = findViewById(R.id.webviewA2);
        w.setWebViewClient(new WebViewClient());
        w.getSettings().setJavaScriptEnabled(true);

        w.loadUrl(subURL);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(A2_display.this, A1_list.class));
    }
}
