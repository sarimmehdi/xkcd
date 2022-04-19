package com.sarim.xkcd;

import static org.junit.Assert.*;

import android.app.Application;
import android.content.Context;
import android.view.View;

import androidx.lifecycle.Lifecycle;
import androidx.room.Room;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;

import com.sarim.xkcd.comic.ComicDatabase;
import com.sarim.xkcd.comic.ComicRepository;

import org.junit.Before;
import org.junit.Test;

public class MainActivityTest {

    ActivityScenario<MainActivity> scenario;
    ViewModel viewModel;

    @Before
    public void setUp() {
        scenario = ActivityScenario.launch(MainActivity.class);
        scenario.moveToState(Lifecycle.State.STARTED);
        Context context = ApplicationProvider.getApplicationContext();
        ComicDatabase comicDatabase = Room.inMemoryDatabaseBuilder(context, ComicDatabase.class)
                .allowMainThreadQueries().build();
        ComicRepository comicRepository = new ComicRepository(comicDatabase.comicDao());
        viewModel = new ViewModel(comicRepository, (Application) context);
    }

    @Test
    public void testOnStart() {
        scenario.onActivity(activity -> {
            activity.onAppStart.execute();
            assertFalse(viewModel.isFavoriteTab());
            assertEquals(
                    activity.comicListBinding.comicRetrievalProgressBar.getVisibility(), View.INVISIBLE
            );
        });
    }

    @Test
    public void testOnAllComicsBtnClicked() {
        scenario.onActivity(activity -> {
            activity.onAllComicsBtnClicked.execute();
            assertFalse(viewModel.isFavoriteTab());
            assertEquals(
                    activity.comicListBinding.comicRetrievalProgressBar.getVisibility(), View.INVISIBLE
            );
        });
    }

    @Test
    public void testOnEditTextChanged() {
        scenario.onActivity(activity -> {
            viewModel.setFavoriteTab(true);
            activity.onEditTextChanged.testExecute("324");
            assertEquals(
                    activity.comicListBinding.editTextPageNumber.getText().toString(), "324"
            );
        });
    }
}