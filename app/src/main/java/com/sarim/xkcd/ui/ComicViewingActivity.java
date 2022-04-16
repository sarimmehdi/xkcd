package com.sarim.xkcd.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.sarim.xkcd.R;
import com.sarim.xkcd.comic.Comic;
import com.sarim.xkcd.databinding.ComicBinding;
import com.sarim.xkcd.ui.interfaces.ExplanationClickListener;

public class ComicViewingActivity extends AppCompatActivity implements ExplanationClickListener {

    private ComicBinding comicBinding;

    private Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        comicBinding = DataBindingUtil.setContentView(this, R.layout.comic);
    }

    @Override
    protected void onStart() {
        super.onStart();

        context = this;
        Bundle data = getIntent().getExtras();
        Comic comic = data.getParcelable("comic");
        comicBinding.setComic(comic);
        comicBinding.setOnExplanationClicked(this);
        comicBinding.executePendingBindings();
    }

    @Override
    public void explanationClicked(Comic comic) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra("comic", comic);
        context.startActivity(intent);
    }
}
