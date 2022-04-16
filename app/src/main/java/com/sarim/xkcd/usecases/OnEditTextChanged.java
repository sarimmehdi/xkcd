package com.sarim.xkcd.usecases;

import android.view.KeyEvent;

import com.sarim.xkcd.ViewModel;
import com.sarim.xkcd.databinding.ComicListBinding;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class OnEditTextChanged {

    private final ViewModel viewModel;
    private final ComicListBinding comicListBinding;

    @Inject
    public OnEditTextChanged(ViewModel viewModel, ComicListBinding comicListBinding) {
        this.viewModel = viewModel;
        this.comicListBinding = comicListBinding;
    }

    @Inject
    public void execute() {
        comicListBinding.editTextPageNumber.setOnKeyListener((view, i, keyEvent) -> {
            if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (i == KeyEvent.KEYCODE_ENTER)) {
                try {
                    String inputByUser = comicListBinding.editTextPageNumber.getText().toString();
                    int pageNum = Integer.parseInt(inputByUser);
                    if (viewModel.isNotFavoriteTab()) {
                        viewModel.deleteOnlyNonFavoriteComicsOnDevice();
                        viewModel.getComicsFromServer(pageNum);
                    }
                    else {
                        viewModel.setCurrPageFavComics(pageNum);
                        viewModel.deleteOnlyNonFavoriteComicsOnDevice();
                        viewModel.forceRefresh();
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                return true;
            }
            return false;
        });
    }
}
