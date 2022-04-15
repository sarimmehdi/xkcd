package com.sarim.xkcd;

import android.app.Application;
import android.os.Handler;
import android.os.HandlerThread;

import androidx.annotation.NonNull;
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
    private final RetrofitHelper retrofitHelper;

    public ViewModel(@NonNull Application application) {
        super(application);
        comicRepository = new ComicRepository(application);
        allComics = comicRepository.getAllComics();
        retrofitHelper = new RetrofitHelper();
    }

    /**
     * create background thread for doing sqlite transactions
     */
    public void createBackgroundThreads() {
        viewModelThread = new HandlerThread("viewModelThread");
        viewModelThread.start();
        viewModelHandler = new Handler(viewModelThread.getLooper());
    }

    public void getComicFromServer(int id) {
        retrofitHelper.getComic(
                id,
                this::insertComicOnDevice
        );
    }

    public void insertComicOnDevice(Comic comic) {
        viewModelHandler.post(() -> comicRepository.insert(comic));
    }

    public void updateComicOnDevice(Comic comic) {
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

