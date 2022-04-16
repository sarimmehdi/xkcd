package com.sarim.xkcd.di;

import android.content.Context;

import com.sarim.xkcd.ViewModel;
import com.sarim.xkcd.databinding.ComicListBinding;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ProvidersModule {

    private final ViewModel viewModel;
    private final ComicListBinding comicListBinding;
    private final Context context;

    public ProvidersModule(ViewModel viewModel, ComicListBinding comicListBinding, Context context) {
        this.viewModel = viewModel;
        this.comicListBinding = comicListBinding;
        this.context = context;
    }

    @Singleton
    @Provides
    ViewModel provideViewModel() {
        return viewModel;
    }

    @Singleton
    @Provides
    ComicListBinding provideComicListBinding() {
        return comicListBinding;
    }

    @Singleton
    @Provides
    Context provideContext() {
        return context;
    }
}
