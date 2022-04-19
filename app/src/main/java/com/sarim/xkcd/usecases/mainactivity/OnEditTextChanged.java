package com.sarim.xkcd.usecases.mainactivity;

import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.sarim.xkcd.R;
import com.sarim.xkcd.ViewModel;
import com.sarim.xkcd.databinding.ComicListBinding;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class OnEditTextChanged {

    private final ViewModel viewModel;
    private final ComicListBinding comicListBinding;
    private final Context context;

    @Inject
    public OnEditTextChanged(ViewModel viewModel, ComicListBinding comicListBinding, Context context) {
        this.viewModel = viewModel;
        this.comicListBinding = comicListBinding;
        this.context = context;
    }

    /**
     * The text written by the user is parsed. If it is just a number, we retrieve the exact comic.
     * Otherwise, a webpage is opened up where the textprovided by the user is used to search for
     * a relevant comic
     */
    @Inject
    public void execute() {
        comicListBinding.editTextPageNumber.setOnKeyListener((view, i, keyEvent) -> {
            if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (i == KeyEvent.KEYCODE_ENTER)) {
                String inputByUser = comicListBinding.editTextPageNumber.getText().toString();
                try {
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
                            comicListBinding.comicRetrievalProgressBar.setVisibility(View.VISIBLE);
                            viewModel.getComicsFromServer(pageNumToCheckAllComicsFor);
                        });
                        comicListBinding.editTextPageNumber.setText(
                                String.valueOf(viewModel.getCurrPageAllComics())
                        );
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(context, context.getString(R.string.toast_invalid_page_number,
                            inputByUser), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                return true;
            }
            return false;
        });
    }

    public void testExecute(String inputByUser) {
        try {
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
                    comicListBinding.comicRetrievalProgressBar.setVisibility(View.VISIBLE);
                    viewModel.getComicsFromServer(pageNumToCheckAllComicsFor);
                });
                comicListBinding.editTextPageNumber.setText(
                        String.valueOf(viewModel.getCurrPageAllComics())
                );
            }
        } catch (NumberFormatException e) {
            Toast.makeText(context, context.getString(R.string.toast_invalid_page_number,
                    inputByUser), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
