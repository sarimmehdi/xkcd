package com.sarim.xkcd.ui;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.sarim.xkcd.R;
import com.sarim.xkcd.databinding.SearchWebviewBinding;

public class SearchWebViewActivity extends AppCompatActivity {

    private SearchWebviewBinding searchWebviewBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        searchWebviewBinding = DataBindingUtil.setContentView(this, R.layout.search_webview);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Bundle data = getIntent().getExtras();
        String searchText = data.getString("search");
        searchWebviewBinding.searchWebView.getSettings().setJavaScriptEnabled(true);
        searchWebviewBinding.searchWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        searchWebviewBinding.setSearchText(searchText);
        searchWebviewBinding.executePendingBindings();
    }
}
