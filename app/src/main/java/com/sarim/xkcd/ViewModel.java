package com.sarim.xkcd;

import android.app.Application;
import android.os.Handler;
import android.os.HandlerThread;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.sarim.xkcd.comic.Comic;
import com.sarim.xkcd.comic.ComicRepository;
import com.sarim.xkcd.retrofit.RetrofitHelper;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ViewModel extends AndroidViewModel {
    @NonNull
    private final ComicRepository comicRepository;
    private final LiveData<List<Comic>> allComics;

    // for communicating with sqlite
    private HandlerThread viewModelThread;
    private Handler viewModelHandler;

    // this will be used for retrieving comics from server
    private final RetrofitHelper retrofitHelper = new RetrofitHelper();

    // check which page the user is on when viewing all comics
    private final ObservableInt currPageAllComics = new ObservableInt(0);

    // check which page the user is on when viewing only favorite comics
    private final ObservableInt currPageFavComics = new ObservableInt(0);

    // the way comics are sorted on each page depends on whether you are in the favorites tab or not
    private final ObservableBoolean favoriteTab = new ObservableBoolean(false);

    // this is what the user will see on the edit text dialog
    public ObservableInt editTextCurrPage = new ObservableInt(
            currPageAllComics, currPageFavComics, favoriteTab
    ) {
        @Override
        public int get() {
            if (favoriteTab.get()) {
                return currPageFavComics.get();
            }
            else {
                return currPageAllComics.get();
            }
        }
    };

    // show only a certain number of comics per page
    private static final int MAX_COMICS_PER_PAGE = 5;

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

    public void getRecentlyAddedComic(Consumer<Comic> comicConsumer) {
        retrofitHelper.getRecentlyAddedComic(comicConsumer::accept);
    }

    public int getCurrPageAllComics() {
        return currPageAllComics.get();
    }

    public void setCurrPageAllComics(int currPageAllComics) {
        this.currPageAllComics.set(Math.max(0, currPageAllComics));
    }

    public int getCurrPageFavComics() {
        return currPageFavComics.get();
    }

    public void setCurrPageFavComics(int currPageFavComics) {
        this.currPageFavComics.set(Math.max(0, currPageFavComics));
    }

    public void setFavoriteTab(boolean favoriteTab) {
        this.favoriteTab.set(favoriteTab);
    }

    public boolean isNotFavoriteTab() {
        return !favoriteTab.get();
    }

    /**
     * retrieve specific comic using just the number value and display it in a separate activity
     * @param num the number for which you wish to retrieve the comic
     * @param comicConsumer what you do with the comic after retrieving it
     */
    public void getComicFromServer(int num, Consumer<Comic> comicConsumer) {
        retrofitHelper.getComic(num, comicConsumer::accept);
    }

    /**
     * First obtain the new range of numbers from the provided page number for which you want to try
     * to grab comics from the server. If you get at least one comic in the new range, then apply
     * the page number, since each page must have at least 1 comic to view
     * @param pageNumber pass a value that will be used to find comics in a range
     */
    public void getComicsFromServer(int pageNumber) {
        int firstComicOnCurrPage = pageNumber * MAX_COMICS_PER_PAGE + 1;
        int lastComicOnCurrPage = firstComicOnCurrPage + MAX_COMICS_PER_PAGE - 1;
        AtomicBoolean foundAtleastOneComicInNewRange = new AtomicBoolean(false);
        for (int id = firstComicOnCurrPage; id <= lastComicOnCurrPage; id++) {
            retrofitHelper.getComic(id, comic -> {
                if (comic != null) {
                    insertComicOnDevice(comic);
                    if (!foundAtleastOneComicInNewRange.get()) {
                        setCurrPageAllComics(pageNumber);
                        foundAtleastOneComicInNewRange.set(true);
                    }
                }
            });
        }
    }

    /**
     * For comics received from the server, always show them in ascending order of their number
     * value regardless of whether they are favorites or not. When the favorites tab is selected,
     * only show the favorite comics in ascending order of their number value
     * @param comics list of comics that will be sorted before being displayed
     * @return list of sorted comics according to the tab selected by the user
     */
    public List<Comic> getComicsForCurrPageOnly(List<Comic> comics) {
        // if you only have favorites on device, that probably means you are offline
        boolean onlyFavComicsOnDevice = comics.stream().allMatch(Comic::isFavorite);

        if (favoriteTab.get() || onlyFavComicsOnDevice) {
            comics.sort((comic1, comic2) -> {
                if (comic1.getNum() < comic2.getNum()) {
                    return -1;
                }
                else if (comic1.getNum() > comic2.getNum()) {
                    return 1;
                }
                return 0;
            });

            // think of a sliding window being passed over list of ints
            int maxLargeIndexOfFinalWindow = comics.size() - 1;
            int minLargeIndexOfFinalWindow = Math.max(0, maxLargeIndexOfFinalWindow - MAX_COMICS_PER_PAGE);
            int allowableMinIndexOfCurrentWindow, allowableMaxIndexOfCurrentWindow;
            int proposedMinIndexOfCurrentWindow = currPageFavComics.get() * MAX_COMICS_PER_PAGE;
            int proposedMaxIndexOfCurrentWindow = proposedMinIndexOfCurrentWindow + MAX_COMICS_PER_PAGE;

            if (proposedMinIndexOfCurrentWindow < 0) {
                allowableMinIndexOfCurrentWindow = 0;
            }
            else if (proposedMinIndexOfCurrentWindow >= comics.size()) {
                allowableMinIndexOfCurrentWindow = minLargeIndexOfFinalWindow;
            }
            else {
                allowableMinIndexOfCurrentWindow = proposedMinIndexOfCurrentWindow;
            }

            if (proposedMaxIndexOfCurrentWindow < 0) {
                allowableMaxIndexOfCurrentWindow = 0;
            }
            else if (proposedMaxIndexOfCurrentWindow >= comics.size()) {
                allowableMaxIndexOfCurrentWindow = maxLargeIndexOfFinalWindow;
            }
            else {
                allowableMaxIndexOfCurrentWindow = proposedMaxIndexOfCurrentWindow;
            }
            return comics.subList(
                    allowableMinIndexOfCurrentWindow,
                    allowableMaxIndexOfCurrentWindow + 1
            );
        }
        else {
            int firstComicOnCurrPage = currPageAllComics.get() * MAX_COMICS_PER_PAGE + 1;
            int lastComicOnCurrPage = firstComicOnCurrPage + MAX_COMICS_PER_PAGE - 1;
            return comics.stream()
                    .filter(comic -> comic.getNum() >= firstComicOnCurrPage
                            && comic.getNum() <= lastComicOnCurrPage)
                    .collect(Collectors.toList());
        }
    }

    private void insertComicOnDevice(Comic comic) {
        viewModelHandler.post(() -> comicRepository.insert(comic));
    }

    public void updateComicOnDevice(Comic comic) {
        viewModelHandler.post(() -> comicRepository.update(comic));
    }

    public void deleteOnlyNonFavoriteComicsOnDevice() {
        viewModelHandler.post(comicRepository::deleteNonFavorites);
    }

    public void forceRefresh() {
        List<Comic> comics = allComics.getValue();
        if (comics != null) {
            for (Comic comic : comics) {
                comic.setFavorite(comic.isFavorite());
                updateComicOnDevice(comic);
            }
        }
    }

    public void deleteComic(Comic comic) {
        viewModelHandler.post(() -> comicRepository.delete(comic));
    }

    public void insertComic(Comic comic) {
        viewModelHandler.post(() -> comicRepository.insert(comic));
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

