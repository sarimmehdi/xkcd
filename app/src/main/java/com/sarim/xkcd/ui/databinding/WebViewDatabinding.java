package com.sarim.xkcd.ui.databinding;

import android.util.Log;
import android.webkit.WebView;

import androidx.databinding.BindingAdapter;

public class WebViewDatabinding {

    @BindingAdapter({"loadUrl"})
    public static void loadUrl(WebView view, String url) {
        view.loadUrl(url);
    }
}
