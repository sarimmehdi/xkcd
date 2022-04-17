package com.sarim.xkcd.di;

import com.sarim.xkcd.ui.ComicViewingActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = ComicViewingActivityProvidersModule.class)
public interface ComicViewingActivityComponents {
    void inject(ComicViewingActivity comicViewingActivity);
}
