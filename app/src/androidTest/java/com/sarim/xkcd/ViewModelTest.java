package com.sarim.xkcd;

import static org.junit.Assert.*;

import android.app.Application;
import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LifecycleOwner;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import com.sarim.xkcd.comic.Comic;
import com.sarim.xkcd.comic.ComicDatabase;
import com.sarim.xkcd.comic.ComicRepository;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

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
        assertFalse(viewModel.isFavoriteTab());
        viewModel.setFavoriteTab(true);
        assertTrue(viewModel.isFavoriteTab());
    }

    @Test
    public void testIsFavoriteTab() {
        assertFalse(viewModel.isFavoriteTab());
        viewModel.setFavoriteTab(true);
        assertTrue(viewModel.isFavoriteTab());
    }

    @Test
    public void testGetComicFromServer() {
        viewModel.getComicFromServer(1, comic -> assertEquals(comic.getNum(), 1));
        viewModel.getComicFromServer(2, comic -> assertEquals(comic.getNum(), 2));
        viewModel.getComicFromServer(3, comic -> assertEquals(comic.getNum(), 3));
    }

    @Test
    public void testCanChangeAllComicsPage() {
        viewModel.getComicsFromServer(1);
        viewModel.getComicsFromServer(2);
        viewModel.getComicsFromServer(3);
        AtomicInteger successCounter = new AtomicInteger();
        viewModel.canChangeAllComicsPage(1, successCounter::getAndIncrement);
        viewModel.canChangeAllComicsPage(2, successCounter::getAndIncrement);
        viewModel.canChangeAllComicsPage(3, () -> {
            successCounter.getAndIncrement();
            assertEquals(successCounter.get(), 3);
        });
        viewModel.canChangeAllComicsPage(4, Assert::fail);
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

    @Test
    public void testCanChangeFavComicsPage() {
        viewModel.getAllComicsOnDevice().observe(lifecycleOwner, comics -> {
            if (comics.size() == 8) {
                assertTrue(viewModel.canChangeFavComicsPage(1));
                assertTrue(viewModel.canChangeFavComicsPage(2));
                assertFalse(viewModel.canChangeFavComicsPage(3));
            }
        });
        List<Integer> favComicIds = Arrays.asList(1, 2, 3, 7, 15, 6, 9, 10);
        for (int id = 1; id <= 20; id++) {
            viewModel.getComicFromServer(id, comic -> {
                if (favComicIds.contains(comic.getNum())) {
                    comic.setFavorite(true);
                    viewModel.insertComic(comic);
                }
            });
        }
    }

    @Test
    public void testGetFavComicsForCurrPageOnly() {
        viewModel.getAllComicsOnDevice().observe(lifecycleOwner, comics -> {
            if (comics.size() == 8) {
                viewModel.setCurrPageFavComics(1);
                List<Comic> comicsForPage1 = viewModel.getFavComicsForCurrPageOnly(comics);
                List<Integer> favComicIdsPage1 = Arrays.asList(1, 2, 3, 7, 15);
                comicsForPage1.forEach(comic -> assertTrue(favComicIdsPage1.contains(comic.getNum())));
                viewModel.setCurrPageFavComics(2);
                List<Comic> comicsForPage2 = viewModel.getFavComicsForCurrPageOnly(comics);
                List<Integer> favComicIdsPage2 = Arrays.asList(6, 9, 10);
                comicsForPage2.forEach(comic -> assertTrue(favComicIdsPage2.contains(comic.getNum())));
            }
        });
        List<Integer> favComicIds = Arrays.asList(1, 2, 3, 7, 15, 6, 9, 10);
        for (int id = 1; id <= 20; id++) {
            viewModel.getComicFromServer(id, comic -> {
                if (favComicIds.contains(comic.getNum())) {
                    comic.setFavorite(true);
                    viewModel.insertComic(comic);
                }
            });
        }
    }

    @Test
    public void testGetAllComicsForCurrPageOnly() {
        viewModel.getAllComicsOnDevice().observe(lifecycleOwner, comics -> {
            if (comics.size() == 10) {
                viewModel.setCurrPageAllComics(1);
                List<Comic> comicsForPage1 = viewModel.getAllComicsForCurrPageOnly(comics);
                List<Integer> allComicIdsPage1 = Arrays.asList(1, 2, 3, 4, 5);
                comicsForPage1.forEach(comic -> assertTrue(allComicIdsPage1.contains(comic.getNum())));
                viewModel.setCurrPageAllComics(2);
                List<Comic> comicsForPage2 = viewModel.getAllComicsForCurrPageOnly(comics);
                List<Integer> allComicIdsPage2 = Arrays.asList(6, 7, 8, 9, 10);
                comicsForPage2.forEach(comic -> assertTrue(allComicIdsPage2.contains(comic.getNum())));
            }
        });
        viewModel.getComicsFromServer(1);
        viewModel.getComicsFromServer(2);
    }

    @Test
    public void testInsertComicOnDevice() {
        viewModel.getAllComicsOnDevice().observe(lifecycleOwner, comics -> {
            if (comics.size() == 5) {
                List<Integer> comicIdsToCheckAgainst = Arrays.asList(1, 2, 3, 4, 5);
                comics.forEach(comic -> assertTrue(comicIdsToCheckAgainst.contains(comic.getNum())));
            }
        });
        viewModel.getComicFromServer(1, comic -> viewModel.insertComic(comic));
        viewModel.getComicFromServer(2, comic -> viewModel.insertComic(comic));
        viewModel.getComicFromServer(3, comic -> viewModel.insertComic(comic));
        viewModel.getComicFromServer(4, comic -> viewModel.insertComic(comic));
        viewModel.getComicFromServer(5, comic -> viewModel.insertComic(comic));
    }

    @Test
    public void testUpdateComicOnDevice() {
        viewModel.getAllComicsOnDevice().observe(lifecycleOwner, comics -> {
            List<Integer> comicIdsToCheckAgainst = Arrays.asList(1, 2, 3, 4, 5);
            comics.forEach(comic -> {
                if (!comic.isFavorite()) {
                    comic.setFavorite(true);
                    viewModel.updateComicOnDevice(comic);
                }
                else {
                    assertTrue(comicIdsToCheckAgainst.contains(comic.getNum()));
                }
            });
        });
        viewModel.getComicFromServer(1, comic -> viewModel.insertComic(comic));
        viewModel.getComicFromServer(2, comic -> viewModel.insertComic(comic));
        viewModel.getComicFromServer(3, comic -> viewModel.insertComic(comic));
        viewModel.getComicFromServer(4, comic -> viewModel.insertComic(comic));
        viewModel.getComicFromServer(5, comic -> viewModel.insertComic(comic));
    }

    @Test
    public void testDeleteOnlyNonFavoriteComicsOnDevice() {
        viewModel.getAllComicsOnDevice().observe(lifecycleOwner, comics -> {
            List<Integer> favComicIds = Arrays.asList(1, 2, 5);
            List<Integer> nonFavComicIds = Arrays.asList(3, 4);
            comics.forEach(comic -> {
                if (comic.isFavorite()) {
                    assertTrue(favComicIds.contains(comic.getNum()));
                }
                else {
                    assertTrue(nonFavComicIds.contains(comic.getNum()));
                }
            });
        });
        viewModel.getComicFromServer(1, comic -> {
            comic.setFavorite(true);
            viewModel.insertComic(comic);
        });
        viewModel.getComicFromServer(2, comic -> {
            comic.setFavorite(true);
            viewModel.insertComic(comic);
        });
        viewModel.getComicFromServer(3, comic -> viewModel.insertComic(comic));
        viewModel.getComicFromServer(4, comic -> viewModel.insertComic(comic));
        viewModel.getComicFromServer(5, comic -> {
            comic.setFavorite(true);
            viewModel.insertComic(comic);
        });
    }

    @Test
    public void testForceRefresh() {
        AtomicBoolean forceRefreshCalled = new AtomicBoolean(false);
        viewModel.getAllComicsOnDevice().observe(lifecycleOwner, comics -> {
            if (forceRefreshCalled.get()) {
                assertTrue(true);
            }
            else {
                forceRefreshCalled.set(true);
                viewModel.forceRefresh();
            }
        });
        viewModel.getComicFromServer(1, comic -> viewModel.insertComic(comic));
    }

    @Test
    public void testDeleteComic() {
        AtomicBoolean checkForDeletedComic = new AtomicBoolean(false);
        viewModel.getAllComicsOnDevice().observe(lifecycleOwner, comics -> {
            if (checkForDeletedComic.get()) {
                assertEquals(comics.size(), 1);
                Comic comic = comics.get(0);
                assertEquals(comic.getNum(), 1);
            }
            else {
                checkForDeletedComic.set(true);
                comics.stream()
                        .filter(comic -> comic.getNum() == 2)
                        .findAny()
                        .ifPresent(comic -> viewModel.deleteComic(comic));
            }

        });
        viewModel.getComicFromServer(1, comic -> viewModel.insertComic(comic));
        viewModel.getComicFromServer(2, comic -> viewModel.insertComic(comic));
    }

    @Test
    public void insertComic() {
        viewModel.getAllComicsOnDevice().observe(lifecycleOwner, comics -> {
            assertEquals(comics.size(), 5);
            List<Integer> comicIdsToCheckAgainst = Arrays.asList(1, 2, 7, 9, 10);
            comics.forEach(comic -> assertTrue(comicIdsToCheckAgainst.contains(comic.getNum())));
        });
        viewModel.getComicFromServer(1, comic -> viewModel.insertComic(comic));
        viewModel.getComicFromServer(2, comic -> viewModel.insertComic(comic));
        viewModel.getComicFromServer(7, comic -> viewModel.insertComic(comic));
        viewModel.getComicFromServer(9, comic -> viewModel.insertComic(comic));
        viewModel.getComicFromServer(10, comic -> viewModel.insertComic(comic));
    }
}