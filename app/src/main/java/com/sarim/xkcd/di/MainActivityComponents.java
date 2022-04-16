package com.sarim.xkcd.di;

import com.sarim.xkcd.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = ProvidersModule.class)
public interface MainActivityComponents {
    void inject(MainActivity mainActivity);
}
