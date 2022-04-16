package com.sarim.xkcd.usecases;

import com.sarim.xkcd.ViewModel;
import com.sarim.xkcd.databinding.ComicListBinding;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class OnNextPageBtnClicked {

    private final ViewModel viewModel;
    private final ComicListBinding comicListBinding;

    @Inject
    public OnNextPageBtnClicked(ViewModel viewModel, ComicListBinding comicListBinding) {
        this.viewModel = viewModel;
        this.comicListBinding = comicListBinding;
    }

    @Inject
    public void execute() {
        comicListBinding.setNextPageClickListener(() -> {
            viewModel.deleteAllComicsOnDevice();
            viewModel.incCurrPage();
            viewModel.getComicsFromServer();
        });
    }
}
