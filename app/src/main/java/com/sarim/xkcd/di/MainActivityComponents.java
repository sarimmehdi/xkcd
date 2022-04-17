package com.sarim.xkcd.di;

import com.sarim.xkcd.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = MainActivityProvidersModule.class)
public interface MainActivityComponents {
    void inject(MainActivity mainActivity);
}
