package com.sarim.xkcd.usecases.mainactivity;

import com.sarim.xkcd.ViewModel;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class OnAppExit {

    private final ViewModel viewModel;

    @Inject
    public OnAppExit(ViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public void execute() {
        viewModel.deleteOnlyNonFavoriteComicsOnDevice();
        viewModel.quitThreads();
    }
}
