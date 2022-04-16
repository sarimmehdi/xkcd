package com.sarim.xkcd;

import android.app.Application;
import android.os.Handler;
import android.os.HandlerThread;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.sarim.xkcd.comic.Comic;
import com.sarim.xkcd.comic.ComicRepository;
import com.sarim.xkcd.retrofit.RetrofitHelper;

import java.util.List;

public class ViewModel extends AndroidViewModel {
    @NonNull
    private final ComicRepository comicRepository;
    private final LiveData<List<Comic>> allComics;

    // for communicating with sqlite
    private HandlerThread viewModelThread;
    private Handler viewModelHandler;

    // this will be used for retrieving comics from server
    private final RetrofitHelper retrofitHelper = new RetrofitHelper();

    // check which page the user is on
    public ObservableInt currPage = new ObservableInt(0);

    // check whether you are viewing favorite comics or all comics
    private boolean favoritesTab = false;

    // show only a certain number of comics per page
    private static int MAX_COMICS_PER_PAGE = 5;

    public ViewModel(@NonNull Application application) {
        super(application);
        comicRepository = new ComicRepository(application);
        allComics = comicRepository.getAllComics();
    }

    /**
     * create background thread for doing sqlite transactions
     */
    public void createBackgroundThreads() {
        viewModelThread = new HandlerThread("viewModelThread");
        viewModelThread.start();
        viewModelHandler = new Handler(viewModelThread.getLooper());
    }

    public void incCurrPage() {
        currPage.set(currPage.get() + 1);
    }

    public void decCurrPage() {
        currPage.set(Math.max(0, currPage.get() - 1));
    }

    public void setCurrPage(int currPage) {
        this.currPage.set(currPage);
    }

    public void setFavoritesTab(boolean favoritesTab) {
        this.favoritesTab = favoritesTab;
    }

    public void getComicsFromServer() {
        int firstComicOnCurrPage = currPage.get() * MAX_COMICS_PER_PAGE + 1;
        int lastComicOnCurrPage = firstComicOnCurrPage + MAX_COMICS_PER_PAGE - 1;
        for (int id = firstComicOnCurrPage; id <= lastComicOnCurrPage; id++) {
            retrofitHelper.getComic(id, this::insertComicOnDevice);
        }
    }

    private void insertComicOnDevice(Comic comic) {
        viewModelHandler.post(() -> comicRepository.insert(comic));
    }

    private void updateComicOnDevice(Comic comic) {
        viewModelHandler.post(() -> comicRepository.update(comic));
    }

    public void deleteComicOnDevice(Comic comic) {
        viewModelHandler.post(() -> comicRepository.delete(comic));
    }

    public void deleteAllComicsOnDevice() {
        viewModelHandler.post(comicRepository::deleteAll);
    }

    public LiveData<List<Comic>> getAllComicsOnDevice() {
        return allComics;
    }

    /**
     * safely quit the view model thread
     */
    public void quitThreads() {
        viewModelThread.quitSafely();
    }
}

