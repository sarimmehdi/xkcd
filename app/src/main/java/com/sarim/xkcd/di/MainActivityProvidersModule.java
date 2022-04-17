package com.sarim.xkcd.di;

import android.content.Context;

import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.LifecycleOwner;

import com.sarim.xkcd.ViewModel;
import com.sarim.xkcd.comic.Comic;
import com.sarim.xkcd.databinding.ComicBinding;
import com.sarim.xkcd.databinding.ComicListBinding;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class MainActivityProvidersModule {

    private final ViewModel viewModel;
    private final ComicListBinding comicListBinding;
    private final NotificationManagerCompat notificationManager;
    private final LifecycleOwner lifecycleOwner;
    private final Context context;

    public MainActivityProvidersModule(ViewModel viewModel, ComicListBinding comicListBinding,
                                       NotificationManagerCompat notificationManager,
                                       LifecycleOwner lifecycleOwner, Context context) {
        this.viewModel = viewModel;
        this.comicListBinding = comicListBinding;
        this.notificationManager = notificationManager;
        this.lifecycleOwner = lifecycleOwner;
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
    NotificationManagerCompat provideNotificationManager() {
        return notificationManager;
    }

    @Singleton
    @Provides
    LifecycleOwner provideLifecycleOwner() {
        return lifecycleOwner;
    }

    @Singleton
    @Provides
    Context provideContext() {
        return context;
    }
}
