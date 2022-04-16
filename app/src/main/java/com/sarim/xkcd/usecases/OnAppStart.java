package com.sarim.xkcd.usecases;

import android.content.Context;

import androidx.core.content.ContextCompat;

import com.sarim.xkcd.R;
import com.sarim.xkcd.ViewModel;
import com.sarim.xkcd.databinding.ComicListBinding;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class OnAppStart {

    private final ViewModel viewModel;
    private final ComicListBinding comicListBinding;
    private final Context context;

    @Inject
    public OnAppStart(ViewModel viewModel, ComicListBinding comicListBinding, Context context) {
        this.viewModel = viewModel;
        this.comicListBinding = comicListBinding;
        this.context = context;
    }

    public void execute() {
        viewModel.setFavoriteTab(false);
        viewModel.getComicsFromServer(0);
        comicListBinding.favoritesTab.setBackgroundColor(
                ContextCompat.getColor(context, R.color.white)
        );
        comicListBinding.allComicsTab.setBackgroundColor(
                ContextCompat.getColor(context, R.color.silver)
        );
    }
}