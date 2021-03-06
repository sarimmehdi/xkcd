package com.sarim.xkcd.usecases.mainactivity;

import android.content.Context;

import androidx.core.content.ContextCompat;

import com.sarim.xkcd.R;
import com.sarim.xkcd.ViewModel;
import com.sarim.xkcd.databinding.ComicListBinding;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class OnFavBtnClicked {

    private final ViewModel viewModel;
    private final ComicListBinding comicListBinding;
    private final Context context;

    @Inject
    public OnFavBtnClicked(ViewModel viewModel, ComicListBinding comicListBinding, Context context) {
        this.viewModel = viewModel;
        this.comicListBinding = comicListBinding;
        this.context = context;
    }

    /**
     * switch to the favorite comics tab and force a refresh so that the RecyclerView
     * displays the favorite comics for that page only
     */
    @Inject
    public void execute() {
        comicListBinding.setFavoritesClickListener(() -> {
            viewModel.deleteOnlyNonFavoriteComicsOnDevice();
            viewModel.forceRefresh();
            viewModel.setFavoriteTab(true);
            comicListBinding.favoritesTab.setBackgroundColor(
                    ContextCompat.getColor(context, R.color.silver)
            );
            comicListBinding.allComicsTab.setBackgroundColor(
                    ContextCompat.getColor(context, R.color.white)
            );
        });
    }
}
