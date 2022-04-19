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

    /**
     * when you exit the comic viewing activity, you must destroy the threads for the view model
     * and delete any non-favorite comics cached into your Room database
     */
    public void execute() {
        viewModel.deleteOnlyNonFavoriteComicsOnDevice();
        viewModel.quitThreads();
    }
}
