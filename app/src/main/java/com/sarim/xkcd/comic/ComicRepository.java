package com.sarim.xkcd.comic;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ComicRepository {
    private final ComicDao comicDao;
    private final LiveData<List<Comic>> allComics;

    public ComicRepository(@NonNull Application application) {
        ComicDatabase comicDatabase = ComicDatabase.getInstance(application);
        comicDao = comicDatabase.comicDao();
        allComics = comicDao.getAllComics();
    }

    public ComicRepository(ComicDao comicDao) {
        this.comicDao = comicDao;
        allComics = comicDao.getAllComics();
    }

    public void insert(Comic comic) {
        comicDao.insert(comic);
    }

    public void update(Comic comic) {
        comicDao.update(comic);
    }

    public void delete(Comic comic) {
        comicDao.delete(comic);
    }

    public void deleteNonFavorites() {
        comicDao.deleteNonFavorites();
    }

    public LiveData<List<Comic>> getAllComics() {
        return allComics;
    }
}
