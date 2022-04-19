package com.sarim.xkcd.ui.databinding;

import android.util.Log;
import android.webkit.WebView;

import androidx.databinding.BindingAdapter;

public class WebViewDatabinding {

    /**
     * load webview using the provided url
     * @param view the WebView object we wish to populate with a webpage
     * @param url the url for which we wish to retrieve the webpage
     */
    @BindingAdapter({"loadUrl"})
    public static void loadUrl(WebView view, String url) {
        view.loadUrl(url);
    }
}
