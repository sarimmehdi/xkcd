package com.sarim.xkcd.usecases.mainactivity;

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
            if (viewModel.isNotFavoriteTab()) {
                viewModel.deleteOnlyNonFavoriteComicsOnDevice();
                viewModel.getComicsFromServer(viewModel.getCurrPageAllComics() + 1);
            }
            else {
                viewModel.setCurrPageFavComics(viewModel.getCurrPageFavComics() + 1);
                viewModel.deleteOnlyNonFavoriteComicsOnDevice();
                viewModel.forceRefresh();
            }
        });
    }
}
