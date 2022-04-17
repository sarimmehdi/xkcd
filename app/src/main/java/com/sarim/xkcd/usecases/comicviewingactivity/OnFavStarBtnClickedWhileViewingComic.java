package com.sarim.xkcd.usecases.comicviewingactivity;


import com.sarim.xkcd.ViewModel;
import com.sarim.xkcd.databinding.ComicBinding;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class OnFavStarBtnClickedWhileViewingComic {

    private final ComicBinding comicBinding;
    private final ViewModel viewModel;

    @Inject
    public OnFavStarBtnClickedWhileViewingComic(ComicBinding comicBinding, ViewModel viewModel) {
        this.comicBinding = comicBinding;
        this.viewModel = viewModel;
    }

    @Inject
    public void execute() {
        comicBinding.setFavComicClickListener(comic -> {
            comic.setFavorite(!comic.isFavorite());
            comicBinding.setComic(comic);
            comicBinding.executePendingBindings();
            Picasso.get().load(comic.getImg());

            // comic that was set to bein
            if (comic.isFavorite()) {
                viewModel.insertComic(comic);
            }
            else {
                viewModel.deleteComic(comic);
            }
        });
    }
}
