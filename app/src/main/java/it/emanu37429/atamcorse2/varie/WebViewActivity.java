package it.emanu37429.atamcorse2.varie;

import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import it.emanu37429.atamcorse2.BaseActivity;
import it.emanu37429.atamcorse2.R;

public class WebViewActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        String url = getIntent().getExtras().getString("url");
        WebView webview = findViewById(R.id.webview1);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setWebChromeClient(new WebChromeClient());
        webview.loadUrl(url);
    }
}
