package com.sarim.xkcd.usecases.comicviewingactivity;

import com.sarim.xkcd.comic.Comic;
import com.sarim.xkcd.databinding.ComicBinding;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class OnComicViewingActivityStart {

    private final ComicBinding comicBinding;
    private final Comic comic;

    @Inject
    public OnComicViewingActivityStart(ComicBinding comicBinding, Comic comic) {
        this.comicBinding = comicBinding;
        this.comic = comic;
    }

    public void execute() {
        comicBinding.setComic(comic);
        comicBinding.executePendingBindings();
    }
}
