package com.sarim.xkcd.di;

import android.content.Context;

import com.sarim.xkcd.ViewModel;
import com.sarim.xkcd.comic.Comic;
import com.sarim.xkcd.databinding.ComicBinding;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ComicViewingActivityProvidersModule {

    private final ViewModel viewModel;
    private final ComicBinding comicBinding;
    private final Comic comic;
    private final Context context;

    public ComicViewingActivityProvidersModule(ViewModel viewModel, ComicBinding comicBinding,
                                               Comic comic, Context context) {
        this.viewModel = viewModel;
        this.comicBinding = comicBinding;
        this.comic = comic;
        this.context = context;
    }

    @Singleton
    @Provides
    ViewModel provideViewModel() {
        return viewModel;
    }

    @Singleton
    @Provides
    ComicBinding provideComicBinding() {
        return comicBinding;
    }

    @Singleton
    @Provides
    Comic provideComic() {
        return comic;
    }

    @Singleton
    @Provides
    Context provideContext() {
        return context;
    }
}
