package com.sarim.xkcd.usecases.comicviewingactivity;

import com.sarim.xkcd.ViewModel;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class OnComicViewingActivityEnd {

    private final ViewModel viewModel;

    @Inject
    public OnComicViewingActivityEnd(ViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public void execute() {
        viewModel.deleteOnlyNonFavoriteComicsOnDevice();
        viewModel.quitThreads();
    }
}
