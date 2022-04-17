package com.sarim.xkcd.usecases.comicviewingactivity;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.sarim.xkcd.comic.Comic;
import com.sarim.xkcd.databinding.ComicBinding;
import com.sarim.xkcd.ui.ExplanationWebViewActivity;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class OnExplanationBtnClicked {

    private final ComicBinding comicBinding;
    private final Comic comic;
    private final Context context;

    @Inject
    public OnExplanationBtnClicked(ComicBinding comicBinding, Comic comic, Context context) {
        this.comicBinding = comicBinding;
        this.comic = comic;
        this.context = context;
    }

    @Inject
    public void execute() {
        comicBinding.explanationBtn.setOnClickListener(view -> {
            Intent intent = new Intent(context, ExplanationWebViewActivity.class);
            intent.putExtra("comic", comic);
            context.startActivity(intent);
        });
    }
}
