package com.sarim.xkcd.usecases.mainactivity;

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
                    if (viewModel.isFavoriteTab()) {
                        int pageNumToCheckFavComicsFor = Integer.parseInt(inputByUser);
                        if (viewModel.canChangeFavComicsPage(pageNumToCheckFavComicsFor)) {
                            viewModel.setCurrPageFavComics(pageNumToCheckFavComicsFor);
                            viewModel.deleteOnlyNonFavoriteComicsOnDevice();
                            viewModel.forceRefresh();
                        }
                        else {
                            comicListBinding.editTextPageNumber.setText(
                                    String.valueOf(viewModel.getCurrPageFavComics())
                            );
                        }
                    }
                    else {
                        int pageNumToCheckAllComicsFor = Integer.parseInt(inputByUser);
                        viewModel.canChangeAllComicsPage(pageNumToCheckAllComicsFor, () -> {
                            viewModel.setCurrPageAllComics(pageNumToCheckAllComicsFor);
                            viewModel.deleteOnlyNonFavoriteComicsOnDevice();
                            viewModel.getComicsFromServer(pageNumToCheckAllComicsFor);
                        });
                        comicListBinding.editTextPageNumber.setText(
                                String.valueOf(viewModel.getCurrPageAllComics())
                        );
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
