package com.sarim.xkcd.usecases.mainactivity;

import android.view.View;

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
            if (viewModel.isFavoriteTab()) {
                int pageNumToCheckFavComicsFor = viewModel.getCurrPageFavComics() + 1;
                if (viewModel.canChangeFavComicsPage(pageNumToCheckFavComicsFor)) {
                    viewModel.setCurrPageFavComics(pageNumToCheckFavComicsFor);
                    viewModel.deleteOnlyNonFavoriteComicsOnDevice();
                    viewModel.forceRefresh();
                }
            }
            else {
                int pageNumToCheckAllComicsFor = viewModel.getCurrPageAllComics() + 1;
                viewModel.canChangeAllComicsPage(pageNumToCheckAllComicsFor, () -> {
                    viewModel.setCurrPageAllComics(pageNumToCheckAllComicsFor);
                    viewModel.deleteOnlyNonFavoriteComicsOnDevice();
                    comicListBinding.comicRetrievalProgressBar.setVisibility(View.VISIBLE);
                    viewModel.getComicsFromServer(pageNumToCheckAllComicsFor);
                });
            }
        });
    }
}
