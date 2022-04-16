package com.sarim.xkcd.usecases;

import android.text.Editable;
import android.text.TextWatcher;

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
        comicListBinding.editTextPageNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    int pageNum = Integer.parseInt(editable.toString());
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
            }
        });
    }
}
