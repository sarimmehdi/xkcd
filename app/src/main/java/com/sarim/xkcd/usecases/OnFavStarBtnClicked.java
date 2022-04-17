package com.sarim.xkcd.usecases;


import com.sarim.xkcd.ViewModel;
import com.sarim.xkcd.comic.Comic;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.function.Consumer;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class OnFavStarBtnClicked {

    private final ViewModel viewModel;

    @Inject
    public OnFavStarBtnClicked(ViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public Consumer<Comic> execute() {
        return comic -> {
            comic.setFavorite(!comic.isFavorite());
            Picasso.get().load(comic.getImg());
            viewModel.updateComicOnDevice(comic);
        };
    }
}
