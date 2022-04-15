package com.sarim.xkcd;

import android.app.Application;
import android.os.Handler;
import android.os.HandlerThread;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.sarim.xkcd.room.Comic;
import com.sarim.xkcd.room.ComicRepository;

import java.util.List;

public class ViewModel extends AndroidViewModel {
    @NonNull
    private final ComicRepository comicRepository;
    private LiveData<List<Comic>> allComics;

    // for communicating with sqlite
    private HandlerThread viewModelThread;
    private Handler viewModelHandler;

    public ViewModel(@NonNull Application application) {
        super(application);
        comicRepository = new ComicRepository(application);
    }

    /**
     * create background thread for doing sqlite transactions
     */
    public void createBackgroundThreads() {
        viewModelThread = new HandlerThread("viewModelThread");
        viewModelThread.start();
        viewModelHandler = new Handler(viewModelThread.getLooper());
    }

    /**
     * called whenever the viewmodel is instantiated to get all comics on device
     */
    public void getComicsOnDevice() {
        allComics = comicRepository.getAllComics();
    }

    public void insertComic(Comic comic) {
        viewModelHandler.post(() -> comicRepository.insert(comic));
    }

    public void updateComic(Comic comic) {
        viewModelHandler.post(() -> comicRepository.update(comic));
    }

    public void deleteComic(Comic comic) {
        viewModelHandler.post(() -> comicRepository.delete(comic));
    }

    public void deleteAllComics() {
        viewModelHandler.post(comicRepository::deleteAll);
    }

    public LiveData<List<Comic>> getAllComics() {
        return allComics;
    }

    /**
     * safely quit the view model thread
     */
    public void quitThreads() {
        viewModelHandler.removeCallbacksAndMessages(null);
        viewModelThread.quitSafely();
    }
}

