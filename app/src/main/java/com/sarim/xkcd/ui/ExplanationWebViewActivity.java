package com.sarim.xkcd.ui;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.sarim.xkcd.R;
import com.sarim.xkcd.comic.Comic;
import com.sarim.xkcd.databinding.ExplanationWebviewBinding;

public class ExplanationWebViewActivity extends AppCompatActivity {

    private ExplanationWebviewBinding explanationWebviewBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        explanationWebviewBinding = DataBindingUtil.setContentView(this, R.layout.explanation_webview);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Bundle data = getIntent().getExtras();
        Comic comic = data.getParcelable("comic");
        explanationWebviewBinding.setComic(comic);
        explanationWebviewBinding.executePendingBindings();
    }

    @Override
    protected void onPause() {
        super.onPause();

        explanationWebviewBinding.explanationWebView.removeAllViews();
        explanationWebviewBinding.explanationWebView.destroy();
    }
}
