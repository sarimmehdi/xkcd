package com.sarim.xkcd;

import static org.junit.Assert.*;

import android.app.Application;
import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import com.sarim.xkcd.comic.ComicDatabase;
import com.sarim.xkcd.comic.ComicRepository;

import org.junit.Before;
import org.junit.Test;

public class ViewModelTest {

    ViewModel viewModel;

    @Before
    public void setUp() {
        Context context = ApplicationProvider.getApplicationContext();
        ComicDatabase comicDatabase = Room.inMemoryDatabaseBuilder(context, ComicDatabase.class)
                .allowMainThreadQueries().build();
        ComicRepository comicRepository = new ComicRepository(comicDatabase.comicDao());
        viewModel = new ViewModel(comicRepository, (Application) context);
        viewModel.createBackgroundThreads();
    }

    @Test
    public void getRecentlyAddedComic() {
    }

    @Test
    public void getCurrPageAllComics() {
    }

    @Test
    public void setCurrPageAllComics() {
    }

    @Test
    public void getCurrPageFavComics() {
    }

    @Test
    public void setCurrPageFavComics() {
    }

    @Test
    public void setFavoriteTab() {
    }
}