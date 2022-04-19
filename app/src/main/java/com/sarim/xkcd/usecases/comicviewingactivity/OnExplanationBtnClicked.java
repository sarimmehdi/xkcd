package com.sarim.xkcd.usecases.comicviewingactivity;

import android.content.Context;
import android.content.Intent;

import com.sarim.xkcd.databinding.ComicBinding;
import com.sarim.xkcd.ui.ExplanationWebViewActivity;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class OnExplanationBtnClicked {

    private final ComicBinding comicBinding;
    private final Context context;

    @Inject
    public OnExplanationBtnClicked(ComicBinding comicBinding, Context context) {
        this.comicBinding = comicBinding;
        this.context = context;
    }

    /**
     * Open a new activity where a webpage, with the comic's explanation, will be displayed
     */
    @Inject
    public void execute() {
        comicBinding.setOnExplanationClicked(comic -> {
            Intent intent = new Intent(context, ExplanationWebViewActivity.class);
            intent.putExtra("comic", comic);
            context.startActivity(intent);
        });
    }
}
