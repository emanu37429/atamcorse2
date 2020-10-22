package it.emanu37429.atamcorse2.trainsfrag;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import androidx.fragment.app.Fragment;

import it.emanu37429.atamcorse2.R;

public class FragmentWebViewVT extends Fragment {

    public FragmentWebViewVT() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_web_view, container, false);
        WebView webview = v.findViewById(R.id.webview1);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setWebChromeClient(new WebChromeClient());
        webview.loadUrl("https://www.trenitalia.com/it/informazioni/Infomobilita/notizie-infomobilita.html");
        return v;
    }
}