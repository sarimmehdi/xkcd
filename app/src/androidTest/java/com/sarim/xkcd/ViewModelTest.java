package com.sarim.xkcd;

import static org.junit.Assert.*;

import android.app.Application;
import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import com.google.gson.Gson;
import com.sarim.xkcd.comic.Comic;
import com.sarim.xkcd.comic.ComicDatabase;
import com.sarim.xkcd.comic.ComicRepository;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class ViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    ViewModel viewModel;
    LifecycleOwner lifecycleOwner;

    @Before
    public void setUp() {
        Context context = ApplicationProvider.getApplicationContext();
        ComicDatabase comicDatabase = Room.inMemoryDatabaseBuilder(context, ComicDatabase.class)
                .allowMainThreadQueries().build();
        ComicRepository comicRepository = new ComicRepository(comicDatabase.comicDao());
        viewModel = new ViewModel(comicRepository, (Application) context);
        viewModel.createBackgroundThreads();
        lifecycleOwner = new TestLifecycleOwner();
    }

    @Test
    public void testGetRecentlyAddedComic() {
        viewModel.getRecentlyAddedComic(comic -> assertTrue(comic.getNum() >= 2607));
    }

    @Test
    public void testGetCurrPageAllComics() {
        assertEquals(viewModel.getCurrPageAllComics(), 0);
        viewModel.setCurrPageAllComics(23);
        assertNotEquals(viewModel.getCurrPageAllComics(), 0);
        assertEquals(viewModel.getCurrPageAllComics(), 23);
    }

    @Test
    public void testSetCurrPageAllComics() {
        assertEquals(viewModel.getCurrPageAllComics(), 0);
        viewModel.setCurrPageAllComics(23);
        assertNotEquals(viewModel.getCurrPageAllComics(), 0);
        assertEquals(viewModel.getCurrPageAllComics(), 23);
    }

    @Test
    public void testGetCurrPageFavComics() {
        assertEquals(viewModel.getCurrPageFavComics(), 0);
        viewModel.setCurrPageFavComics(47);
        assertNotEquals(viewModel.getCurrPageFavComics(), 0);
        assertEquals(viewModel.getCurrPageFavComics(), 47);
    }

    @Test
    public void testSetCurrPageFavComics() {
        assertEquals(viewModel.getCurrPageFavComics(), 0);
        viewModel.setCurrPageFavComics(47);
        assertNotEquals(viewModel.getCurrPageFavComics(), 0);
        assertEquals(viewModel.getCurrPageFavComics(), 47);
    }

    @Test
    public void testSetFavoriteTab() {
        assertTrue(viewModel.isNotFavoriteTab());
        viewModel.setFavoriteTab(true);
        assertFalse(viewModel.isNotFavoriteTab());
    }

    @Test
    public void testIsNotFavoriteTab() {
        assertTrue(viewModel.isNotFavoriteTab());
        viewModel.setFavoriteTab(true);
        assertFalse(viewModel.isNotFavoriteTab());
    }

    @Test
    public void testGetComicFromServer() {
        viewModel.getComicFromServer(1, comic -> assertEquals(comic.getNum(), 1));
        viewModel.getComicFromServer(2, comic -> assertEquals(comic.getNum(), 2));
        viewModel.getComicFromServer(3, comic -> assertEquals(comic.getNum(), 3));
    }

    @Test
    public void testGetComicsFromServer() {
        int firstComicOnCurrPage = 23 * viewModel.getMaxComicsPerPage() + 1;
        int lastComicOnCurrPage = firstComicOnCurrPage + viewModel.getMaxComicsPerPage() - 1;
        viewModel.getAllComicsOnDevice().observe(lifecycleOwner, comics -> {
            for (Comic comic : comics) {
                assertTrue(comic.getNum() >= firstComicOnCurrPage
                        && comic.getNum() <= lastComicOnCurrPage);
            }
        });
        viewModel.getComicsFromServer(23);
    }

    // test for non-favorite comics only
    @Test
    public void testGetComicsForCurrPageOnly1() {
        viewModel.setFavoriteTab(false);
        viewModel.getAllComicsOnDevice().observe(lifecycleOwner, comics -> {
            assertEquals(comics.size(), viewModel.getMaxComicsPerPage());
            Collections.shuffle(comics);
            List<Comic> currPageComics = viewModel.getComicsForCurrPageOnly(comics);
            int numOfCurrComic = currPageComics.get(0).getNum();
            for (Comic currPageComic : currPageComics.subList(1, currPageComics.size())) {
                assertTrue(currPageComic.getNum() > numOfCurrComic);
                numOfCurrComic++;
            }
        });
        viewModel.getComicsFromServer(23);
    }

    // test for favorite comics only
    @Test
    public void testGetComicsForCurrPageOnly2() {
        viewModel.setFavoriteTab(true);
        viewModel.getAllComicsOnDevice().observe(lifecycleOwner, comics -> {
            assertEquals(comics.size(), viewModel.getMaxComicsPerPage());
            Collections.shuffle(comics);
            List<Comic> currPageComics = viewModel.getComicsForCurrPageOnly(comics);
            int numOfCurrComic = currPageComics.get(0).getNum();
            for (Comic currPageComic : currPageComics.subList(1, currPageComics.size())) {
                assertTrue(currPageComic.getNum() > numOfCurrComic);
                numOfCurrComic++;
            }
        });
        viewModel.getComicsFromServer(23);
    }

    @Test
    public void updateComicOnDevice() {
    }

    @Test
    public void deleteOnlyNonFavoriteComicsOnDevice() {
    }

    @Test
    public void forceRefresh() {
    }

    @Test
    public void deleteComic() {
    }

    @Test
    public void insertComic() {
    }

    @Test
    public void getAllComicsOnDevice() {
    }
}