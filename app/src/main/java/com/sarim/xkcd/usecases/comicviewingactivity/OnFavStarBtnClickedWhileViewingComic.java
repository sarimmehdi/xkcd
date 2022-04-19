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

    /**
     * save the comic in the persistent Room database and cache its image on the device
     */
    @Inject
    public void execute() {
        comicBinding.setFavComicClickListener(comic -> {
            comic.setFavorite(!comic.isFavorite());
            comicBinding.setComic(comic);
            comicBinding.executePendingBindings();

            // cache the image on the device
            Picasso.get().load(comic.getImg());

            // if a comic is now a favorite, that means it must be added to the Room database
            if (comic.isFavorite()) {
                viewModel.insertComic(comic);
            }
            // otherwise delete it from the Room database
            else {
                viewModel.deleteComic(comic);
            }
        });
    }
}
