package com.sarim.xkcd.ui;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.sarim.xkcd.R;
import com.sarim.xkcd.comic.Comic;
import com.sarim.xkcd.databinding.WebviewBinding;

public class WebViewActivity extends AppCompatActivity {

    WebviewBinding webviewBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        webviewBinding = DataBindingUtil.setContentView(this, R.layout.webview);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Bundle data = getIntent().getExtras();
        Comic comic = data.getParcelable("comic");
        webviewBinding.setComic(comic);
        webviewBinding.executePendingBindings();
    }
}
